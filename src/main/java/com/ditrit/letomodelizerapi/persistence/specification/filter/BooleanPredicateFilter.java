package com.ditrit.letomodelizerapi.persistence.specification.filter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

/**
 * Class to extract boolean filter from the query.
 */
public class BooleanPredicateFilter extends PredicateFilter {

    /**
     * Create boolean filter with field name and default type is "boolean".
     *
     * @param name  Field name.
     * @param value Field value.
     */
    public BooleanPredicateFilter(final String name, final String value) {
        super(name, value, FilterType.Type.BOOLEAN);
    }

    @Override
    public final <T> Predicate getPredicate(final int index, final CriteriaBuilder builder, final Expression<T> field) {
        if (this.getIsNotOperator(index)) {
            return builder.notEqual(field, Boolean.parseBoolean(this.getValue(index)));
        }
        return builder.equal(field, Boolean.parseBoolean(this.getValue(index)));
    }

}
