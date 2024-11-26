package com.ditrit.letomodelizerapi.persistence.converter;

import com.ditrit.letomodelizerapi.model.permission.ActionPermission;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Tag("unit")
@DisplayName("Test class: ActionPermissionConverter")
class ActionPermissionConverterTest {

    @Test
    @DisplayName("Test convertToDatabaseColumn: should return null with null attribute")
    void testConvertToDatabaseColumnNull() {
        assertNull(new ActionPermissionConverter().convertToDatabaseColumn(null));
    }

    @Test
    @DisplayName("Test convertToDatabaseColumn: should return string of enum")
    void testConvertToDatabaseColumn() {
        assertEquals("ACCESS", new ActionPermissionConverter().convertToDatabaseColumn(ActionPermission.ACCESS));
        assertEquals("CREATE", new ActionPermissionConverter().convertToDatabaseColumn(ActionPermission.CREATE));
    }

    @Test
    @DisplayName("Test convertToEntityAttribute: should return null with null value")
    void testConvertToEntityAttributeNull() {
        assertNull(new ActionPermissionConverter().convertToEntityAttribute(null));
    }

    @Test
    @DisplayName("Test convertToEntityAttribute: should return wanted enum")
    void testConvertToEntityAttribute() {
        assertEquals(ActionPermission.ACCESS, new ActionPermissionConverter().convertToEntityAttribute("ACCESS"));
        assertEquals(ActionPermission.CREATE, new ActionPermissionConverter().convertToEntityAttribute("CREATE"));
    }
}
