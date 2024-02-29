package com.ditrit.letomodelizerapi.controller.handler;

import com.ditrit.letomodelizerapi.model.error.ErrorType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@DisplayName("Test class: IllegalArgumentExceptionHandler")
class IllegalArgumentExceptionHandlerTest {

    @Test
    @DisplayName("Should return valid response")
    void testToResponse() {
        IllegalArgumentException exception = new IllegalArgumentException();
        Response response = new IllegalArgumentExceptionHandler().toResponse(exception);

        assertNotNull(response);
        assertEquals(ErrorType.WRONG_VALUE.getStatus().value(), response.getStatus());
    }
}
