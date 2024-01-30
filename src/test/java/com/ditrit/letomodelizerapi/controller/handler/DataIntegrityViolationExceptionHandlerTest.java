package com.ditrit.letomodelizerapi.controller.handler;

import com.ditrit.letomodelizerapi.model.error.ErrorDTO;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
@DisplayName("Test class: DataIntegrityViolationExceptionHandler")
class DataIntegrityViolationExceptionHandlerTest {

    @Test
    @DisplayName("Test toResponse: should return empty bad request response with unknown violation.")
    void testToResponseWithUnknownViolation() {
        DataIntegrityViolationExceptionHandler handler = new DataIntegrityViolationExceptionHandler();

        Response response = handler.toResponse(new DataIntegrityViolationException("test"));
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
    }

    @Test
    @DisplayName("Test toResponse: should return bad request response")
    void testToResponse() throws JsonProcessingException {
        DataIntegrityViolationExceptionHandler handler = new DataIntegrityViolationExceptionHandler();
        SQLException sqlException = new SQLException();
        ConstraintViolationException violation = new ConstraintViolationException("test sql", sqlException, "data");
        DataIntegrityViolationException exception = new DataIntegrityViolationException("test", violation);
        ErrorDTO expectedError = new ErrorDTO(ErrorType.WRONG_VALUE.getCode(), ErrorType.WRONG_VALUE.getMessage(), "constraint", "data",  null);

        Response response = handler.toResponse(exception);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals(new ObjectMapper().writeValueAsString(expectedError), response.getEntity());
    }
}
