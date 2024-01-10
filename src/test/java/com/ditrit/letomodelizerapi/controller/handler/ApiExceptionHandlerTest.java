package com.ditrit.letomodelizerapi.controller.handler;

import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@DisplayName("Test class: ApiExceptionHandler")
class ApiExceptionHandlerTest {
    @Test
    @DisplayName("Test toResponse: should create wanted response.")
    void testToResponse() throws JsonProcessingException {
        ApiExceptionHandler handler = new ApiExceptionHandler();
        ApiException exception = new ApiException(ErrorType.WRONG_VALUE, null, null);

        Response response = handler.toResponse(exception);
        assertNotNull(response);
        assertEquals(exception.getStatus().value(), response.getStatus());
        assertEquals(new ObjectMapper().writeValueAsString(exception.getError()), response.getEntity());
    }
}
