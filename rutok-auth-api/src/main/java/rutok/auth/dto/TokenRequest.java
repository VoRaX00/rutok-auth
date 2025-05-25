package rutok.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Schema(description = "Запрос для передачи токена(access или refresh)")
public class TokenRequest {

    @Schema(description = "Токен")
    private String token;

}
