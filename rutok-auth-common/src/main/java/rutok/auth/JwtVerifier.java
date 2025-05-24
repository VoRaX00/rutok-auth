package rutok.auth;

import java.time.*;
import java.util.*;

import com.auth0.jwt.*;
import com.auth0.jwt.interfaces.*;
import lombok.*;
import lombok.extern.slf4j.*;
import rutok.auth.exceptions.*;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RequiredArgsConstructor
public class JwtVerifier {

    private final JwtContext context;

    public DecodedJWT verify(String value) {
        DecodedJWT token = null;
        try {
            token = JWT.decode(value);
            context.getAlgorithm().verify(token);
        } catch (Exception ex) {
            log.debug("Bad token: {}", value);

            var jti = token != null
                ? token.getId()
                : "";

            throw new AuthAppException(String.format(
                "Unable to verify token: '%s'",
                jti
            ), UNAUTHORIZED, ex);
        }

        var now = Instant.now();
        if (token.getExpiresAtAsInstant().isBefore(now)) {
            throw new IllegalStateException(String.format(
                "Token is expired: %s < %s",
                token.getExpiresAtAsInstant(),
                now
            ));
        }

        var isMissingRoles = Optional.of(token)
            .map(t -> t.getClaim("roles"))
            .filter(r -> !r.isMissing())
            .map(r -> r.asList(String.class))
            .map(List::isEmpty)
            .orElse(false);

        if (isMissingRoles) {
            throw new IllegalStateException(String.format(
                "Missing roles for token: %s",
                token.getId()
            ));
        }

        return token;
    }

}
