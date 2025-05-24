package rutok.auth.dto;

import io.swagger.v3.oas.annotations.media.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Запрос аутентификации")
public class LoginRequest {

    @Schema(description = "Почта пользователя")
    private String email;

    @Schema(description = "Пароль пользователя")
    private String password;

}
