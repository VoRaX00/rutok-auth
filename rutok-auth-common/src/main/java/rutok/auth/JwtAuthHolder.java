package rutok.auth;

import java.util.*;

import org.springframework.security.core.*;
import org.springframework.security.core.context.*;

public class JwtAuthHolder {

    public Authentication get() {
        var ctx = SecurityContextHolder.getContext();
        var auth = Optional.ofNullable(ctx)
            .map(SecurityContext::getAuthentication)
            .orElse(null);
        return auth;
    }

    public void set(Authentication auth) {
        var ctx = SecurityContextHolder.getContext();
        if (ctx != null) {
            ctx.setAuthentication(auth);
        }
    }

    public void forget() {
        var ctx = SecurityContextHolder.getContext();
        if (ctx != null) {
            ctx.setAuthentication(null);
        }
    }

}
