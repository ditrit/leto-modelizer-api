package com.ditrit.letomodelizerapi.controller.handler;

import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorDTO;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.Iterator;
import java.util.Optional;

/**
 * Handle api exception and send appropriate response.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link ApiException} by returning a JSON response with the error details and HTTP status.
     *
     * @param exception the {@link ApiException} to handle
     * @return a {@link ResponseEntity} containing the serialized error details and HTTP status
     * @throws ApiException if an error occurs during error serialization
     */
    @ExceptionHandler(ApiException.class)
    public final ResponseEntity<String> handleApiException(final ApiException exception) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        log.debug("General error", exception);

        try {
            return ResponseEntity.status(exception.getStatus().value())
                    .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(exception.getError()));
        } catch (Exception e) {
            throw new ApiException(exception, ErrorType.INTERNAL_ERROR, exception.getError().getField());
        }
    }

    /**
     * Handles field validation errors by generating a JSON response with the error details.
     * <p>
     * This method creates an {@link ErrorDTO} object to encapsulate error information such as the
     * field name, error code, error message, and an optional invalid value. The response is serialized
     * into JSON format using Jackson's {@link ObjectMapper}.
     * </p>
     *
     * @param field the name of the field where the error occurred.
     * @param value the value of the field that caused the error, or {@code null} if the field is empty.
     * @return a {@link ResponseEntity} containing the error details in JSON format and an appropriate HTTP status code.
     * @throws ApiException if the error details cannot be serialized into JSON, wrapping
     * an {@link JsonProcessingException}.
     */
    public ResponseEntity<String> handleFieldError(final String field, final Object value) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        ErrorDTO error = new ErrorDTO();
        int status;

        error.setField(field);

        if (value == null) {
            status = ErrorType.EMPTY_VALUE.getStatus().value();
            error.setCode(ErrorType.EMPTY_VALUE.getCode());
            error.setMessage(ErrorType.EMPTY_VALUE.getMessage());
        } else {
            status = ErrorType.WRONG_VALUE.getStatus().value();
            error.setCode(ErrorType.WRONG_VALUE.getCode());
            error.setMessage(ErrorType.WRONG_VALUE.getMessage());
            error.setValue(value.toString());
        }

        try {
            return ResponseEntity.status(status)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(error));
        } catch (JsonProcessingException e) {
            throw new ApiException(e, ErrorType.INTERNAL_ERROR, null);
        }
    }

    /**
     * Handles {@link ConstraintViolationException} and sends a detailed error response.
     *
     * @param exception the {@link ConstraintViolationException} to handle
     * @return a {@link ResponseEntity} containing the serialized error details and HTTP status
     * @throws ApiException if an error occurs during error serialization
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<String> handleConstraintViolationException(
            final ConstraintViolationException exception) {
        Optional<ConstraintViolation<?>> opt = exception.getConstraintViolations().stream().findFirst();

        if (opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).build();
        }

        ConstraintViolation<?> violation = opt.get();

        return handleFieldError(getLastPath(violation.getPropertyPath()), violation.getInvalidValue());
    }

    /**
     * Handles {@link ConstraintViolationException} and sends a detailed error response.
     *
     * @param exception the {@link ConstraintViolationException} to handle
     * @return a {@link ResponseEntity} containing the serialized error details and HTTP status
     * @throws ApiException if an error occurs during error serialization
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<String> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException exception) {
        var error = exception.getFieldError();

        if (error == null) {
            throw new ApiException(exception, ErrorType.INTERNAL_ERROR, null);
        }

        return handleFieldError(error.getField(), error.getRejectedValue());
    }

    /**
     * Handles {@link HandlerMethodValidationException} and sends a detailed error response.
     *
     * @param exception the {@link HandlerMethodValidationException} to handle
     * @return a {@link ResponseEntity} containing the serialized error details and HTTP status
     * @throws ApiException if an error occurs during error serialization
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public final ResponseEntity<String> handleMethodValidationException(
            final HandlerMethodValidationException exception) {
        var violation = exception.getValueResults().getFirst();

        return handleFieldError(violation.getMethodParameter().getParameterName(), violation.getArgument());
    }

    /**
     * Retrieves the last node's name in the given Path.
     *  <p>
     *  This method iterates through the entire Path, returning the name of the last node in the sequence.
     *  </p>
     *  @param path The Path object to be processed.
     *  @return The name of the last node in the Path.
     */
    public String getLastPath(final Path path) {
        Iterator<Path.Node> iterator = path.iterator();
        Path.Node node = iterator.next();

        while (iterator.hasNext()) {
            node = iterator.next();
        }
        return node.getName();
    }

    /**
     * Handles {@link DataIntegrityViolationException} and sends a detailed error response.
     *
     * @param exception the {@link DataIntegrityViolationException} to handle
     * @return a {@link ResponseEntity} containing the serialized error details and HTTP status
     * @throws ApiException if an error occurs during error serialization
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<String> handleDataIntegrityViolationException(
            final DataIntegrityViolationException exception) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        int status  = ErrorType.WRONG_VALUE.getStatus().value();
        ErrorDTO error = new ErrorDTO();
        error.setCode(ErrorType.WRONG_VALUE.getCode());
        error.setMessage(ErrorType.WRONG_VALUE.getMessage());

        if (exception.contains(org.hibernate.exception.ConstraintViolationException.class)) {
            org.hibernate.exception.ConstraintViolationException violation =
                    (org.hibernate.exception.ConstraintViolationException) exception.getCause();
            error.setField("constraint");
            error.setValue(violation.getConstraintName());
            error.setCause(violation.getSQLException().getMessage());
        }

        try {
            return ResponseEntity
                    .status(status)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(error));
        } catch (JsonProcessingException e) {
            throw new ApiException(e, ErrorType.INTERNAL_ERROR, "constraint");
        }
    }

    /**
     * Handles {@link IllegalArgumentException} and sends a detailed error response.
     *
     * @param exception the {@link IllegalArgumentException} to handle
     * @return a {@link ResponseEntity} containing the serialized error details and HTTP status
     * @throws ApiException if an error occurs during error serialization
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(final IllegalArgumentException exception) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        ErrorDTO error = new ErrorDTO();
        error.setCode(ErrorType.WRONG_VALUE.getCode());
        error.setMessage(ErrorType.WRONG_VALUE.getMessage());
        error.setField("id");
        error.setCause(exception.getMessage());

        try {
            return ResponseEntity
                    .status(ErrorType.WRONG_VALUE.getStatus().value())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(error));
        } catch (JsonProcessingException e) {
            throw new ApiException(e, ErrorType.INTERNAL_ERROR, "id");
        }
    }
}
