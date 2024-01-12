package com.ditrit.letomodelizerapi.persistence.specification.filter;

import com.ditrit.letomodelizerapi.helper.MockHelper;
import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.persistence.specification.PredicateOperator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@DisplayName("Test class: DatePredicateFilter")
class DatePredicateFilterTest extends MockHelper {

    @Test
    @DisplayName("Test constructor: should set values from parameters.")
    void constructorTest() {
        DatePredicateFilter filter = new DatePredicateFilter(null, null);

        assertNull(filter.getName());
        assertEquals(0, filter.getValues().length);

        filter = new DatePredicateFilter("test", "value");
        assertEquals("test", filter.getName());
        assertEquals(1, filter.getValues().length);
        assertEquals("value", filter.getValues()[0]);
    }

    @Test
    @DisplayName("Test extract: should verify extract date from value.")
    void extractTest() {
        assertFalse(new DatePredicateFilter(null, null).extract());

        ApiException exception = null;
        try {
            new DatePredicateFilter("name", "bad").extract();
        } catch (ApiException e) {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals(ErrorType.WRONG_FILTER_VALUE.getMessage(), exception.getError().getMessage());

        DatePredicateFilter filter = new DatePredicateFilter(null, "2019-01-01 00:00:00");
        assertTrue(filter.extract());
        assertEquals(PredicateOperator.EQUALS, filter.getOperator(0));
        assertEquals("2019-01-01 00:00:00", filter.getValue(0));

        filter = new DatePredicateFilter(null, "gt2019-01-01 00:00:00");
        assertTrue(filter.extract());
        assertEquals(PredicateOperator.SUPERIOR, filter.getOperator(0));
        assertEquals("2019-01-01 00:00:00", filter.getValue(0));

        filter = new DatePredicateFilter(null, "lt2019-01-01 00:00:00");
        assertTrue(filter.extract());
        assertEquals(PredicateOperator.INFERIOR, filter.getOperator(0));
        assertEquals("2019-01-01 00:00:00", filter.getValue(0));

        filter = new DatePredicateFilter(null, "2018-01-01 00:00:00bt2019-01-01 00:00:00");
        assertTrue(filter.extract());
        assertEquals(PredicateOperator.BETWEEN, filter.getOperator(0));
        assertEquals("2019-01-01 00:00:00", filter.getValue(0));

        filter = new DatePredicateFilter(null, "null");
        assertTrue(filter.extract());
        assertEquals(PredicateOperator.NULL, filter.getOperator(0));
        assertEquals("null", filter.getValue(0));

        filter = new DatePredicateFilter(null, "not_null");
        assertTrue(filter.extract());
        assertEquals(PredicateOperator.NULL, filter.getOperator(0));
        assertTrue(filter.getIsNotOperator(0));
        assertEquals("null", filter.getValue(0));

        exception = null;
        try {
            new DatePredicateFilter(null, "bt2019-01-01 00:00:00").extract();
        } catch (ApiException e) {
            exception = e;
        }
        assertNotNull(exception);
        assertNotNull(exception.getError());
        assertEquals(ErrorType.EMPTY_VALUE.getMessage(), exception.getError().getMessage());
    }

    @Test
    @DisplayName("Test extract: should set wanted operator.")
    void getSpecificOperatorTest() {
        ApiException exception = null;
        try {
            new DatePredicateFilter(null, "2019-01-01 00:00:00aa2019-01-01 00:00:00").extract();
        } catch (ApiException e) {
            exception = e;
        }
        assertNotNull(exception);
        assertNotNull(exception.getError());
        assertEquals(ErrorType.WRONG_FILTER_OPERATOR.getMessage(), exception.getError().getMessage());
        exception = null;
        try {
            new DatePredicateFilter(null, "not_2019-01-01 00:00:00").extract();
        } catch (ApiException e) {
            exception = e;
        }
        assertNull(exception);
    }

    @Test
    @DisplayName("Test getDate: should return valid date.")
    void getDateTest() {
        final DatePredicateFilter filter = new DatePredicateFilter(null, null);
        ApiException exception = null;
        try {
            filter.getDate(null);
        } catch (final ApiException e) {
            exception = e;
        }
        assertNotNull(exception);

        Date date = null;
        exception = null;
        try {
            date = filter.getDate("2019-12-01 00:00:00");
        } catch (final ApiException e) {
            exception = e;
        }

        assertNull(exception);
        assertNotNull(date);
    }

    @Test
    @DisplayName("Test getPredicate: should return wanted predicate.")
    void getPredicateTest() {
        EntityManager entityManager = mockEntityManager(Entity.class);
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Entity> query = builder.createQuery(Entity.class);
        final Root<Entity> root = query.from(Entity.class);

        DatePredicateFilter filter = new DatePredicateFilter("name", "2019-01-01 00:00:00|not_2018-01-01 00:00:00");
        assertTrue(filter.extract());
        Predicate predicate = filter.getPredicate(builder, root, null);
        assertNotNull(predicate);

        filter = new DatePredicateFilter("date", "lt2019-01-01 00:00:00");
        assertTrue(filter.extract());
        predicate = filter.getPredicate(builder, root, null);
        assertNotNull(predicate);

        filter = new DatePredicateFilter("date", "gt2019-01-01 00:00:00");
        assertTrue(filter.extract());
        predicate = filter.getPredicate(builder, root, null);
        assertNotNull(predicate);

        filter = new DatePredicateFilter("date", "2019-01-01 00:00:00bt2019-01-02 00:00:00");
        assertTrue(filter.extract());
        predicate = filter.getPredicate(builder, root, null);
        assertNotNull(predicate);
        assertEquals(1, filter.getValues().length);
        assertEquals("2019-01-02 00:00:00", filter.getValue(0));

        filter = new DatePredicateFilter("date", "NULL");
        assertTrue(filter.extract());
        predicate = filter.getPredicate(builder, root, null);
        assertNotNull(predicate);

        filter = new DatePredicateFilter("date", "NOT_NULL");
        assertTrue(filter.extract());
        predicate = filter.getPredicate(builder, root, null);
        assertNotNull(predicate);

        filter = new DatePredicateFilter("date", "NOT_2019-01-01 00:00:00");
        assertTrue(filter.extract());
        predicate = filter.getPredicate(builder, root, null);
        assertNotNull(predicate);

        filter = new DatePredicateFilter("date", "not_2019-01-01 00:00:00bt2019-01-02 00:00:00");
        assertTrue(filter.extract());
        predicate = filter.getPredicate(builder, root, null);
        assertNotNull(predicate);
        assertEquals(1, filter.getValues().length);
        assertEquals("2019-01-02 00:00:00", filter.getValue(0));
    }

    class Entity {
        @FilterType(type = FilterType.Type.DATE)
        private Date date;
    }
}
