package com.ditrit.letomodelizerapi.persistence.converter;

import com.ditrit.letomodelizerapi.model.permission.ActionPermission;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converter class for ActionPermission entity. This class implements the AttributeConverter interface,
 * providing methods to convert ActionPermission objects to their database column representation and vice versa.
 * It is marked with @Converter to be automatically applied to entity attribute conversions.
 */
@Converter(autoApply = true)
public class ActionPermissionConverter implements AttributeConverter<ActionPermission, String> {

    @Override
    public String convertToDatabaseColumn(final ActionPermission attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public ActionPermission convertToEntityAttribute(final String dbData) {
        if (dbData == null) {
            return null;
        }
        return ActionPermission.valueOf(dbData);
    }
}
