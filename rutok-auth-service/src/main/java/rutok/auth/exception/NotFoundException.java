package rutok.auth.exception;

import org.springframework.http.*;
import rutok.auth.exceptions.*;

public class NotFoundException extends BaseException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public NotFoundException(String message, Throwable e) {
        super(message, e, HttpStatus.NOT_FOUND);
    }

}
