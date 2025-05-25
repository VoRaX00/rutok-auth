package rutok.auth.service;

import java.time.*;
import java.util.*;

import jakarta.transaction.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.stereotype.*;
import rutok.auth.*;
import rutok.auth.entity.*;
import rutok.auth.exception.*;
import rutok.auth.exceptions.*;
import rutok.auth.model.*;
import rutok.auth.repository.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String ROLE_USER_CODE = "ROLE_USER";

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final RoleRepository roleRepository;

    private final CredentialRepository credentialRepository;

    @Getter
    private final JwtProperties jwtProps;

    @Getter
    private final JwtProvider jwtProvider;

    @Getter
    private final JwtVerifier jwtVerifier;

    public AuthModel login(LoginModel loginModel) {
        var user = findUserByEmail(loginModel.getEmail());
        if (user == null) {
            throw new AuthenticationException();
        }

        var salt = findSaltByUserId(user.getId());
        if (salt == null || salt.isBlank()) {
            throw new AuthenticationException();
        }

        var hash = BCrypt.hashpw(loginModel.getPassword(), salt);
        var id = findCredentialId(user.getId(), hash);
        if (id == null) {
            throw new AuthenticationException();
        }

        var principal = buildPrincipal(user);

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
    public AuthModel refresh(String refreshToken) {
        var decoded = getJwtVerifier().verify(refreshToken);

        var jti = UUID.fromString(decoded.getId());
        var old = findRefreshTokenByJti(jti);
        if (old == null) {
            throw new JwtTokenException();
        }

        var user = userRepository.findUserById(old.getUserId())
            .orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден"));
        var principal = buildPrincipal(user);

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

    public void logout(String refreshToken) {
        var decoded = getJwtVerifier().verify(refreshToken);
        var userId = decoded.getClaim("user_id").asLong();

        refreshTokenRepository.deleteByUserId(userId);
    }

    public void register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ConflictException("Пользователь с таким email существует");
        }

        var userRole = roleRepository.findByCode(ROLE_USER_CODE)
            .orElseThrow(() -> new InternalServerException("Роль пользователя не найдена"));
        user.setRoles(Set.of(userRole));

        var salt = jwtProvider.generateSalt();
        var hashedPassword = jwtProvider.hashPassword(user.getPassword(), salt);

        userRepository.save(user);

        credentialRepository.save(Credential.builder()
            .id(user.getId())
            .user(user)
            .salt(salt)
            .hash(hashedPassword)
            .build());
    }

    private RefreshToken findRefreshTokenByJti(UUID refreshJti) {
        return refreshTokenRepository.findByRefreshJti(refreshJti);
    }

    private String findSaltByUserId(Long userId) {
        var credentials = credentialRepository.findByUserId(userId)
            .orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден"));
        return credentials.getSalt();
    }

    private Credential findCredentialId(Long userId, String hash) {
        return credentialRepository.findByUserIdAndHash(userId, hash)
            .orElseThrow(() -> new NotFoundException("Пользователь с таким id и hash не найден"));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("Пользователь с таким email не найден"));
    }

    private Principal buildPrincipal(User user) {
        var roles = user.getRoles().stream()
            .map(Role::getCode)
            .toList();

        return Principal.builder()
            .id(user.getId())
            .login(user.getEmail())
            .roles(roles)
            .build();
    }

    private void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

}
