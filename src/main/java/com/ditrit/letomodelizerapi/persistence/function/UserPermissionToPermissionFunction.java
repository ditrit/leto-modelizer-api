package com.ditrit.letomodelizerapi.persistence.function;

import com.ditrit.letomodelizerapi.persistence.model.Permission;
import com.ditrit.letomodelizerapi.persistence.model.UserPermission;

import java.util.function.Function;

public class UserPermissionToPermissionFunction implements Function<UserPermission, Permission> {
    @Override
    public Permission apply(final UserPermission userPermission) {
        Permission permission = new Permission();

        permission.setId(userPermission.getPermissionId());
        permission.setLibraryId(userPermission.getLibraryId());
        permission.setEntity(userPermission.getEntity());
        permission.setAction(userPermission.getAction());

        return permission;
    }
}
