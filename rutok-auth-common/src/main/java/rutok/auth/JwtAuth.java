package rutok.auth;

import java.util.*;

import com.auth0.jwt.interfaces.*;
import lombok.*;
import org.springframework.security.core.*;

@Getter
@Setter
@Builder
public class JwtAuth implements Authentication {

    private boolean authenticated;

    private Long userId;

    private String login;

    private String token;

    private List<GrantedAuthority> roles;

    private Map<String, Claim> claims;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public Object getPrincipal() {
        return this;
    }

    @Override
    public String getName() {
        return login;
    }

    @Override
    public Object getDetails() {
        return claims;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

}
