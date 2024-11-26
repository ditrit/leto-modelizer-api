package com.ditrit.letomodelizerapi.persistence.specification;

import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import io.github.zorin95670.exception.SpringQueryFilterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@DisplayName("Test class: AccessControlTypePredicateFilter")
class AccessControlTypePredicateFilterTest {

    @Test
    @DisplayName("Test parseValue, should return valid AccessControlType")
    void testParseValue() {
        var predicateFilter = new AccessControlTypePredicateFilter<>("name", "ROLE");

        assertEquals(AccessControlType.ROLE, predicateFilter.parseValue("ROLE"));
        assertEquals(AccessControlType.GROUP, predicateFilter.parseValue("GROUP"));
    }

    @Test
    @DisplayName("Test parseValue, should throw exception on invalid value")
    void testParseValueThrowException() {
        var predicateFilter = new AccessControlTypePredicateFilter<>("name", "value");

        SpringQueryFilterException exception = null;

        try {
            predicateFilter.parseValue("bad");
        } catch (SpringQueryFilterException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals("Invalid AccessControlType format: Unable to parse the value 'bad' as an AccessControlType.", exception.getMessage());
        assertNotNull(exception.getCause());
        assertEquals("AccessControlType", exception.getQueryFilterType());
        assertEquals("name", exception.getQueryParameterName());
        assertEquals("bad", exception.getQueryParameterValue());
    }
}