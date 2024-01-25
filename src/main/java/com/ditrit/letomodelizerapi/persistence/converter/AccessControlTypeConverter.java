package com.ditrit.letomodelizerapi.persistence.converter;

import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converter class for AccessControlType entity. This class implements the AttributeConverter interface,
 * providing methods to convert AccessControlType objects to their database column representation and vice versa.
 * It is marked with @Converter to be automatically applied to entity attribute conversions.
 */
@Converter(autoApply = true)
public class AccessControlTypeConverter implements AttributeConverter<AccessControlType, String> {

    @Override
    public String convertToDatabaseColumn(final AccessControlType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public AccessControlType convertToEntityAttribute(final String dbData) {
        if (dbData == null) {
            return null;
        }
        return AccessControlType.valueOf(dbData);
    }
}
