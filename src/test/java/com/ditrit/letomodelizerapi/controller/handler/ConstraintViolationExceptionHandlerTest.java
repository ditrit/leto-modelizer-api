package com.ditrit.letomodelizerapi.controller.handler;

import com.ditrit.letomodelizerapi.model.error.ErrorDTO;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
@DisplayName("Test class: ConstraintViolationExceptionHandler")
class ConstraintViolationExceptionHandlerTest {

    private final Path defaultPath = () -> {
        Path.Node node1 = Mockito.mock(Path.Node.class);
        Path.Node node2 = Mockito.mock(Path.Node.class);

        Mockito.when(node1.getName()).thenReturn("test1");
        Mockito.when(node2.getName()).thenReturn("test2");

        return List.of(node1, node2).iterator();
    };

    @Test
    @DisplayName("Test toResponse: should return empty bad request response without ConstraintViolation.")
    void testToResponseWithoutConstraintViolation() {
        ConstraintViolationExceptionHandler handler = new ConstraintViolationExceptionHandler();
        ConstraintViolationException exception = new ConstraintViolationException(Collections.emptySet());

        Response response = handler.toResponse(exception);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    void toResponseShouldReturnBadRequestOnEmptyValue() throws JsonProcessingException {
        ConstraintViolationExceptionHandler handler = new ConstraintViolationExceptionHandler();
        ConstraintViolation violation = Mockito.mock(ConstraintViolation.class);
        Mockito.when(violation.getPropertyPath()).thenReturn(defaultPath);
        Set<ConstraintViolation<String>> set = new HashSet<>();
        set.add(violation);
        ConstraintViolationException exception = new ConstraintViolationException(set);
        ErrorDTO expectedError = new ErrorDTO(ErrorType.EMPTY_VALUE.getCode(), ErrorType.EMPTY_VALUE.getMessage(), "test2", null, null);

        Response response = handler.toResponse(exception);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(new ObjectMapper().writeValueAsString(expectedError), response.getEntity());
    }

    @Test
    void toResponseShouldReturnBadRequest() throws JsonProcessingException {
        ConstraintViolationExceptionHandler handler = new ConstraintViolationExceptionHandler();
        ConstraintViolation violation = Mockito.mock(ConstraintViolation.class);
        Mockito.when(violation.getPropertyPath()).thenReturn(defaultPath);
        Mockito.when(violation.getInvalidValue()).thenReturn("test");
        Set<ConstraintViolation<String>> set = new HashSet<>();
        set.add(violation);
        ConstraintViolationException exception = new ConstraintViolationException(set);
        ErrorDTO expectedError = new ErrorDTO(ErrorType.WRONG_VALUE.getCode(), ErrorType.WRONG_VALUE.getMessage(), "test2", "test", null);

        Response response = handler.toResponse(exception);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(new ObjectMapper().writeValueAsString(expectedError), response.getEntity());
    }
}
