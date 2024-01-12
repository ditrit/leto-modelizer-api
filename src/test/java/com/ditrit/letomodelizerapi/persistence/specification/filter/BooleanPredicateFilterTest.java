package com.ditrit.letomodelizerapi.persistence.specification.filter;

import com.ditrit.letomodelizerapi.helper.MockHelper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@DisplayName("Test class: BooleanPredicateFilter")
class BooleanPredicateFilterTest extends MockHelper {

    @Test
    @DisplayName("Test constructor: should set values from parameters.")
    void constructorTest() {
        BooleanPredicateFilter filter = new BooleanPredicateFilter(null, null);
        filter.extract();

        assertNull(filter.getName());
        assertEquals(0, filter.getValues().length);

        filter = new BooleanPredicateFilter("test", "true");
        filter.extract();
        assertEquals("test", filter.getName());
        assertEquals(1, filter.getValues().length);
        assertEquals("true", filter.getValues()[0]);
        assertFalse(filter.getIsNotOperator(0));

        filter = new BooleanPredicateFilter("test", "not_true");
        filter.extract();
        assertEquals("test", filter.getName());
        assertEquals(1, filter.getValues().length);
        assertEquals("true", filter.getValues()[0]);
        assertTrue(filter.getIsNotOperator(0));
    }

    @Test
    @DisplayName("Test getPredicate: should return wanted predicate.")
    void getPredicateTest() {
        EntityManager entityManager = this.mockEntityManager(Entity.class);
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Entity> query = builder.createQuery(Entity.class);
        final Root<Entity> root = query.from(Entity.class);

        BooleanPredicateFilter filter = new BooleanPredicateFilter("bool", "test");
        assertTrue(filter.extract());

        Predicate predicate = filter.getPredicate(builder, root, null);
        assertNotNull(predicate);
        assertFalse(filter.getIsNotOperator(0));

        filter = new BooleanPredicateFilter("bool", "not_test");
        assertTrue(filter.extract());

        predicate = filter.getPredicate(builder, root, null);
        assertNotNull(predicate);
        assertTrue(filter.getIsNotOperator(0));
    }

    class Entity {
        @FilterType(type = FilterType.Type.BOOLEAN)
        private boolean bool;
    }
}
