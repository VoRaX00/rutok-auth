package rutok.auth.exceptions;

import lombok.*;
import org.springframework.http.*;

@Getter
public class BaseException extends RuntimeException {

    private final HttpStatus httpStatusCode;

    public BaseException(String message, HttpStatus httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public BaseException(String message, Throwable e, HttpStatus httpStatusCode) {
        super(message, e);
        this.httpStatusCode = httpStatusCode;
    }

}
