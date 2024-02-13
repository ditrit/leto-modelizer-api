package com.ditrit.letomodelizerapi.persistence.converter;

import com.ditrit.letomodelizerapi.model.library.LibraryTemplateType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Tag("unit")
@DisplayName("Test class: LibraryTemplateTypeConverter")
class LibraryTemplateTypeConverterTest {

    @Test
    @DisplayName("Test convertToDatabaseColumn: should return null with null attribute")
    void testConvertToDatabaseColumnNull() {
        assertNull(new LibraryTemplateTypeConverter().convertToDatabaseColumn(null));
    }

    @Test
    @DisplayName("Test convertToDatabaseColumn: should return string of enum")
    void testConvertToDatabaseColumn() {
        assertEquals("COMPONENT", new LibraryTemplateTypeConverter().convertToDatabaseColumn(LibraryTemplateType.COMPONENT));
        assertEquals("PROJECT", new LibraryTemplateTypeConverter().convertToDatabaseColumn(LibraryTemplateType.PROJECT));
    }

    @Test
    @DisplayName("Test convertToEntityAttribute: should return null with null value")
    void testConvertToEntityAttributeNull() {
        assertNull(new LibraryTemplateTypeConverter().convertToEntityAttribute(null));
    }

    @Test
    @DisplayName("Test convertToEntityAttribute: should return wanted enum")
    void testConvertToEntityAttribute() {
        assertEquals(LibraryTemplateType.COMPONENT, new LibraryTemplateTypeConverter().convertToEntityAttribute("COMPONENT"));
        assertEquals(LibraryTemplateType.PROJECT, new LibraryTemplateTypeConverter().convertToEntityAttribute("PROJECT"));
    }
}
