package com.ditrit.letomodelizerapi.model.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@DisplayName("Test class: ApiException")
class ApiExceptionTest {

    @Test
    @DisplayName("Test constructor: should set only ErrorType and Field properties, default for others.")
    void testConstructorErrorTypeField() {
        ErrorType error = ErrorType.INTERNAL_ERROR;
        ApiException exception = new ApiException(error, "field");

        assertEquals(exception.getMessage(), error.getMessage());
        assertEquals(exception.getStatus(), error.getStatus());
        assertEquals(exception.getError(), new ErrorDTO(error.getCode(), error.getMessage(), "field", null, null));
        assertTrue(exception.isLogIt());
    }

    @Test
    @DisplayName("Test constructor: should set only ErrorType, Field and Value properties, default for others.")
    void testConstructorErrorTypeFieldValue() {
        ErrorType error = ErrorType.INTERNAL_ERROR;
        ApiException exception = new ApiException(error, "field", "value");

        assertEquals(exception.getMessage(), error.getMessage());
        assertEquals(exception.getStatus(), error.getStatus());
        assertEquals(exception.getError(), new ErrorDTO(error.getCode(), error.getMessage(), "field", "value", null));
        assertTrue(exception.isLogIt());
    }

    @Test
    @DisplayName("Test constructor: should set only Exception, ErrorType and Field properties, default for others.")
    void testConstructorExceptionErrorTypeField() {
        ErrorType error = ErrorType.INTERNAL_ERROR;
        Exception cause = new Exception("cause");
        ApiException exception = new ApiException(cause, error, "field");

        assertEquals(exception.getMessage(), error.getMessage());
        assertEquals(exception.getStatus(), error.getStatus());
        assertEquals(exception.getError(), new ErrorDTO(error.getCode(), error.getMessage(), "field", null, cause));
        assertTrue(exception.isLogIt());
    }

    @Test
    @DisplayName("Test constructor: should set only Exception, ErrorType, Field and Value properties, default for others.")
    void testConstructorExceptionErrorTypeFieldValue() {
        ErrorType error = ErrorType.INTERNAL_ERROR;
        Exception cause = new Exception("cause");
        ApiException exception = new ApiException(cause, error, "field", "value");

        assertEquals(exception.getMessage(), error.getMessage());
        assertEquals(exception.getStatus(), error.getStatus());
        assertEquals(exception.getError(), new ErrorDTO(error.getCode(), error.getMessage(), "field", "value", cause));
        assertTrue(exception.isLogIt());
    }

    @Test
    @DisplayName("Test constructor: should set only Exception, ErrorType, Field Value and NeedToBeLogged properties, default for others.")
    void testConstructorExceptionErrorTypeFieldValueLogged() {
        ErrorType error = ErrorType.INTERNAL_ERROR;
        Exception cause = new Exception("cause");
        ApiException exception = new ApiException(cause, error, "field", "value", false);

        assertEquals(exception.getMessage(), error.getMessage());
        assertEquals(exception.getStatus(), error.getStatus());
        assertEquals(exception.getError(), new ErrorDTO(error.getCode(), error.getMessage(), "field", "value", cause));
        assertFalse(exception.isLogIt());
    }
}
