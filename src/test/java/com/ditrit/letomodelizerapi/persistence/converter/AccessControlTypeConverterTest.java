package com.ditrit.letomodelizerapi.persistence.converter;

import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@DisplayName("Test class: AccessControlTypeConverter")
class AccessControlTypeConverterTest {

    @Test
    @DisplayName("Test convertToDatabaseColumn: should return null with null attribute")
    void testConvertToDatabaseColumnNull() {
        assertNull(new AccessControlTypeConverter().convertToDatabaseColumn(null));
    }

    @Test
    @DisplayName("Test convertToDatabaseColumn: should return string of enum")
    void testConvertToDatabaseColumn() {
        assertEquals("ROLE", new AccessControlTypeConverter().convertToDatabaseColumn(AccessControlType.ROLE));
        assertEquals("GROUP", new AccessControlTypeConverter().convertToDatabaseColumn(AccessControlType.GROUP));
    }

    @Test
    @DisplayName("Test convertToEntityAttribute: should return null with null value")
    void testConvertToEntityAttributeNull() {
        assertNull(new AccessControlTypeConverter().convertToEntityAttribute(null));
    }

    @Test
    @DisplayName("Test convertToEntityAttribute: should return wanted enum")
    void testConvertToEntityAttribute() {
        assertEquals(AccessControlType.ROLE, new AccessControlTypeConverter().convertToEntityAttribute("ROLE"));
        assertEquals(AccessControlType.GROUP, new AccessControlTypeConverter().convertToEntityAttribute("GROUP"));
    }
}
