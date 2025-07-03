package rutok.auth.exception;

import java.nio.file.*;
import java.util.*;

import com.fasterxml.jackson.databind.exc.*;
import jakarta.validation.*;
import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.http.converter.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.*;
import org.springframework.web.multipart.*;
import org.springframework.web.servlet.resource.*;
import rutok.auth.dto.*;
import rutok.auth.exceptions.*;

@Slf4j
@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseError> handle(AccessDeniedException error) {
        log.error("Access denied: {}", error.getMessage(), error);

        return new ResponseEntity<>(
            new ResponseError(
                "RETRY",
                error.getMessage() != null ? error.getMessage() : "Доступ запрещён"
            ),
            HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseError> handle(HttpMessageNotReadableException error) {
        log.error(error.getMessage(), error);

        var invalidFormatException = Optional.ofNullable(error.getCause())
            .map(InvalidFormatException.class::cast)
            .orElse(null);

        if (invalidFormatException == null) {
            return new ResponseEntity<>(
                new ResponseError(
                    "RETRY",
                    error.getMessage()
                ),
                HttpStatus.BAD_REQUEST
            );
        }

        var fieldName = "";
        if (!invalidFormatException.getPath().isEmpty()) {
            fieldName = invalidFormatException.getPath().getFirst().getFieldName();
        }

        String targetType = invalidFormatException.getTargetType() != null
            ? invalidFormatException.getTargetType().getSimpleName()
            : "неизвестный тип";

        var message = String.format(
            "Поле '%s' должно быть типа %s",
            fieldName,
            targetType
        );

        return new ResponseEntity<>(
            new
                ResponseError(
                "RETRY",
                message
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handle(MethodArgumentNotValidException error) {
        log.error(error.getMessage(), error);

        var invalidFields = error.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
            .toList();

        var message = String.join("; ", invalidFields);
        return new ResponseEntity<>(
            new ResponseError(
                "RETRY",
                message
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseError> handler(MethodArgumentTypeMismatchException exception) {
        log.error(exception.getMessage(), exception);

        var name = exception.getName();
        exception.getRequiredType();
        var type = exception.getRequiredType().getSimpleName();

        var message = String.format("Параметр %s должен быть типа %s", name, type);
        return new ResponseEntity<>(
            new ResponseError(
                "RETRY",
                message
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseError> handler(MaxUploadSizeExceededException exception) {
        log.error(exception.getMessage(), exception);
        return new ResponseEntity<>(
            new ResponseError(
                "RETRY",
                "Файл превышает максимальный размер"
            ),
            HttpStatus.PAYLOAD_TOO_LARGE
        );
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseError> handle(NotFoundException error) {
        log.error(error.getMessage(), error);
        return new ResponseEntity<>(
            new ResponseError(
                "RETRY",
                error.getMessage()
            ),
            error.getHttpStatusCode()
        );
    }

    @ExceptionHandler
    public ResponseEntity<ResponseError> handle(ConstraintViolationException error) {
        log.error(error.getMessage(), error);
        var messages = error.getConstraintViolations().stream()
            .map(ConstraintViolation::getMessage)
            .toList();

        var message = String.join("; ", messages);
        return new ResponseEntity<>(
            new ResponseError(
                "RETRY",
                message
            ),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<ResponseError> handle(NoResourceFoundException error) {
        log.error(error.getMessage(), error);
        return new ResponseEntity<>(
            new ResponseError(
                "RETRY",
                error.getMessage()
            ),
            error.getStatusCode()
        );
    }

    @ExceptionHandler
    public ResponseEntity<ResponseError> handle(Exception error) {
        log.error(error.getMessage(), error);
        return new ResponseEntity<>(
            new ResponseError(
                "FINAL",
                "Ошибка сервера. Пожалуйста подождите или обратитесь в поддержку"
            ),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ResponseError> handle(InternalServerException error) {
        log.error(error.getMessage(), error);
        return new ResponseEntity<>(
            new ResponseError(
                "FINAL",
                error.getMessage()
            ),
            error.getHttpStatusCode()
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ResponseError> handle(UnauthorizedException error) {
        log.error(error.getMessage(), error);
        return new ResponseEntity<>(
            new ResponseError(
                "RETRY",
                error.getMessage()
            ),
            error.getHttpStatusCode()
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseError> handle(BadRequestException error) {
        log.error(error.getMessage(), error);
        return new ResponseEntity<>(
            new ResponseError(
                "RETRY",
                error.getMessage()
            ),
            error.getHttpStatusCode()
        );
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ResponseError> handle(ConflictException error) {
        log.error(error.getMessage(), error);
        return new ResponseEntity<>(
            new ResponseError(
                "RETRY",
                error.getMessage()
            ),
            error.getHttpStatusCode()
        );
    }

}
