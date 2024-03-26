package com.ditrit.letomodelizerapi.persistence.function;

import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDirectDTO;
import com.ditrit.letomodelizerapi.persistence.model.UserAccessControlView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unit")
@DisplayName("Test class: UserAccessControlViewToAccessControlDirectDTOFunction")
class UserAccessControlViewToAccessControlDirectDTOFunctionTest {

    @Test
    @DisplayName("Test apply: should transform UserAccessControlView to AccessControlDirectDTO with parent")
    void testApply() {
        UserAccessControlView userAccessControlView = new UserAccessControlView();
        userAccessControlView.setAccessControlId(UUID.randomUUID());
        userAccessControlView.setName("current");
        userAccessControlView.setIsDirect(true);

        AccessControlDirectDTO accessControl = new UserAccessControlViewToAccessControlDirectDTOFunction()
                .apply(userAccessControlView);

        assertEquals(userAccessControlView.getAccessControlId(), accessControl.getId());
        assertEquals("current", accessControl.getName());
        assertTrue(accessControl.getIsDirect());
    }
}
