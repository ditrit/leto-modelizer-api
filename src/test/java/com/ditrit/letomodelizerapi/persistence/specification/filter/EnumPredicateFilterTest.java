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
@DisplayName("Test class: EnumPredicateFilter")
class EnumPredicateFilterTest extends MockHelper {

    @Test
    @DisplayName("Test constructor: should set values from parameters.")
    void constructorTest() {
        EnumPredicateFilter filter = new EnumPredicateFilter(null, null);
        filter.extract();

        assertNull(filter.getName());
        assertEquals(0, filter.getValues().length);

        filter = new EnumPredicateFilter("test", "enum");
        filter.extract();
        assertEquals("test", filter.getName());
        assertEquals(1, filter.getValues().length);
        assertEquals("enum", filter.getValues()[0]);
        assertFalse(filter.getIsNotOperator(0));

        filter = new EnumPredicateFilter("test", "not_enum");
        filter.extract();
        assertEquals("test", filter.getName());
        assertEquals(1, filter.getValues().length);
        assertEquals("enum", filter.getValues()[0]);
        assertTrue(filter.getIsNotOperator(0));
    }

    @Test
    @DisplayName("Test getPredicate: should return wanted predicate.")
    void getPredicateTest() {
        EntityManager entityManager = this.mockEntityManager(Entity.class);
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Entity> query = builder.createQuery(Entity.class);
        final Root<Entity> root = query.from(Entity.class);

        EnumPredicateFilter filter = new EnumPredicateFilter("enum", "test");
        assertTrue(filter.extract());

        Predicate predicate = filter.getPredicate(builder, root, null);
        assertNotNull(predicate);
        assertFalse(filter.getIsNotOperator(0));

        filter = new EnumPredicateFilter("enum", "not_test");
        assertTrue(filter.extract());

        predicate = filter.getPredicate(builder, root, null);
        assertNotNull(predicate);
        assertTrue(filter.getIsNotOperator(0));
    }

    class Entity {
        @FilterType(type = FilterType.Type.ENUM)
        private String bool;
    }
}
