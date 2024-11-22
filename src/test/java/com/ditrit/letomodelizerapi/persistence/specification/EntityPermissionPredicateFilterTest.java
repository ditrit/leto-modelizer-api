package com.ditrit.letomodelizerapi.persistence.specification;

import com.ditrit.letomodelizerapi.model.permission.EntityPermission;
import io.github.zorin95670.exception.SpringQueryFilterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@DisplayName("Test class: EntityPermissionPredicateFilter")
class EntityPermissionPredicateFilterTest {

    @Test
    @DisplayName("Test parseValue, should return valid EntityPermission")
    void testParseValue() {
        var predicateFilter = new EntityPermissionPredicateFilter<>("name", "DIAGRAM");

        assertEquals(EntityPermission.DIAGRAM, predicateFilter.parseValue("DIAGRAM"));
        assertEquals(EntityPermission.ADMIN, predicateFilter.parseValue("ADMIN"));
    }

    @Test
    @DisplayName("Test parseValue, should throw exception on invalid value")
    void testParseValueThrowException() {
        var predicateFilter = new EntityPermissionPredicateFilter<>("name", "value");

        SpringQueryFilterException exception = null;

        try {
            predicateFilter.parseValue("bad");
        } catch (SpringQueryFilterException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals("Invalid EntityPermission format: Unable to parse the value 'bad' as an EntityPermission.", exception.getMessage());
        assertNotNull(exception.getCause());
        assertEquals("EntityPermission", exception.getQueryFilterType());
        assertEquals("name", exception.getQueryParameterName());
        assertEquals("bad", exception.getQueryParameterValue());
    }
}