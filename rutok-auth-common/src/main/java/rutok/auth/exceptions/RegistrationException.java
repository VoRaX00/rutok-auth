package rutok.auth.exceptions;

import org.springframework.http.*;

public class RegistrationException extends AuthorizationException {
    public RegistrationException() {
        super("Ошибка регистрации", HttpStatus.UNAUTHORIZED);
    }

}
