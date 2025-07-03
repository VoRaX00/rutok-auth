package rutok.auth.service;

import java.time.*;
import java.util.*;

import feign.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;
import rutok.auth.*;
import rutok.auth.client.*;
import rutok.auth.dto.*;
import rutok.auth.entity.*;
import rutok.auth.exception.*;
import rutok.auth.exceptions.*;
import rutok.auth.mapper.*;
import rutok.auth.model.*;
import rutok.auth.repository.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String ROLE_USER_CODE = "USER";

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserClient userClient;

    private final CredentialRepository credentialRepository;

    private final UserMapper userMapper;

    @Getter
    private final JwtProperties jwtProps;

    @Getter
    private final JwtProvider jwtProvider;

    @Getter
    private final JwtVerifier jwtVerifier;

    public AuthModel login(LoginRequest loginRequests) {
        var salt = findSaltByUserEmail(loginRequests.getEmail());
        if (salt == null || salt.isBlank()) {
            throw new AuthenticationException();
        }

        var hash = BCrypt.hashpw(loginRequests.getPassword(), salt);
        loginRequests.setPassword(hash);
        var user = getUser(loginRequests);

        var id = findCredentialId(user.getEmail(), hash);
        if (id == null) {
            throw new AuthenticationException();
        }

        var model = userMapper.toModel(user);
        var principal = buildPrincipal(model);

        var accessJti = UUID.randomUUID();
        var refreshJti = UUID.randomUUID();
        var access = getJwtProvider().generateAccess(accessJti, principal);
        var refresh = getJwtProvider().generateRefresh(refreshJti, principal);

        var now = ZonedDateTime.now();
        var expireAt = now.plusSeconds(getJwtProps().getRefreshTokenExpiredOnSeconds());

        var token = RefreshToken.builder()
            .userId(user.getId())
            .token(refresh)
            .accessJti(accessJti)
            .refreshJti(refreshJti)
            .issuedAt(now)
            .expiredAt(expireAt)
            .build();

        saveRefreshToken(token);
        return new AuthModel(access, refresh);
    }

    @Transactional
    public AuthDto issueTokens(UserModel user) {
        var principal = buildPrincipal(user);

        var accessJti = UUID.randomUUID();
        var refreshJti = UUID.randomUUID();

        var access = jwtProvider.generateAccess(accessJti, principal);
        var refresh = jwtProvider.generateRefresh(refreshJti, principal);

        var now = ZonedDateTime.now();
        var expireAt = now.plusSeconds(jwtProps.getRefreshTokenExpiredOnSeconds());

        var token = RefreshToken.builder()
            .userId(user.getId())
            .token(refresh)
            .accessJti(accessJti)
            .refreshJti(refreshJti)
            .issuedAt(now)
            .expiredAt(expireAt)
            .build();

        refreshTokenRepository.save(token);

        return new AuthDto(access, refresh);
    }

    @Transactional
    public AuthModel refresh(String refreshToken) {
        var decoded = getJwtVerifier().verify(refreshToken);

        var jti = UUID.fromString(decoded.getId());
        var old = findRefreshTokenByJti(jti);
        if (old == null) {
            throw new JwtTokenException();
        }

        var user = getUser(old.getUserId());
        var model = userMapper.toModel(user);
        var principal = buildPrincipal(model);

        var accessJti = UUID.randomUUID();
        var refreshJti = UUID.randomUUID();
        var access = getJwtProvider().generateAccess(accessJti, principal);
        var refresh = getJwtProvider().generateRefresh(refreshJti, principal);

        var token = RefreshToken.builder()
            .userId(user.getId())
            .token(refresh)
            .accessJti(accessJti)
            .refreshJti(refreshJti)
            .issuedAt(ZonedDateTime.now())
            .expiredAt(ZonedDateTime.now().plusDays(7))
            .build();

        refreshTokenRepository.deleteByRefreshJti(jti);
        refreshTokenRepository.save(token);
        return new AuthModel(access, refresh);
    }

    @Transactional
    public void logout(String refreshToken) {
        var decoded = getJwtVerifier().verify(refreshToken);
        var userId = decoded.getClaim("user_id").asLong();

        refreshTokenRepository.deleteByUserId(userId);
    }

    @Transactional
    public AuthDto register(RegisterDto registerDto) {
        var salt = jwtProvider.generateSalt();
        var hashedPassword = jwtProvider.hashPassword(registerDto.getPassword(), salt);
        registerDto.setPassword(hashedPassword);
        var response = createUser(registerDto);
        var model = userMapper.toModel(registerDto);
        model.setId(response.getId());
        model.setRoleCode(response.getRoleCode());

        credentialRepository.save(Credential.builder()
            .id(model.getId())
            .userEmail(model.getEmail())
            .salt(salt)
            .hash(hashedPassword)
            .build());
        return issueTokens(model);
    }

    public String hashPassword(String email, String password) {
        var credential = credentialRepository.findByUserEmail(email)
            .orElseThrow(() -> new BadRequestException("Неверная почта"));
        var salt = credential.getSalt();
        if (salt == null || salt.isBlank()) {
            throw new AuthenticationException();
        }

        var passwordHash = jwtProvider.hashPassword(password, salt);
        credential.setHash(passwordHash);
        credentialRepository.save(credential);
        return passwordHash;
    }

    private CreateUserResponse createUser(RegisterDto registerDto) {
        try {
            var id = userClient.createUser(registerDto);
            return Optional.ofNullable(id)
                .map(HttpEntity::getBody)
                .orElseThrow(() -> new InternalServerException("Не удалось получить id пользователя"));
        } catch (FeignException.BadRequest e) {
            throw new BadRequestException(e.getMessage(), e);
        } catch (FeignException.Conflict e) {
            throw new ConflictException(e.getMessage(), e);
        } catch (FeignException.InternalServerError e) {
            throw new InternalServerException(
                "Ошибка сервера пользователей. Не удалось создать пользователя", e
            );
        } catch (FeignException e) {
            throw new InternalServerException(
                "Ошибка сервера при обращении к микросервису пользователей", e
            );
        }
    }

    private UserDto getUser(LoginRequest loginRequest) {
        try {
            var result = userClient.check(loginRequest);
            return Optional.ofNullable(result)
                .map(HttpEntity::getBody)
                .orElseThrow(() -> new InternalServerException(
                    "Не удалось получить информацию о пользователе"));
        } catch (FeignException.BadRequest e) {
            throw new BadRequestException(e.getMessage(), e);
        } catch (FeignException.NotFound e) {
            throw new NotFoundException(e.getMessage(), e);
        } catch (FeignException.InternalServerError e) {
            throw new InternalServerException(
                "Ошибка сервера пользователей. Не удалось пройти аутентификацию", e
            );
        } catch (FeignException e) {
            throw new InternalServerException(
                "Ошибка сервера. Не удалось обратиться к сервису пользователей", e
            );
        }
    }

    private UserDto getUser(Long userId) {
        try {
            var result = userClient.getUser(userId);
            return Optional.ofNullable(result)
                .map(HttpEntity::getBody)
                .orElseThrow(() -> new InternalServerException(
                    "Не удалось получить информацию о пользователе"));
        } catch (FeignException.BadRequest e) {
            throw new BadRequestException(e.getMessage(), e);
        } catch (FeignException.NotFound e) {
            throw new NotFoundException(e.getMessage(), e);
        } catch (FeignException.InternalServerError e) {
            throw new InternalServerException(
                "Ошибка сервера пользователей. Не удалось пройти аутентификацию", e
            );
        } catch (FeignException e) {
            throw new InternalServerException(
                "Ошибка сервера. Не удалось обратиться к сервису пользователей", e
            );
        }
    }

    private RefreshToken findRefreshTokenByJti(UUID refreshJti) {
        return refreshTokenRepository.findByRefreshJti(refreshJti);
    }

    private String findSaltByUserEmail(String userEmail) {
        var credentials = credentialRepository.findByUserEmail(userEmail)
            .orElseThrow(() -> new NotFoundException("Пользователь с таким email не найден"));
        return credentials.getSalt();
    }

    private Credential findCredentialId(String userEmail, String hash) {
        return credentialRepository.findByUserEmailAndHash(userEmail, hash)
            .orElseThrow(() -> new NotFoundException("Пользователь с таким email и hash не найден"));
    }

    private Principal buildPrincipal(UserModel user) {
        return Principal.builder()
            .id(user.getId())
            .login(user.getEmail())
            .roles(List.of(user.getRoleCode()))
            .build();
    }

    private void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

}
