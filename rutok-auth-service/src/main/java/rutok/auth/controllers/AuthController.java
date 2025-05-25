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

    @Override
    public ResponseEntity<AuthDto> login(LoginRequest loginRequest) {
        var loginModel = authMapper.toModel(loginRequest);
        var model = authService.login(loginModel);
        var dto = authMapper.toDto(model);
        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<AuthDto> refresh(TokenRequest request) {
        var model = authService.refresh(request.getToken());
        var dto = authMapper.toDto(model);
        return ResponseEntity.ok(dto);
    }

}
