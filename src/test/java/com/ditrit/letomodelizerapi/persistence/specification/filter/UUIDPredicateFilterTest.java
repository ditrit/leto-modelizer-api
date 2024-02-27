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

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@DisplayName("Test class: UUIDPredicateFilter")
class UUIDPredicateFilterTest extends MockHelper {

    @Test
    @DisplayName("Test constructor: should set values from parameters.")
    void constructorTest() {
        UUIDPredicateFilter filter = new UUIDPredicateFilter(null, null);

        assertNull(filter.getName());
        assertEquals(0, filter.getValues().length);
    }

    @Test
    @DisplayName("Test getPredicate: should return wanted predicate.")
    void getPredicateTest() {
        EntityManager entityManager = this.mockEntityManager(BooleanPredicateFilterTest.Entity.class);
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<BooleanPredicateFilterTest.Entity> query = builder.createQuery(BooleanPredicateFilterTest.Entity.class);
        final Root<UUIDPredicateFilterTest.Entity> root = query.from(UUIDPredicateFilterTest.Entity.class);

        UUIDPredicateFilter filter = new UUIDPredicateFilter("uuid", UUID.randomUUID().toString());
        assertTrue(filter.extract());

        Predicate predicate = filter.getPredicate(builder, root, null);
        assertNotNull(predicate);
        assertFalse(filter.getIsNotOperator(0));

        filter = new UUIDPredicateFilter("uuid", "not_" + UUID.randomUUID());
        assertTrue(filter.extract());

        predicate = filter.getPredicate(builder, root, null);
        assertNotNull(predicate);
        assertTrue(filter.getIsNotOperator(0));
    }

    class Entity {
        @FilterType(type = FilterType.Type.UUID)
        private UUID uuid;
    }
}
