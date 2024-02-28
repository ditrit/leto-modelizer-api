package com.ditrit.letomodelizerapi.persistence.function;

import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.persistence.model.UserAccessControlView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Tag("unit")
@DisplayName("Test class: UserAccesControlViewToUserFunction")
class UserAccesControlViewToUserFunctionTest {

    @Test
    @DisplayName("Test apply: should transform UserAccessControlView to User")
    void testApply() {
        UserAccessControlView userAccessControlView = new UserAccessControlView();
        userAccessControlView.setAccessControlId(UUID.randomUUID());
        userAccessControlView.setUserId(UUID.randomUUID());
        userAccessControlView.setId("3");
        userAccessControlView.setUserName("userName");
        userAccessControlView.setAccessControlName("accessControlName");
        userAccessControlView.setEmail("email");
        userAccessControlView.setLogin("login");
        userAccessControlView.setType(AccessControlType.ROLE);

        User user = new UserAccesControlViewToUserFunction().apply(userAccessControlView);

        assertEquals(userAccessControlView.getUserId(), user.getId());
        assertEquals("userName", user.getName());
        assertEquals("login", user.getLogin());
        assertEquals("email", user.getEmail());
        assertNull(user.getPicture());
        assertNull(user.getInsertDate());
        assertNull(user.getUpdateDate());
    }
}
