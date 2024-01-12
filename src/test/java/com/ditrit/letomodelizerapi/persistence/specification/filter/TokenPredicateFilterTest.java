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
@DisplayName("Test class: TokenPredicateFilter")
class TokenPredicateFilterTest extends MockHelper {

    @Test
    @DisplayName("Test constructor: should set values from parameters.")
    void constructorTest() {
        TokenPredicateFilter filter = new TokenPredicateFilter(null, null);

        assertNull(filter.getName());
        assertEquals(0, filter.getValues().length);
    }

    @Test
    @DisplayName("Test getPredicate: should return wanted predicate.")
    void testGetPredicate() {
        EntityManager entityManager = mockEntityManager(Entity.class);
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Entity> query = builder.createQuery(Entity.class);
        final Root<Entity> root = query.from(Entity.class);

        TokenPredicateFilter filter = new TokenPredicateFilter("token", "test");
        assertTrue(filter.extract());
        assertNotNull(filter.getPredicate(builder, root, null));
        assertEquals(PredicateOperator.EQUALS, filter.getOperator(0));

        filter = new TokenPredicateFilter("token", "not_test");
        assertTrue(filter.extract());
        assertNotNull(filter.getPredicate(builder, root, null));
        assertEquals(PredicateOperator.EQUALS, filter.getOperator(0));
    }

    class Entity {
        @FilterType(type = FilterType.Type.TOKEN)
        private String token;
    }
}
