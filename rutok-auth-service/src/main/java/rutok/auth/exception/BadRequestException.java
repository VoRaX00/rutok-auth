package rutok.auth.exception;

import org.springframework.http.*;
import rutok.auth.exceptions.*;

public class BadRequestException extends BaseException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(String message, Throwable e) {
        super(message, e, HttpStatus.BAD_REQUEST);
    }

}
