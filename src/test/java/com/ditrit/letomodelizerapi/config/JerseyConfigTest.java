package com.ditrit.letomodelizerapi.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

@Tag("unit")
@DisplayName("Test class: JerseyConfig")
class JerseyConfigTest {
    @Test
    @DisplayName("Test constructor: should not throw exception")
    void testConstructorShouldNotThrowException() {
        Exception exception = null;

        try {
            new JerseyConfig();
        } catch (Exception e) {
            exception = e;
        }

        assertNull(exception);
    }
}
