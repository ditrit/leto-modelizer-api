package com.ditrit.letomodelizerapi.persistence.specification;

import com.ditrit.letomodelizerapi.helper.MockHelper;
import com.ditrit.letomodelizerapi.persistence.specification.filter.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@DisplayName("Test class: SpecificationHelper")
class SpecificationHelperTest extends MockHelper {

    @Test
    @DisplayName("Test getFilters: Should return all related filters to the filter map.")
    void testGetFilters() {
        SpecificationHelper<Entity> helper = new SpecificationHelper<>(Entity.class, Map.of(
                "aNumber", "1",
                "aString", "test",
                "aDate", "null",
                "aBoolean", "true",
                "aToken", "a",
                "aEnum", "b",
                "aUUID", UUID.randomUUID().toString()
        ));

        List<IPredicateFilter> filters = helper.getFilters();
        assertNotNull(filters);
        assertEquals(7, filters.size());
        assertEquals(NumberPredicateFilter.class, filters.get(0).getClass());
        assertEquals(TextPredicateFilter.class, filters.get(1).getClass());
        assertEquals(BooleanPredicateFilter.class, filters.get(2).getClass());
        assertEquals(TokenPredicateFilter.class, filters.get(3).getClass());
        assertEquals(DatePredicateFilter.class, filters.get(4).getClass());
        assertEquals(EnumPredicateFilter.class, filters.get(5).getClass());
        assertEquals(UUIDPredicateFilter.class, filters.get(6).getClass());
    }

    @Test
    @DisplayName("Test toPredicate: Should return non null predicate.")
    void testToPredicate() {
        EntityManager entityManager = mockEntityManager(Entity.class);
        SpecificationHelper<Entity> helper = new SpecificationHelper<>(Entity.class, Map.of(
                "aNumber", "1"
        ));
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Entity> query = builder.createQuery(Entity.class);
        final Root<Entity> root = query.from(Entity.class);
        assertNotNull(helper.toPredicate(root, query, builder));
    }

    class Entity {
        @FilterType(type = FilterType.Type.NUMBER)
        private Long aNumber;
        @FilterType(type = FilterType.Type.TEXT)
        private String aString;
        @FilterType(type = FilterType.Type.BOOLEAN)
        private boolean aBoolean;
        @FilterType(type = FilterType.Type.TOKEN)
        private String aToken;
        @FilterType(type = FilterType.Type.DATE)
        private Date aDate;
        @FilterType(type = FilterType.Type.ENUM)
        private String aEnum;
        @FilterType(type = FilterType.Type.UUID)
        private UUID aUUID;
    }
}
