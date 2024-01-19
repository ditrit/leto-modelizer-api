package com.ditrit.letomodelizerapi.model;

import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.user.UserDTO;
import com.ditrit.letomodelizerapi.persistence.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@DisplayName("Test class: BeanMapper")
class BeanMapperTest {

    @Test
    @DisplayName("Test apply: should map dto to user.")
    void testApply() {
        final BeanMapper<User, UserDTO> mapper = new BeanMapper<>(UserDTO.class);

        final User entity = new User();
        entity.setEmail("email");
        UserDTO dto = mapper.apply(entity);

        assertEquals("email", dto.getEmail());

        dto = mapper.apply(null);
        assertNotNull(dto);
    }

    @Test
    @DisplayName("Test apply: should map dto to user without picture field.")
    void testWithIgnoreFields() {
        final BeanMapper<User, User> mapper = new BeanMapper<>(User.class, "picture");

        final User dto = new User();
        dto.setEmail("email");
        dto.setPicture("picture");

        final User value = mapper.apply(dto);
        final User expected = new User();
        expected.setEmail("email");

        assertEquals(expected, value);
    }

    @Test
    @DisplayName("Test apply: should throw exception on error.")
    void testException() throws NoSuchFieldException, SecurityException {
        class Source {
            @SuppressWarnings("unused")
            public String test1;
        }
        class Destination {
            @SuppressWarnings("unused")
            public String test2;
        }
        class Fail {
            @SuppressWarnings("unused")
            public Fail(final String test) {

            }
        }

        final BeanMapper<Source, Destination> mapper = new BeanMapper<>(Destination.class);

        ApiException exception = null;
        try {
            mapper.setFieldValue(Source.class.getDeclaredField("test1"), Source.class.getDeclaredField("test1"),
                    new Destination(), new User());
        } catch (final ApiException e) {
            exception = e;
        }
        assertNotNull(exception);

        exception = null;
        try {
            new BeanMapper<Source, Fail>(Fail.class).init();
        } catch (final ApiException e) {
            exception = e;
        }
        assertNotNull(exception);
    }
}
