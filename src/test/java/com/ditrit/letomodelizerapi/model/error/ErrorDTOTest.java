package com.ditrit.letomodelizerapi.model.error;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@DisplayName("Test class: ErrorDTO")
class ErrorDTOTest {

    @Test
    @DisplayName("Test constructor: should set default properties values.")
    void testDefaultConstructor() {
        ErrorDTO dto = new ErrorDTO();

        assertNull(dto.getMessage());
        assertNull(dto.getField());
        assertNull(dto.getValue());
        assertNull(dto.getCause());
        assertEquals(-1, dto.getCode());
    }

    @Test
    @DisplayName("Test constructor: should set all properties values.")
    void testConstructor() {
        ErrorDTO dto = new ErrorDTO(1, "message", "field", "value", new Exception("cause"));

        assertEquals("message", dto.getMessage());
        assertEquals("field", dto.getField());
        assertEquals("value", dto.getValue());
        assertEquals("cause", dto.getCause());
        assertEquals(1, dto.getCode());
    }

    @Test
    @DisplayName("Test setThrowable: should set null without throwable.")
    void testSetThrowable() {
        ErrorDTO dto = new ErrorDTO();
        assertNull(dto.getCause());

        dto.setThrowable(null);
        assertNull(dto.getCause());
    }

    @Test
    @DisplayName("Test setThrowable: should set null with throwable with null message.")
    void testSetThrowableNullMessage() {
        ErrorDTO dto = new ErrorDTO();
        assertNull(dto.getCause());

        dto.setThrowable(new Exception());
        assertNull(dto.getCause());
    }

    @Test
    @DisplayName("Test setThrowable: should set related message with throwable with not null message.")
    void testSetThrowableNotNullMessage() {
        ErrorDTO dto = new ErrorDTO();
        assertNull(dto.getCause());

        dto.setThrowable(new Exception("cause"));
        assertEquals("cause", dto.getCause());
    }
}
