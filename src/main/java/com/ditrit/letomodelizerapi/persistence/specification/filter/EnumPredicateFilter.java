package com.ditrit.letomodelizerapi.persistence.specification.filter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

/**
 * Class to extract enum filter from the query.
 */
public class EnumPredicateFilter extends PredicateFilter {

    /**
     * Create enum filter with field name and default type filter as "enum".
     *
     * @param name  Field name.
     * @param value Field value.
     */
    public EnumPredicateFilter(final String name, final String value) {
        super(name, value, FilterType.Type.ENUM);
    }

    @Override
    public final <T> Predicate getPredicate(final int index, final CriteriaBuilder builder, final Expression<T> field) {
        if (this.getIsNotOperator(index)) {
            return builder.notEqual(field.as(String.class), this.getValue(index));
        }
        return builder.equal(field.as(String.class), this.getValue(index));
    }
}
