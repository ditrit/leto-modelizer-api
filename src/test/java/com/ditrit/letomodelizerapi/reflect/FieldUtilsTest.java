package com.ditrit.letomodelizerapi.reflect;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@DisplayName("Test class: FieldUtils")
class FieldUtilsTest {

    @Test
    @DisplayName("Test constructor: should not be accessible.")
    void testConstructor() {
        Exception exception = null;
        try {
            final Constructor<FieldUtils> c = FieldUtils.class.getDeclaredConstructor();
            c.setAccessible(true);
            c.newInstance();
        } catch (final Exception e) {
            exception = e;
        }
        assertNotNull(exception);
        assertEquals(UnsupportedOperationException.class, exception.getCause().getClass());
    }

    @Test
    @DisplayName("Test hasField: should verify if field is contain in the provided class.")
    void testClassHasField() {
        class Test {
            @SuppressWarnings("unused") // Used in tests below.
            private String test;
        }

        assertFalse(FieldUtils.hasField(Test.class, "bad"));
        assertTrue(FieldUtils.hasField(Test.class, "test"));
    }

    @Test
    @DisplayName("Test hasField: should verify if field is contain in the super class.")
    void testSuperClassHasField() {
        class Test {
            @SuppressWarnings("unused") // Used in tests below.
            private String test;
        }
        class SubTest extends Test {
            @SuppressWarnings("unused") // Used in tests below.
            private String subTest;
        }

        assertFalse(FieldUtils.hasField(SubTest.class, "bad"));
        assertTrue(FieldUtils.hasField(SubTest.class, "subTest"));
        assertTrue(FieldUtils.hasField(SubTest.class, "test"));
    }
}
