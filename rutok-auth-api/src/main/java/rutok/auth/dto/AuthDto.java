package rutok.auth.dto;

import io.swagger.v3.oas.annotations.media.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ аутентификации")
public class AuthDto {

    @Schema(description = "Access токен")
    private String accessToken;

    @Schema(description = "Refresh токен")
    private String refreshToken;

}
