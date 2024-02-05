package com.ditrit.letomodelizerapi.persistence.function;

import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDirectDTO;
import com.ditrit.letomodelizerapi.persistence.model.AccessControlTreeView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unit")
@DisplayName("Test class: AccessControlTreeViewToAccessControlDirectDTOFunction")
class AccessControlTreeViewToAccessControlDirectDTOFunctionTest {

    @Test
    @DisplayName("Test apply: should transform AccessControlTreeView to AccessControlDirectDTO")
    void testApply() {
        AccessControlTreeView accessControlTreeView = new AccessControlTreeView();
        accessControlTreeView.setAccessControlId(1L);
        accessControlTreeView.setAccessControlType("ROLE");
        accessControlTreeView.setParentAccessControlId(2L);
        accessControlTreeView.setParentAccessControlName("parent");
        accessControlTreeView.setParentAccessControlType("parent_type");
        accessControlTreeView.setIsDirect(true);

        AccessControlDirectDTO accessControl = new AccessControlTreeViewToAccessControlDirectDTOFunction()
                .apply(accessControlTreeView);

        assertEquals(2L, accessControl.getId());
        assertEquals("parent", accessControl.getName());
        assertTrue(accessControl.getIsDirect());
    }
}
