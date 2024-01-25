package com.ditrit.letomodelizerapi.persistence.specification.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that turns a field into a filterable field.
 * The type of filter must be adapted to the value's type of the field.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FilterType {

    /**
     * Type of filter.
     *
     * @return Type of filter.
     */
    Type type();

    /**
     * Type of filter.
     */
    enum Type {
        /**
         * Text.
         */
        TEXT,
        /**
         * Date.
         */
        DATE,
        /**
         * Number.
         */
        NUMBER,
        /**
         * Boolean.
         */
        BOOLEAN,
        /**
         * Token.
         */
        TOKEN,
        /**
         * Enum.
         */
        ENUM,
    }
}
