package com.ditrit.letomodelizerapi.persistence.specification.filter;

import com.ditrit.letomodelizerapi.helper.MockHelper;
import com.ditrit.letomodelizerapi.persistence.specification.PredicateOperator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class PredicateFilterTest extends MockHelper {

    @Test
    void testConstructor() {
        PredicateFilterFake filter = new PredicateFilterFake(null, null);
        assertNull(filter.getName());
        assertEquals(0, filter.getValues().length);

        filter = new PredicateFilterFake("text", "test");
        assertEquals("text", filter.getName());
        assertEquals(1, filter.getValues().length);
        assertEquals("test", filter.getValues()[0]);

        assertTrue(PredicateOperator.get("bad").isEmpty());
        assertNotNull(filter.getOperators());
    }

    @Test
    void testSetOperatorFromValue() {
        PredicateFilterFake filter = new PredicateFilterFake("eq1", PredicateOperator.EQUALS.getValue());
        filter.extract();
        assertEquals(PredicateOperator.EQUALS, filter.getOperator(0));
        assertFalse(filter.getIsNotOperator(0));

        filter = new PredicateFilterFake(null, "not_null");
        filter.extract();
        assertEquals(PredicateOperator.NULL, filter.getOperator(0));
        assertTrue(filter.getIsNotOperator(0));

        filter = new PredicateFilterFake(null, "null");
        filter.extract();
        assertEquals(PredicateOperator.NULL, filter.getOperator(0));
        assertFalse(filter.getIsNotOperator(0));

        filter = new PredicateFilterFake(null, PredicateOperator.NULL.getValue());
        filter.extract();
        assertEquals(PredicateOperator.NULL, filter.getOperator(0));
    }

    @Test
    void testExtract() {
        PredicateFilterFake filter = new PredicateFilterFake(null, null);
        assertFalse(filter.extract());

        filter = new PredicateFilterFake(null, "not_");
        assertTrue(filter.extract());
        assertEquals(PredicateOperator.EQUALS, filter.getOperator(0));
        assertTrue(filter.getIsNotOperator(0));
        assertEquals("", filter.getValue(0));
    }

    @Test
    void getPredicateTest() {
        EntityManager entityManager = mockEntityManager(Entity.class);
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Entity> query = builder.createQuery(Entity.class);
        final Root<Entity> root = query.from(Entity.class);

        PredicateFilterFake filter = new PredicateFilterFake("text", "test");
        assertTrue(filter.extract());
        assertNotNull(filter.getPredicate(builder, root, null));

        filter = new PredicateFilterFake("text", "not_null");
        assertTrue(filter.extract());
        assertNotNull(filter.getPredicate(builder, root, null));

        filter = new PredicateFilterFake("text", "null");
        assertTrue(filter.extract());
        assertNotNull(filter.getPredicate(builder, root, null));

        filter = new PredicateFilterFake("text", "not_1");
        assertTrue(filter.extract());
        assertNotNull(filter.getPredicate(builder, root, null));

        filter = new PredicateFilterFake("text", "1");
        assertTrue(filter.extract());
        assertNotNull(filter.getPredicate(builder, root, null));
    }

    private class PredicateFilterFake extends PredicateFilter {

        public PredicateFilterFake(final String name, final String value) {
            super(name, value, FilterType.Type.TEXT);
        }
    }


    class Entity {
        @FilterType(type = FilterType.Type.NUMBER)
        private Long number;
        @FilterType(type = FilterType.Type.TEXT)
        private String text;
        @FilterType(type = FilterType.Type.BOOLEAN)
        private boolean bool;
        @FilterType(type = FilterType.Type.TOKEN)
        private String token;
        @FilterType(type = FilterType.Type.DATE)
        private Date date;
    }
}
