package rutok.auth.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthModel {

    private String accessToken;

    private String refreshToken;

}
