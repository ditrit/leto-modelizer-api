package com.ditrit.letomodelizerapi.persistence.specification;

import com.ditrit.letomodelizerapi.model.permission.ActionPermission;
import io.github.zorin95670.exception.SpringQueryFilterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@DisplayName("Test class: ActionPermissionPredicateFilter")
class ActionPermissionPredicateFilterTest {

    @Test
    @DisplayName("Test parseValue, should return valid ActionPermission")
    void testParseValue() {
        var predicateFilter = new ActionPermissionPredicateFilter<>("name", "ACCESS");

        assertEquals(ActionPermission.ACCESS, predicateFilter.parseValue("ACCESS"));
        assertEquals(ActionPermission.CREATE, predicateFilter.parseValue("CREATE"));
    }

    @Test
    @DisplayName("Test parseValue, should throw exception on invalid value")
    void testParseValueThrowException() {
        var predicateFilter = new ActionPermissionPredicateFilter<>("name", "value");

        SpringQueryFilterException exception = null;

        try {
            predicateFilter.parseValue("bad");
        } catch (SpringQueryFilterException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals("Invalid ActionPermission format: Unable to parse the value 'bad' as an ActionPermission.", exception.getMessage());
        assertNotNull(exception.getCause());
        assertEquals("ActionPermission", exception.getQueryFilterType());
        assertEquals("name", exception.getQueryParameterName());
        assertEquals("bad", exception.getQueryParameterValue());
    }
}