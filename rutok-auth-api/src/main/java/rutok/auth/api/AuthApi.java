package rutok.auth.api;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import rutok.auth.dto.*;

@Tag(name = "Auth", description = "Сервис авторизации/аутентификации")
public interface AuthApi {

    @PostMapping("/api/login")
    @Operation(summary = "Аутентификация пользователя")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Успешная регистрация пользователя",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Неверные почта или пароль",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ResponseError.class),
                examples = @ExampleObject(value = """
                    {
                        "type": "RETRY",
                        "message": "Неверные почта или пароль"
                    }
                    """)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Внутренняя ошибка сервера",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ResponseError.class),
                examples = @ExampleObject(value = """
                    {
                        "type": "FINAL",
                        "message": "Внутрення ошибка сервера. Попробуйте позже"
                    }
                    """)
            )
        )
    })
    ResponseEntity<AuthDto> login(@RequestBody LoginRequest request);

    @PostMapping("/api/refresh")
    @Operation(summary = "Обновление пары JWT токенов")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Пара токенов обновлены",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthDto.class)
            )
        ),
    })
    ResponseEntity<AuthDto> refresh(@RequestBody TokenRequest request);

    @PostMapping("/api/logout")
    @Operation(summary = "Удаление активных сессий")
    void logout(@RequestBody TokenRequest request);

    @PostMapping("/api/registration")
    @Operation(summary = "Регистрация пользователя")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Успешная регистрация пользователя",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthDto.class)
            )
        )
    })
    ResponseEntity<AuthDto> registration(@RequestBody RegisterDto registerDto);

    @PostMapping("/api/hash")
    @Operation(summary = "Хеширование паролей")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Успешный ответ, пароль",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = HashedPasswordDto.class)
            )
        )
    })
    ResponseEntity<HashedPasswordDto> hash(@RequestBody PasswordDto passwordDto);

}
