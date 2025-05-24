package rutok.auth.exception;

import static java.util.stream.Collectors.*;

import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.validation.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import rutok.auth.dto.*;
import rutok.auth.exceptions.*;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler {

    private static final String RETRY_TYPE = "RETRY";

    private static final String FINAL_TYPE = "FINAL";

    private static final String ERROR_SERVER =
        "Что-то пошло не так. Внутренняя ошибка сервера";

    private static final String ERROR_VALIDATION_NULL =
        "Ошибка запроса, отсутствуют следующие параметры запроса:";

    private static final String ERROR_VALIDATION_PATTERN =
        "Ошибка запроса, некорректные данные в следующих параметрах:";

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseError> handleRuntimeException(RuntimeException ex) {
        log.error("Unexpected exception", ex);
        var dto = new ResponseError(FINAL_TYPE, ERROR_SERVER);

        return ResponseEntity
            .status(INTERNAL_SERVER_ERROR)
            .body(dto);
    }

    @ExceptionHandler(AuthAppException.class)
    public ResponseEntity<ResponseError> handleDefaultException(AuthAppException ex) {
        log.error("Unexpected exception", ex);
        var dto = new ResponseError(RETRY_TYPE, ex.getMessage());

        return ResponseEntity
            .status(ex.getHttpStatus())
            .body(dto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handleValidationException(
        MethodArgumentNotValidException ex
    ) {
        log.error("Unexpected exception", ex);
        var message = getValidationErrorMessage(ex.getBindingResult());
        var dto = new ResponseError(RETRY_TYPE, message);

        return ResponseEntity
            .status(BAD_REQUEST)
            .body(dto);
    }

    private String getValidationErrorMessage(BindingResult bindingResult) {
        var message = new StringBuilder();
        var errors = bindingResult.getFieldErrors();

        var nullFields = errors.stream()
            .filter(e -> "NotNull".equals(e.getCode()))
            .toList();

        if (!nullFields.isEmpty()) {
            var badFields = nullFields.stream()
                .map(FieldError::getField)
                .collect(joining(", "));

            message.append(ERROR_VALIDATION_NULL);
            message.append(badFields);
            return message.toString();
        }

        var invalidFields = errors.stream()
            .filter(e -> "Pattern".equals(e.getCode()))
            .toList();

        if (!invalidFields.isEmpty()) {
            var badFields = invalidFields.stream()
                .map(f -> f.getField() + " - " + f.getRejectedValue())
                .collect(joining(", "));

            message.append(ERROR_VALIDATION_PATTERN);
            message.append(badFields);
            return message.toString();
        }

        log.warn("No errors for validation exception");
        return "No errors";
    }

}
