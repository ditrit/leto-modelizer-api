package com.ditrit.letomodelizerapi.persistence.specification;

import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import io.github.zorin95670.exception.SpringQueryFilterException;
import io.github.zorin95670.predicate.PredicateFilter;

public class AccessControlTypePredicateFilter<T> extends PredicateFilter<T, AccessControlType> {

    /**
     * Constructs a new {@link AccessControlTypePredicateFilter} with the specified name and filter value.
     *
     * @param name The name of the field to filter by.
     * @param value The filter value(s) to apply.
     */
    public AccessControlTypePredicateFilter(final String name, final String value) {
        super(name, value);
    }

    @Override
    public AccessControlType parseValue(final String value) {
        try {
            return AccessControlType.valueOf(value);
        } catch (IllegalArgumentException exception) {
            throw new SpringQueryFilterException(
                "Invalid AccessControlType format: Unable to parse the value '" + value + "' as an AccessControlType.",
                exception,
                "AccessControlType",
                this.getName(),
                value
            );
        }
    }
}
