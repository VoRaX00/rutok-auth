package rutok.auth.config;

import com.fasterxml.jackson.databind.*;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.*;
import rutok.auth.*;

import static org.springframework.core.Ordered.*;

@Configuration
public class AuthConfig {

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtContext jwtContext(JwtProperties properties) {
        return new JwtContext(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthHolder jwtAuthHolder() {
        return new JwtAuthHolder();
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtProvider jwtProvider(JwtProperties props, JwtContext context) {
        return new JwtProvider(props, context);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtVerifier jwtVerifier(JwtContext context) {
        return new JwtVerifier(context);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthEntryPoint jwtAuthEntryPoint(ObjectMapper objectMapper) {
        return new JwtAuthEntryPoint(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthFilter jwtAuthFilter(
            JwtAuthHolder holder,
            JwtVerifier verifier,
            JwtAuthEntryPoint entryPoint
    ) {
        return new JwtAuthFilter(holder, verifier, entryPoint);
    }

}
