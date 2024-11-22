package com.ditrit.letomodelizerapi.persistence.converter;

import com.ditrit.letomodelizerapi.model.permission.EntityPermission;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Tag("unit")
@DisplayName("Test class: EntityPermissionConverter")
class EntityPermissionConverterTest {

    @Test
    @DisplayName("Test convertToDatabaseColumn: should return null with null attribute")
    void testConvertToDatabaseColumnNull() {
        assertNull(new EntityPermissionConverter().convertToDatabaseColumn(null));
    }

    @Test
    @DisplayName("Test convertToDatabaseColumn: should return string of enum")
    void testConvertToDatabaseColumn() {
        assertEquals("COMPONENT", new EntityPermissionConverter().convertToDatabaseColumn(EntityPermission.COMPONENT));
        assertEquals("DIAGRAM", new EntityPermissionConverter().convertToDatabaseColumn(EntityPermission.DIAGRAM));
    }

    @Test
    @DisplayName("Test convertToEntityAttribute: should return null with null value")
    void testConvertToEntityAttributeNull() {
        assertNull(new EntityPermissionConverter().convertToEntityAttribute(null));
    }

    @Test
    @DisplayName("Test convertToEntityAttribute: should return wanted enum")
    void testConvertToEntityAttribute() {
        assertEquals(EntityPermission.COMPONENT, new EntityPermissionConverter().convertToEntityAttribute("COMPONENT"));
        assertEquals(EntityPermission.DIAGRAM, new EntityPermissionConverter().convertToEntityAttribute("DIAGRAM"));
    }
}
