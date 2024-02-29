package com.ditrit.letomodelizerapi.persistence.function;

import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDirectDTO;
import com.ditrit.letomodelizerapi.persistence.model.AccessControlTreeView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unit")
@DisplayName("Test class: AccessControlTreeViewToAccessControlDirectDTOFunction")
class AccessControlTreeViewToAccessControlDirectDTOFunctionTest {

    @Test
    @DisplayName("Test apply: should transform AccessControlTreeView to AccessControlDirectDTO with parent")
    void testApply() {
        AccessControlTreeView accessControlTreeView = new AccessControlTreeView();
        accessControlTreeView.setAccessControlId(UUID.randomUUID());
        accessControlTreeView.setAccessControlName("current");
        accessControlTreeView.setAccessControlType("current_Type");
        accessControlTreeView.setParentAccessControlId(UUID.randomUUID());
        accessControlTreeView.setParentAccessControlName("parent");
        accessControlTreeView.setParentAccessControlType("parent_type");
        accessControlTreeView.setIsDirect(true);

        AccessControlDirectDTO accessControl = new AccessControlTreeViewToAccessControlDirectDTOFunction()
                .apply(accessControlTreeView);

        assertEquals(accessControlTreeView.getParentAccessControlId(), accessControl.getId());
        assertEquals("parent", accessControl.getName());
        assertTrue(accessControl.getIsDirect());
    }

    @Test
    @DisplayName("Test apply: should transform AccessControlTreeView to AccessControlDirectDTO with current")
    void testApplyNotFromParent() {
        AccessControlTreeView accessControlTreeView = new AccessControlTreeView();
        accessControlTreeView.setAccessControlId(UUID.randomUUID());
        accessControlTreeView.setAccessControlName("current");
        accessControlTreeView.setAccessControlType("current_Type");
        accessControlTreeView.setParentAccessControlId(UUID.randomUUID());
        accessControlTreeView.setParentAccessControlName("parent");
        accessControlTreeView.setParentAccessControlType("parent_type");
        accessControlTreeView.setIsDirect(true);

        AccessControlDirectDTO accessControl = new AccessControlTreeViewToAccessControlDirectDTOFunction(false)
                .apply(accessControlTreeView);

        assertEquals(accessControlTreeView.getAccessControlId(), accessControl.getId());
        assertEquals("current", accessControl.getName());
        assertTrue(accessControl.getIsDirect());
    }
}
