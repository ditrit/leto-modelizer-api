package com.ditrit.letomodelizerapi.persistence.specification.filter;

import jakarta.persistence.criteria.CommonAbstractCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;

/**
 * Predicate extractor.
 */
public interface IPredicateFilter {

    /**
     * Extract Filter from query value.
     *
     * @return True if there is a predicate, otherwise false.
     */
    boolean extract();

    /**
     * Create predicate from entity.
     *
     * @param <T>     Entity class.
     * @param <Y>     Entity class.
     * @param builder Criteria builder.
     * @param root    Entity root.
     * @param query   Default Query.
     * @return Predicate from entity.
     */
    <T, Y> Predicate getPredicate(CriteriaBuilder builder, From<T, Y> root, CommonAbstractCriteria query);

}
