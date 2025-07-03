package rutok.auth.config;

import java.util.*;

import feign.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import rutok.auth.*;
import rutok.auth.exceptions.*;

@Configuration
@RequiredArgsConstructor
public class FeignConfig {

    private final JwtAuthHolder jwtAuthHolder;

//    @Bean
//    public RequestInterceptor requestInterceptor() {
//        return requestTemplate -> {
//            var token = Optional.ofNullable(jwtAuthHolder.get())
//                .filter(JwtAuth.class::isInstance)
//                .map(JwtAuth.class::cast)
//                .map(JwtAuth::getToken)
//                .orElse(null);
//
//            if (token == null || token.isEmpty()) {
//                throw new UnauthorizedException("Не найден токен для авторизации");
//            }
//
//            requestTemplate.header("Authorization", "Bearer " + token);
//        };
//    }

}
