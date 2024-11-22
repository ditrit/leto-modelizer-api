package com.ditrit.letomodelizerapi.persistence.specification;

import com.ditrit.letomodelizerapi.model.permission.ActionPermission;
import io.github.zorin95670.exception.SpringQueryFilterException;
import io.github.zorin95670.predicate.PredicateFilter;

public class ActionPermissionPredicateFilter<T> extends PredicateFilter<T, ActionPermission> {

    /**
     * Constructs a new {@link ActionPermissionPredicateFilter} with the specified name and filter value.
     *
     * @param name The name of the field to filter by.
     * @param value The filter value(s) to apply.
     */
    public ActionPermissionPredicateFilter(final String name, final String value) {
        super(name, value);
    }

    @Override
    public ActionPermission parseValue(final String value) {
        try {
            return ActionPermission.valueOf(value);
        } catch (IllegalArgumentException exception) {
            throw new SpringQueryFilterException(
                "Invalid ActionPermission format: Unable to parse the value '" + value + "' as an ActionPermission.",
                exception,
                "ActionPermission",
                this.getName(),
                value
            );
        }
    }
}
