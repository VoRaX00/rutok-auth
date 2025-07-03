package rutok.auth;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.cloud.openfeign.*;

@EnableFeignClients
@SpringBootApplication
public class RutokAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(RutokAuthApplication.class, args);
    }

}
