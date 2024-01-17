package com.ditrit.letomodelizerapi.persistence.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Tag("unit")
@DisplayName("Test class: User")
class UserTest {

    @Test
    @DisplayName("Test prePersist: should set insert date")
    void isValidTestValid() {
        User user = new User();
        assertNull(user.getInsertDate());

        user.prePersist();
        assertNotNull(user.getInsertDate());
    }
}
