package rutok.auth.exception;

import org.springframework.http.*;
import rutok.auth.exceptions.*;

public class ConflictException extends BaseException {

    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause, HttpStatus.CONFLICT);
    }

}
