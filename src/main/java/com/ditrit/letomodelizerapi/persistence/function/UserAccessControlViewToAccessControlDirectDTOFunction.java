package com.ditrit.letomodelizerapi.persistence.function;

import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDirectDTO;
import com.ditrit.letomodelizerapi.persistence.model.UserAccessControlView;

import java.util.function.Function;

/**
 * Implements a {@link Function} that converts an {@link UserAccessControlView} entity into an
 * {@link AccessControlDirectDTO} object.
 * This function is designed to map data from a hierarchical view of access controls into a data transfer object that
 * includes information about the directness of the access control relationship.
 */
public class UserAccessControlViewToAccessControlDirectDTOFunction implements Function<UserAccessControlView,
        AccessControlDirectDTO> {

    @Override
    public AccessControlDirectDTO apply(final UserAccessControlView userAccessControlView) {
        AccessControlDirectDTO dto = new AccessControlDirectDTO();

        dto.setId(userAccessControlView.getAccessControlId());
        dto.setName(userAccessControlView.getName());
        dto.setIsDirect(userAccessControlView.getIsDirect());

        return dto;
    }
}
