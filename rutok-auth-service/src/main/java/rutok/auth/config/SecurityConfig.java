package rutok.auth.config;

import java.util.*;

import lombok.*;
import lombok.extern.slf4j.*;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.web.*;
import org.springframework.web.cors.*;
import rutok.auth.*;

import static org.springframework.security.config.http.SessionCreationPolicy.*;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    private final JwtAuthFilter jwtFilter;

    private String[] allowedUrls = new String[]{
        "/swagger-ui/**",
        "/v3/**",
        "/api/login",
        "/api/logout",
        "/api/refresh",
        "/api/registration",
        "/api/hash"
    };

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("Loockup SecurityFilterChain");
        return http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(c -> c
                .requestMatchers(allowedUrls).permitAll()
                .anyRequest().authenticated())
            .sessionManagement(c -> c
                .sessionCreationPolicy(STATELESS))
            .build();
    }

}
