package com.ditrit.letomodelizerapi.persistence.converter;

import com.ditrit.letomodelizerapi.model.library.LibraryTemplateType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converter class for LibraryTemplateType entity. This class implements the AttributeConverter interface,
 * providing methods to convert LibraryTemplateType objects to their database column representation and vice versa.
 * It is marked with @Converter to be automatically applied to entity attribute conversions.
 */
@Converter(autoApply = true)
public class LibraryTemplateTypeConverter implements AttributeConverter<LibraryTemplateType, String> {

    @Override
    public String convertToDatabaseColumn(final LibraryTemplateType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public LibraryTemplateType convertToEntityAttribute(final String dbData) {
        if (dbData == null) {
            return null;
        }
        return LibraryTemplateType.valueOf(dbData);
    }
}
