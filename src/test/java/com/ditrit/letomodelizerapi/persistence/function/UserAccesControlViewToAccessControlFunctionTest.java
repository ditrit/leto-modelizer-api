package com.ditrit.letomodelizerapi.persistence.function;

import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import com.ditrit.letomodelizerapi.persistence.model.AccessControl;
import com.ditrit.letomodelizerapi.persistence.model.UserAccessControlView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Tag("unit")
@DisplayName("Test class: UserAccesControlViewToAccessControlFunction")
class UserAccesControlViewToAccessControlFunctionTest {

    @Test
    @DisplayName("Test apply: should transform UserAccessControlView to AccessControl")
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

        AccessControl accessControl = new UserAccesControlViewToAccessControlFunction().apply(userAccessControlView);

        assertEquals(userAccessControlView.getAccessControlId(), accessControl.getId());
        assertEquals("accessControlName", accessControl.getName());
        assertEquals(AccessControlType.ROLE, accessControl.getType());
        assertNull(accessControl.getInsertDate());
        assertNull(accessControl.getUpdateDate());
    }
}
