package rutok.auth;

import java.io.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.security.core.*;
import org.springframework.web.filter.*;
import rutok.auth.exceptions.*;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends GenericFilterBean {

    private static final String AUTH_HEADER_PREFIX = "Bearer ";

    private static final String AUTHORIZATION_CLAIM = "Authorization";

    private final JwtAuthHolder tokenHolder;

    private final JwtVerifier jwtVerifier;

    private final JwtAuthEntryPoint entryPoint;

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain filterChain
    ) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        var httpResponse = (HttpServletResponse) response;
        doFilter(httpRequest, httpResponse, filterChain);
    }

    private void doFilter(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws IOException, ServletException {
        try {
            var tokenValue = getTokenValue(request);
            if (tokenValue != null) {
                var auth = processToken(tokenValue);
                tokenHolder.set(auth);
            }
        } catch (RuntimeException ex) {
            var error = new JwtTokenException(ex);
            entryPoint.commence(request, response, error);
            return;
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            tokenHolder.forget();
        }
    }

    private JwtAuth processToken(String tokenValue) {
        var decoded = jwtVerifier.verify(tokenValue);

        var roles = decoded.getClaim("roles")
            .asList(String.class).stream()
            .map(SimpleAuthority::new)
            .map(it -> (GrantedAuthority) it)
            .toList();

        var auth = JwtAuth.builder()
            .authenticated(true)
            .userId(decoded.getClaim("user_id").asLong())
            .login(decoded.getClaim("login").asString())
            .roles(roles)
            .build();

        return auth;
    }

    private String getTokenValue(HttpServletRequest request) {
        var bearer = request.getHeader(AUTHORIZATION_CLAIM);
        if (bearer != null && bearer.startsWith(AUTH_HEADER_PREFIX)) {
            return bearer.substring(AUTH_HEADER_PREFIX.length());
        }
        return null;
    }

    @RequiredArgsConstructor
    private static class SimpleAuthority implements GrantedAuthority {

        private final String role;

        @Override
        public String getAuthority() {
            return role;
        }

    }

}
