package com.ditrit.letomodelizerapi.persistence.specification;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * List of available operator for QueryFilter.
 */
public enum PredicateOperator {
    /**
     * Operator equals, value "=".
     */
    EQUALS("eq"),
    /**
     * Operator inferior, value "<".
     */
    INFERIOR("lt"),
    /**
     * Operator superior, value ">".
     */
    SUPERIOR("gt"),
    /**
     * Operator between, value "<>".
     */
    BETWEEN("bt"),
    /**
     * Operator null, value "null".
     */
    NULL("null"),
    /**
     * Operator like, value "lk".
     */
    LIKE("lk_");

    /**
     * String representation of operator.
     */
    @Getter
    private final String value;

    /**
     * Default constructor.
     *
     * @param value Representation of operator
     */
    PredicateOperator(final String value) {
        this.value = value;
    }

    /**
     * Indicates if operator is valid or not.
     *
     * @param operator Operator value.
     * @return If operator exists return true otherwise false.
     */
    public static boolean isValid(final String operator) {
        return Arrays.stream(PredicateOperator.values()).anyMatch(o -> o.getValue().equalsIgnoreCase(operator));
    }

    /**
     * Get operator from value.
     *
     * @param operator Operator value.
     * @return Operator or null.
     */
    public static Optional<PredicateOperator> get(final String operator) {
        return Arrays.stream(PredicateOperator.values())
                .filter(predicateOperator -> predicateOperator.getValue().equalsIgnoreCase(operator))
                .findAny();
    }
}
