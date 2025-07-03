package rutok.auth.controllers;

import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import rutok.auth.api.*;
import rutok.auth.dto.*;
import rutok.auth.mapper.*;
import rutok.auth.service.*;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {

    private final AuthService authService;

    private final AuthMapper authMapper;

    private final UserMapper userMapper;

    @Override
    public ResponseEntity<AuthDto> login(LoginRequest loginRequest) {
        var model = authService.login(loginRequest);
        var dto = authMapper.toDto(model);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<AuthDto> refresh(TokenRequest request) {
        var model = authService.refresh(request.getToken());
        var dto = authMapper.toDto(model);
        return ResponseEntity.ok(dto);
    }

    @Override
    public void logout(TokenRequest request) {
        var token = request.getToken();
        authService.logout(token);
    }

    @Override
    public ResponseEntity<AuthDto> registration(RegisterDto registerDto) {
        return new ResponseEntity<>(authService.register(registerDto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<HashedPasswordDto> hash(PasswordDto passwordDto) {
        var hashedPassword = authService.hashPassword(
            passwordDto.getEmail(),
            passwordDto.getPassword());
        return ResponseEntity.ok(new HashedPasswordDto(hashedPassword));
    }

}
