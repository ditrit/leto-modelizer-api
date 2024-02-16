package com.ditrit.letomodelizerapi.persistence.converter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
@DisplayName("Test class: StringListConverter")
class StringListConverterTest {

    @Test
    @DisplayName("Test convertToDatabaseColumn: should return null with null attribute")
    void testConvertToDatabaseColumnNull() {
        assertEquals("[]", new StringListConverter().convertToDatabaseColumn(null));
    }

    @Test
    @DisplayName("Test convertToDatabaseColumn: should return string of enum")
    void testConvertToDatabaseColumn() {
        assertEquals("[\"test\"]", new StringListConverter().convertToDatabaseColumn(List.of("test")));
        assertEquals("[\"test1\",\"test2\"]", new StringListConverter().convertToDatabaseColumn(List.of("test1", "test2")));
    }

    @Test
    @DisplayName("Test convertToEntityAttribute: should return null with null value")
    void testConvertToEntityAttributeNull() {
        assertEquals(List.of(), new StringListConverter().convertToEntityAttribute(null));
    }

    @Test
    @DisplayName("Test convertToEntityAttribute: should return wanted enum")
    void testConvertToEntityAttribute() {
        assertEquals(List.of("test"), new StringListConverter().convertToEntityAttribute("[\"test\"]"));
        assertEquals(List.of("test1", "test2"), new StringListConverter().convertToEntityAttribute("[\"test1\", \"test2\"]"));
    }
}
