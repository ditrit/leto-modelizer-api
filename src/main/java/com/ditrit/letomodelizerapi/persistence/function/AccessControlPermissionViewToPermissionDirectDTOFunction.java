package com.ditrit.letomodelizerapi.persistence.function;

import com.ditrit.letomodelizerapi.model.BeanMapper;
import com.ditrit.letomodelizerapi.model.permission.PermissionDirectDTO;
import com.ditrit.letomodelizerapi.persistence.model.AccessControlPermissionView;

import java.util.function.Function;

public class AccessControlPermissionViewToPermissionDirectDTOFunction implements
        Function<AccessControlPermissionView, PermissionDirectDTO> {
    @Override
    public PermissionDirectDTO apply(final AccessControlPermissionView accessControlPermissionView) {
        PermissionDirectDTO dto = new BeanMapper<>(PermissionDirectDTO.class, "id")
                .apply(accessControlPermissionView);

        dto.setId(accessControlPermissionView.getPermissionId());

        return dto;
    }
}
