package rutok.auth.exceptions;

import org.springframework.http.*;

public class UnauthorizedException extends BaseException {

    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedException(String message, Throwable e) {
        super(message, e, HttpStatus.UNAUTHORIZED);
    }

}
