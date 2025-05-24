package rutok.auth;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import static lombok.AccessLevel.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class JwtProperties {

    @Value("${jwt.privateKey:#{null}}")
    private String privateKey;

    @Value("${jwt.publicKey:#{null}}")
    private String publicKey;

    @Value("${jwt.access-timeout-seconds:300}")
    private Integer accessTokenExpiredOnSeconds;

    @Value("${jwt.refresh-token-expired-on-seconds:604800}")
    private Integer refreshTokenExpiredOnSeconds;

}
