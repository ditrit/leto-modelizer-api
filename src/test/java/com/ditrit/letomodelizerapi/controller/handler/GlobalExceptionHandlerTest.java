package com.ditrit.letomodelizerapi.controller.handler;

import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorDTO;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@DisplayName("Test class: GlobalExceptionHandler")
class GlobalExceptionHandlerTest {

    @Test
    @DisplayName("Test handleApiException: should create wanted response.")
    void testHandleApiException() throws JsonProcessingException {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ApiException exception = new ApiException(ErrorType.WRONG_VALUE, null, null);

        ResponseEntity<String> response = handler.handleApiException(exception);
        assertNotNull(response);
        assertEquals(exception.getStatus(), response.getStatusCode());
        assertEquals(new ObjectMapper().writeValueAsString(exception.getError()), response.getBody());
    }

    private final Path defaultPath = () -> {
        Path.Node node1 = Mockito.mock(Path.Node.class);
        Path.Node node2 = Mockito.mock(Path.Node.class);

        Mockito.when(node1.getName()).thenReturn("test1");
        Mockito.when(node2.getName()).thenReturn("test2");

        return List.of(node1, node2).iterator();
    };

    @Test
    @DisplayName("Test handleConstraintViolationException: should return empty bad request response without ConstraintViolation.")
    void testHandleConstraintViolationExceptionWithoutConstraintViolation() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ConstraintViolationException exception = new ConstraintViolationException(Collections.emptySet());

        ResponseEntity<String> response = handler.handleConstraintViolationException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Test handleConstraintViolationException: should return empty bad request response with empty value.")
    void testHandleConstraintViolationShouldReturnBadRequestOnEmptyValue() throws JsonProcessingException {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ConstraintViolation violation = Mockito.mock(ConstraintViolation.class);
        Mockito.when(violation.getPropertyPath()).thenReturn(defaultPath);
        Set<ConstraintViolation<String>> set = new HashSet<>();
        set.add(violation);
        ConstraintViolationException exception = new ConstraintViolationException(set);
        ErrorDTO expectedError = new ErrorDTO(ErrorType.EMPTY_VALUE.getCode(), ErrorType.EMPTY_VALUE.getMessage(), "test2", null, null);

        ResponseEntity<String> response = handler.handleConstraintViolationException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(new ObjectMapper().writeValueAsString(expectedError), response.getBody());
    }

    @Test
    @DisplayName("Test handleConstraintViolationException: should return empty bad request")
    void voidHandleConstraintViolationShouldReturnBadRequest() throws JsonProcessingException {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ConstraintViolation violation = Mockito.mock(ConstraintViolation.class);
        Mockito.when(violation.getPropertyPath()).thenReturn(defaultPath);
        Mockito.when(violation.getInvalidValue()).thenReturn("test");
        Set<ConstraintViolation<String>> set = new HashSet<>();
        set.add(violation);
        ConstraintViolationException exception = new ConstraintViolationException(set);
        ErrorDTO expectedError = new ErrorDTO(ErrorType.WRONG_VALUE.getCode(), ErrorType.WRONG_VALUE.getMessage(), "test2", "test", null);

        ResponseEntity<String> response = handler.handleConstraintViolationException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(new ObjectMapper().writeValueAsString(expectedError), response.getBody());
    }

    @Test
    @DisplayName("Test handleDataIntegrityViolationException: should return empty bad request response with unknown violation.")
    void testHandleDataIntegrityViolationExceptionWithUnknown() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ResponseEntity<String> response = handler.handleDataIntegrityViolationException(new DataIntegrityViolationException("test"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Test handleDataIntegrityViolationException: should return bad request response")
    void testHandleDataIntegrityViolationException() throws JsonProcessingException {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        SQLException sqlException = new SQLException();
        org.hibernate.exception.ConstraintViolationException violation = new org.hibernate.exception.ConstraintViolationException("test sql", sqlException, "data");
        DataIntegrityViolationException exception = new DataIntegrityViolationException("test", violation);
        ErrorDTO expectedError = new ErrorDTO(ErrorType.WRONG_VALUE.getCode(), ErrorType.WRONG_VALUE.getMessage(), "constraint", "data",  null);

        ResponseEntity<String> response = handler.handleDataIntegrityViolationException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(new ObjectMapper().writeValueAsString(expectedError), response.getBody());
    }

    @Test
    @DisplayName("Test handleIllegalArgumentException: should return valid response")
    void testHandleIllegalArgumentException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ResponseEntity<String> response = handler.handleIllegalArgumentException(new IllegalArgumentException("test"));

        assertNotNull(response);
        assertEquals(ErrorType.WRONG_VALUE.getStatus(), response.getStatusCode());
    }

    @Test
    @DisplayName("Test handleMethodArgumentNotValidException: should return valid response")
    void testHandleMethodArgumentNotValidException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        FieldError fieldError = Mockito.mock(FieldError.class);
        Mockito.when(fieldError.getField()).thenReturn("field");
        Mockito.when(fieldError.getRejectedValue()).thenReturn("value");
        MethodArgumentNotValidException error = Mockito.mock(MethodArgumentNotValidException.class);
        Mockito.when(error.getFieldError()).thenReturn(fieldError);

        ResponseEntity<String> response = handler.handleMethodArgumentNotValidException(error);

        assertNotNull(response);
        assertEquals(ErrorType.WRONG_VALUE.getStatus(), response.getStatusCode());
    }

    @Test
    @DisplayName("Test handleMethodArgumentNotValidException: should throw internal error")
    void testHandleMethodArgumentNotValidExceptionInternalError() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        ApiException exception = null;

        MethodArgumentNotValidException error = Mockito.mock(MethodArgumentNotValidException.class);
        Mockito.when(error.getFieldError()).thenReturn(null);

        try {
            handler.handleMethodArgumentNotValidException(error);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.INTERNAL_ERROR.getStatus(), exception.getStatus());
    }

    @Test
    @DisplayName("Test handleMethodValidationException: should return valid response")
    void testHandleMethodValidationException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        MethodParameter methodParameter = Mockito.mock(MethodParameter.class);
        Mockito.when(methodParameter.getParameterName()).thenReturn("method");
        ParameterValidationResult violation = Mockito.mock(ParameterValidationResult.class);
        Mockito.when(violation.getMethodParameter()).thenReturn(methodParameter);
        Mockito.when(violation.getArgument()).thenReturn("argument");

        HandlerMethodValidationException error = Mockito.mock(HandlerMethodValidationException.class);
        Mockito.when(error.getValueResults()).thenReturn(List.of(violation));

        ResponseEntity<String> response = handler.handleMethodValidationException(error);

        assertNotNull(response);
        assertEquals(ErrorType.WRONG_VALUE.getStatus(), response.getStatusCode());
    }
}
