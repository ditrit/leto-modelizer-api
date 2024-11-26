package com.ditrit.letomodelizerapi.persistence.specification;

import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import com.ditrit.letomodelizerapi.model.permission.ActionPermission;
import com.ditrit.letomodelizerapi.model.permission.EntityPermission;
import io.github.zorin95670.predicate.IPredicateFilter;
import io.github.zorin95670.specification.SpringQueryFilterSpecification;

import java.util.List;
import java.util.Map;

/**
 * Custom class to manage our type to generate predicate.
 * @param <T> Entity class.
 */
public class CustomSpringQueryFilterSpecification<T> extends SpringQueryFilterSpecification<T> {

    /**
     * Constructs a new instance of {@code CustomSpringQueryFilterSpecification}.
     *
     * @param entityClass the class of the entity being filtered.
     * @param filters     a map of query parameters to be used for filtering, where the key
     *                    is the field name and the value is a list of filtering criteria.
     */
    public CustomSpringQueryFilterSpecification(final Class<T> entityClass, final Map<String, List<String>> filters) {
        super(entityClass, filters);
    }

    @Override
    public IPredicateFilter<T, ?> getPredicateFilter(final Class<?> type, final String name, final String value) {
        if (AccessControlType.class.equals(type)) {
            return new AccessControlTypePredicateFilter<>(name, value);
        }

        if (EntityPermission.class.equals(type)) {
            return new EntityPermissionPredicateFilter<>(name, value);
        }

        if (ActionPermission.class.equals(type)) {
            return new ActionPermissionPredicateFilter<>(name, value);
        }

        return super.getPredicateFilter(type, name, value);
    }
}
