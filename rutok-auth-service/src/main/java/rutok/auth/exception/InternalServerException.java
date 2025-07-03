package rutok.auth.exception;

import org.springframework.http.*;
import rutok.auth.exceptions.*;

public class InternalServerException extends BaseException {

    public InternalServerException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public InternalServerException(String message, Throwable e) {
        super(message, e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
