package com.ditrit.letomodelizerapi.persistence.specification;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@DisplayName("Test class: PredicateOperator")
class PredicateOperatorTest {

    @Test
    @DisplayName("Test isValid: should return true on valid operator")
    void isValidTestValid() {
        assertTrue(PredicateOperator.isValid("eq"));
        assertTrue(PredicateOperator.isValid("LT"));
        assertTrue(PredicateOperator.isValid("Gt"));
        assertTrue(PredicateOperator.isValid("bT"));
    }

    @Test
    @DisplayName("Test isValid: should return false on invalid operator")
    void isValidTestInvalid() {
        assertFalse(PredicateOperator.isValid("="));
        assertFalse(PredicateOperator.isValid("<"));
        assertFalse(PredicateOperator.isValid(">"));
        assertFalse(PredicateOperator.isValid("<>"));
        assertFalse(PredicateOperator.isValid(null));
        assertFalse(PredicateOperator.isValid("!="));
        assertFalse(PredicateOperator.isValid("><"));
        assertFalse(PredicateOperator.isValid("!"));
        assertFalse(PredicateOperator.isValid("bad"));
    }

    @Test
    void getTest() {
        assertTrue(PredicateOperator.get("bad").isEmpty());
        Optional<PredicateOperator> test = PredicateOperator.get(PredicateOperator.EQUALS.getValue());
        assertFalse(test.isEmpty());
        assertEquals(PredicateOperator.EQUALS, test.get());
    }
}
