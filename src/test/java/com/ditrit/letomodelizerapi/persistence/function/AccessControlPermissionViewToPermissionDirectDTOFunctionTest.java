package com.ditrit.letomodelizerapi.persistence.function;


import com.ditrit.letomodelizerapi.model.permission.ActionPermission;
import com.ditrit.letomodelizerapi.model.permission.EntityPermission;
import com.ditrit.letomodelizerapi.model.permission.PermissionDirectDTO;
import com.ditrit.letomodelizerapi.persistence.model.AccessControlPermissionView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
@DisplayName("Test class: AccessControlPermissionViewToPermissionDirectDTOFunction")
class AccessControlPermissionViewToPermissionDirectDTOFunctionTest {

    @Test
    @DisplayName("Test apply: should return valid dto")
    void testApply() {
        AccessControlPermissionViewToPermissionDirectDTOFunction mapper = new AccessControlPermissionViewToPermissionDirectDTOFunction();

        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();

        AccessControlPermissionView accessControlPermissionView = new AccessControlPermissionView();
        accessControlPermissionView.setAccessControlId(id3);
        accessControlPermissionView.setPermissionId(id1);
        accessControlPermissionView.setIsDirect(true);
        accessControlPermissionView.setEntity(EntityPermission.ADMIN.name());
        accessControlPermissionView.setAction(ActionPermission.ACCESS.name());
        accessControlPermissionView.setLibraryId(id2);

        PermissionDirectDTO expectedDTO = new PermissionDirectDTO();
        expectedDTO.setId(id1);
        expectedDTO.setIsDirect(true);
        expectedDTO.setEntity(EntityPermission.ADMIN.name());
        expectedDTO.setAction(ActionPermission.ACCESS.name());
        expectedDTO.setLibraryId(id2);
        assertEquals(expectedDTO, mapper.apply(accessControlPermissionView));
    }
}
