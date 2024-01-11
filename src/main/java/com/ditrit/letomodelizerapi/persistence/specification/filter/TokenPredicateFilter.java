package com.ditrit.letomodelizerapi.persistence.specification.filter;

/**
 * Class to extract token filter from the query.
 */
public class TokenPredicateFilter extends PredicateFilter {

    /**
     * Create token filter with field name and default type filter as "token".
     *
     * @param name  Field name.
     * @param value Field value.
     */
    public TokenPredicateFilter(final String name, final String value) {
        super(name, value, FilterType.Type.TOKEN);
    }

}
