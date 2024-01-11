package com.ditrit.letomodelizerapi.persistence.specification.filter;

import com.ditrit.letomodelizerapi.helper.MockHelper;
import com.ditrit.letomodelizerapi.persistence.specification.PredicateOperator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@DisplayName("Test class: TextPredicateFilter")
class TextPredicateFilterTest extends MockHelper {

    @Test
    @DisplayName("Test constructor: should set values from parameters.")
    void constructorTest() {
        TextPredicateFilter filter = new TextPredicateFilter(null, null);

        assertNull(filter.getName());
        assertEquals(0, filter.getValues().length);

        filter = new TextPredicateFilter("test", "value");
        assertEquals("test", filter.getName());
        assertEquals(1, filter.getValues().length);
        assertEquals("value", filter.getValues()[0]);
    }

    @Test
    @DisplayName("Test getPredicate: should return wanted predicate.")
    void testGetPredicate() {
        EntityManager entityManager = mockEntityManager(Entity.class);
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Entity> query = builder.createQuery(Entity.class);
        final Root<Entity> root = query.from(Entity.class);

        TextPredicateFilter filter = new TextPredicateFilter("text", "1");
        assertTrue(filter.extract());
        assertNotNull(filter.getPredicate(builder, root, null));
        assertEquals(PredicateOperator.EQUALS, filter.getOperator(0));

        filter = new TextPredicateFilter("text", "");
        assertTrue(filter.extract());
        assertNotNull(filter.getPredicate(builder, root, null));
        assertEquals(PredicateOperator.EQUALS, filter.getOperator(0));

        filter = new TextPredicateFilter("text", "%");
        assertTrue(filter.extract());
        assertNotNull(filter.getPredicate(builder, root, null));
        assertEquals(PredicateOperator.EQUALS, filter.getOperator(0));

        filter = new TextPredicateFilter("text", "lk_1");
        assertTrue(filter.extract());
        assertNotNull(filter.getPredicate(builder, root, null));
        assertFalse(filter.getIsNotOperator(0));
        assertEquals(PredicateOperator.LIKE, filter.getOperator(0));
        assertEquals("1", filter.getValue(0));
    }


    @Test
    @DisplayName("Test getPredicate: should return wanted 'not' predicate.")
    void testGetPredicateNot() {
        EntityManager entityManager = mockEntityManager(Entity.class);
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Entity> query = builder.createQuery(Entity.class);
        final Root<Entity> root = query.from(Entity.class);

        TextPredicateFilter filter = new TextPredicateFilter("text", "not_1");
        assertTrue(filter.extract());
        assertNotNull(filter.getPredicate(builder, root, null));

        filter = new TextPredicateFilter("text", "not_");
        assertTrue(filter.extract());
        assertNotNull(filter.getPredicate(builder, root, null));
        assertEquals(PredicateOperator.EQUALS, filter.getOperator(0));

        filter = new TextPredicateFilter("text", "not_%");
        assertTrue(filter.extract());
        assertNotNull(filter.getPredicate(builder, root, null));
        assertEquals(PredicateOperator.EQUALS, filter.getOperator(0));

        filter = new TextPredicateFilter("text", "not_lk_test");
        assertTrue(filter.extract());
        assertNotNull(filter.getPredicate(builder, root, null));
        assertEquals(PredicateOperator.LIKE, filter.getOperator(0));
        assertTrue(filter.getIsNotOperator(0));
        assertEquals("TEST", filter.getValue(0));

        filter = new TextPredicateFilter("text", "not_lk_t*es*t");
        assertTrue(filter.extract());
        assertNotNull(filter.getPredicate(builder, root, null));
        assertEquals(PredicateOperator.LIKE, filter.getOperator(0));
        assertTrue(filter.getIsNotOperator(0));
        assertEquals("T%ES%T", filter.getValue(0));
    }

    class Entity {
        @FilterType(type = FilterType.Type.TEXT)
        private String text;
    }
}
