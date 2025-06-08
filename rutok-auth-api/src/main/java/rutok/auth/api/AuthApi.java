package rutok.auth.api;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import rutok.auth.dto.*;

@Tag(name = "Auth", description = "Сервис авторизации/аутентификации")
public interface AuthApi {

    @PostMapping("/api/login")
    @Operation(summary = "Аутентификация пользователя")
    ResponseEntity<AuthDto> login(@RequestBody LoginRequest request);

    @PostMapping("/api/refresh")
    @Operation(summary = "Обновление пары JWT токенов")
    ResponseEntity<AuthDto> refresh(@RequestBody TokenRequest request);

    @PostMapping("/api/logout")
    @Operation(summary = "Удаление активных сессий")
    void logout(@RequestBody TokenRequest request);

    @PostMapping("/api/registration")
    @Operation(summary = "Регистрация пользователя")
    void registration(@RequestBody RegisterDto registerDto);

}
