package com.ditrit.letomodelizerapi.persistence.converter;

import com.ditrit.letomodelizerapi.model.permission.EntityPermission;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Converter class for EntityPermission entity. This class implements the AttributeConverter interface,
 * providing methods to convert EntityPermission objects to their database column representation and vice versa.
 * It is marked with @Converter to be automatically applied to entity attribute conversions.
 */
@Converter(autoApply = true)
public class EntityPermissionConverter implements AttributeConverter<EntityPermission, String> {

    @Override
    public String convertToDatabaseColumn(final EntityPermission attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public EntityPermission convertToEntityAttribute(final String dbData) {
        if (dbData == null) {
            return null;
        }
        return EntityPermission.valueOf(dbData);
    }
}
