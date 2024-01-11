package com.ditrit.letomodelizerapi.persistence.specification.filter;

import com.ditrit.letomodelizerapi.persistence.specification.PredicateOperator;
import jakarta.persistence.criteria.CommonAbstractCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

/**
 * Abstract class for implementing the default field of QueryFilter.
 */
public abstract class PredicateFilter implements IPredicateFilter {

    /**
     * Or delimiter.
     */
    protected static final String OR_DELIMITER = "\\|";

    /**
     * Not delimiter.
     */
    protected static final String NOT_DELIMITER = "not_";
    /**
     * Operator of this filter.
     */
    private PredicateOperator[] operators;
    /**
     * Indicate if it's not operator.
     */
    private boolean[] isNotOperators;
    /**
     * Name of field to apply filter.
     */
    @Getter
    @Setter
    private String name;
    /**
     * Values of filter.
     */
    private String[] values;

    /**
     * Create filter with field name.
     *
     * @param name  Field's name to apply filter.
     * @param value Text to convert in filters.
     * @param type  Type of filter.
     */
    PredicateFilter(final String name, final String value, final FilterType.Type type) {
        this.setName(name);
        this.setValues(value);
        this.setOperators(new PredicateOperator[this.getValues().length]);
        this.setIsNotOperators(new boolean[this.getValues().length]);
    }

    /**
     * Get the state of the 'isNotOperator' at position 'index'.
     *
     * @param index Value index.
     * @return State.
     */
    public boolean getIsNotOperator(final int index) {
        return this.isNotOperators[index];
    }

    /**
     * Set the state of the 'isNotOperator' at position 'index'.
     *
     * @param index Index of value to set.
     * @param state State.
     */
    public void setIsNotOperator(final int index, final boolean state) {
        this.isNotOperators[index] = state;
    }

    /**
     * Set is not operator states.
     *
     * @param states States list.
     */
    public final void setIsNotOperators(final boolean[] states) {
        this.isNotOperators = Arrays.copyOf(states, states.length);
    }

    /**
     * Get value of filter from index.
     *
     * @param index Value index.
     * @return Value.
     */
    public String getValue(final int index) {
        return this.values[index];
    }

    /**
     * Set value on index.
     *
     * @param index Index of value to set.
     * @param value Value.
     */
    public void setValue(final int index, final String value) {
        this.values[index] = value;
    }

    /**
     * Get values of filter.
     *
     * @return Values.
     */
    public String[] getValues() {
        return Arrays.copyOf(this.values, this.values.length);
    }

    /**
     * Set values of filter.
     *
     * @param value Not split value.
     */
    public void setValues(final String value) {
        if (value != null) {
            this.values = value.split(OR_DELIMITER);
        } else {
            this.values = new String[0];
        }
    }

    /**
     * Get operator.
     *
     * @param index Operator index.
     * @return Operator.
     */
    public final PredicateOperator getOperator(final int index) {
        return operators[index];
    }

    /**
     * Set operator on index.
     *
     * @param index    Operator index.
     * @param operator Operator to set.
     */
    public final void setOperator(final int index, final PredicateOperator operator) {
        this.operators[index] = operator;
    }

    /**
     * Get operators.
     *
     * @return Operator.
     */
    public final PredicateOperator[] getOperators() {
        return Arrays.copyOf(this.operators, this.operators.length);
    }

    /**
     * Set operators.
     *
     * @param operators Operator list.
     */
    public final void setOperators(final PredicateOperator[] operators) {
        this.operators = Arrays.copyOf(operators, operators.length);
    }

    /**
     * Set operator from value.
     *
     * @param index Value index.
     */
    public void setOperatorFromValue(final int index) {
        this.setOperator(index, PredicateOperator.EQUALS);
        if (this.values[index].toLowerCase().startsWith(NOT_DELIMITER)) {
            this.setIsNotOperator(index, true);
            this.setValue(index, this.values[index].substring(NOT_DELIMITER.length()));
        }
        if (PredicateOperator.NULL.getValue().equalsIgnoreCase(this.values[index])) {
            this.setOperator(index, PredicateOperator.NULL);
        }
    }

    /**
     * If field value is null return false, otherwise set field value in value
     * and return true.
     */
    @Override
    public boolean extract() {
        if (this.getValues().length == 0) {
            return false;
        }

        for (int index = 0; index < this.getValues().length; index++) {
            this.setOperatorFromValue(index);
        }

        return true;
    }

    /**
     * Override this if you need to add predicate on sub-query.
     */
    @Override
    public <T, Y> Predicate getPredicate(final CriteriaBuilder builder, final From<T, Y> root,
                                         final CommonAbstractCriteria query) {
        Predicate[] predicates = new Predicate[this.getValues().length];
        for (int index = 0; index < this.getValues().length; index++) {
            predicates[index] = this.getPredicate(index, builder, root);
        }
        return builder.and(builder.or(predicates));
    }

    /**
     * Return default predicate. This method call getPredicate with Expression.
     * Override it, to use your wanted expression.
     *
     * @param <T>     Entity class.
     * @param <Y>     Entity class.
     * @param index   Index of predicate.
     * @param builder Criteria builder.
     * @param root    Root path.
     * @return Predicate.
     */
    public <T, Y> Predicate getPredicate(final int index, final CriteriaBuilder builder, final From<T, Y> root) {
        if (PredicateOperator.NULL.equals(this.operators[index])) {
            return this.getNullPredicate(index, builder, root.get(this.getName()));
        }

        return this.getPredicate(index, builder, root.get(this.getName()));
    }

    /**
     * Override to return specific null predicate.
     *
     * @param <T>     Entity class.
     * @param index   Index of predicate.
     * @param builder Criteria builder.
     * @param field   Field expression.
     * @return Null predicate.
     */
    private <T> Predicate getNullPredicate(final int index, final CriteriaBuilder builder, final Expression<T> field) {
        if (this.getIsNotOperator(index)) {
            return builder.isNotNull(field);
        }
        return builder.isNull(field);
    }

    /**
     * Override to return specific predicate.
     *
     * @param <T>     Entity class.
     * @param index   Index of predicate.
     * @param builder Criteria builder.
     * @param field   Field expression.
     * @return Predicate.
     */
    public <T> Predicate getPredicate(final int index, final CriteriaBuilder builder, final Expression<T> field) {
        if (this.getIsNotOperator(index)) {
            return builder.notEqual(field, this.getValue(index));
        }
        return builder.equal(field, this.getValue(index));
    }
}
