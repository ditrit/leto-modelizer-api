package com.ditrit.letomodelizerapi.persistence.function;

import com.ditrit.letomodelizerapi.model.permission.ActionPermission;
import com.ditrit.letomodelizerapi.model.permission.EntityPermission;
import com.ditrit.letomodelizerapi.persistence.model.Permission;
import com.ditrit.letomodelizerapi.persistence.model.UserPermission;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
@DisplayName("Test class: UserPermissionToPermissionFunction")
class UserPermissionToPermissionFunctionTest {

    @Test
    @DisplayName("Test apply: should transform UserPermission to Permission")
    void testApply() {
        UserPermissionToPermissionFunction mapper = new UserPermissionToPermissionFunction();

        UUID permissionId = UUID.randomUUID();
        UUID libraryId = UUID.randomUUID();

        Permission expectedPermission = new Permission();

        expectedPermission.setId(permissionId);
        expectedPermission.setLibraryId(libraryId);
        expectedPermission.setEntity(EntityPermission.ADMIN.name());
        expectedPermission.setAction(ActionPermission.ACCESS.name());

        UserPermission userPermission = new UserPermission();
        userPermission.setId("id");
        userPermission.setPermissionId(permissionId);
        userPermission.setLibraryId(libraryId);
        userPermission.setEntity(EntityPermission.ADMIN.name());
        userPermission.setAction(ActionPermission.ACCESS.name());

        assertEquals(expectedPermission, mapper.apply(userPermission));
    }
}
