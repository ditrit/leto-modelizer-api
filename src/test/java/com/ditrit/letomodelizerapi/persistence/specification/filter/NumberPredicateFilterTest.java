package com.ditrit.letomodelizerapi.persistence.specification.filter;


import com.ditrit.letomodelizerapi.helper.MockHelper;
import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@DisplayName("Test class: NumberPredicateFilter")
class NumberPredicateFilterTest extends MockHelper {

    @Test
    @DisplayName("Test constructor: should set values from parameters.")
    void constructorTest() {
        NumberPredicateFilter filter = new NumberPredicateFilter(null, null);

        assertNull(filter.getName());
        assertEquals(0, filter.getValues().length);

        filter = new NumberPredicateFilter("test", "value");
        assertEquals("test", filter.getName());
        assertEquals(1, filter.getValues().length);
        assertEquals("value", filter.getValues()[0]);
    }

    @Test
    @DisplayName("Test extract: should verify extract number from value.")
    void extractTest() {
        NumberPredicateFilter filter = new NumberPredicateFilter(null, null);
        assertFalse(filter.extract());

        filter = new NumberPredicateFilter(null, "null");
        assertTrue(filter.extract());

        filter = new NumberPredicateFilter(null, "not_null");
        assertTrue(filter.extract());

        filter = new NumberPredicateFilter(null, "1");
        assertTrue(filter.extract());

        filter = new NumberPredicateFilter(null, "1|1");
        assertTrue(filter.extract());

        filter = new NumberPredicateFilter(null, "1|2|3|4|5");
        assertTrue(filter.extract());

        filter = new NumberPredicateFilter(null, "not_1");
        assertTrue(filter.extract());

        filter = new NumberPredicateFilter(null, "not_1|2|not_3|4|5|not_null");
        assertTrue(filter.extract());
    }

    @Test
    @DisplayName("Test extract: should throw exception on invalid number.")
    void testWithBadNumber() {
        ApiException exception = null;

        try {
            new NumberPredicateFilter("test", "a").extract();
        } catch (final ApiException hre) {
            exception = hre;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.WRONG_FILTER_VALUE.getMessage(), exception.getMessage());
        exception = null;

        try {
            new NumberPredicateFilter("test", "9999999999999999999999999").extract();
        } catch (final ApiException hre) {
            exception = hre;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.WRONG_FILTER_VALUE.getMessage(), exception.getMessage());
        exception = null;

        try {
            new NumberPredicateFilter("test", "notnot").extract();
        } catch (final ApiException hre) {
            exception = hre;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.WRONG_FILTER_VALUE.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("Test getPredicate: should return wanted predicate.")
    void testGetPredicate() {
        EntityManager entityManager = mockEntityManager(Entity.class);
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Entity> query = builder.createQuery(Entity.class);
        final Root<Entity> root = query.from(Entity.class);

        NumberPredicateFilter filter = new NumberPredicateFilter("number", "not_1");
        assertTrue(filter.extract());
        assertNotNull(filter.getPredicate(builder, root, null));

        filter = new NumberPredicateFilter("number", "1");
        assertTrue(filter.extract());
        assertNotNull(filter.getPredicate(builder, root, null));

        filter = new NumberPredicateFilter("number", "1|not_2");
        assertTrue(filter.extract());
        assertNotNull(filter.getPredicate(builder, root, null));

        filter = new NumberPredicateFilter("number", "not_1|not_2");
        assertTrue(filter.extract());
        assertNotNull(filter.getPredicate(builder, root, null));

        filter = new NumberPredicateFilter("number", "1|2");
        assertTrue(filter.extract());
        assertNotNull(filter.getPredicate(builder, root, null));

        filter = new NumberPredicateFilter("number", "1|2|null");
        assertTrue(filter.extract());
        assertNotNull(filter.getPredicate(builder, root, null));
    }

    class Entity {
        @FilterType(type = FilterType.Type.NUMBER)
        private Long number;
    }
}
