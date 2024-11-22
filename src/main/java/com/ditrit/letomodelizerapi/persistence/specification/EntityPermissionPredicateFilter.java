package com.ditrit.letomodelizerapi.persistence.specification;

import com.ditrit.letomodelizerapi.model.permission.EntityPermission;
import io.github.zorin95670.exception.SpringQueryFilterException;
import io.github.zorin95670.predicate.PredicateFilter;

public class EntityPermissionPredicateFilter<T> extends PredicateFilter<T, EntityPermission> {

    /**
     * Constructs a new {@link EntityPermissionPredicateFilter} with the specified name and filter value.
     *
     * @param name The name of the field to filter by.
     * @param value The filter value(s) to apply.
     */
    public EntityPermissionPredicateFilter(final String name, final String value) {
        super(name, value);
    }

    @Override
    public EntityPermission parseValue(final String value) {
        try {
            return EntityPermission.valueOf(value);
        } catch (IllegalArgumentException exception) {
            throw new SpringQueryFilterException(
                "Invalid EntityPermission format: Unable to parse the value '" + value + "' as an EntityPermission.",
                exception,
                "EntityPermission",
                this.getName(),
                value
            );
        }
    }
}
