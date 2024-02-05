package com.ditrit.letomodelizerapi.persistence.function;

import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDirectDTO;
import com.ditrit.letomodelizerapi.persistence.model.AccessControlTreeView;

import java.util.function.Function;

/**
 * Implements a {@link Function} that converts an {@link AccessControlTreeView} entity into an
 * {@link AccessControlDirectDTO} object.
 * This function is designed to map data from a hierarchical view of access controls into a data transfer object that
 * includes information about the directness of the access control relationship.
 */
public class AccessControlTreeViewToAccessControlDirectDTOFunction implements Function<AccessControlTreeView,
        AccessControlDirectDTO> {

    @Override
    public AccessControlDirectDTO apply(final AccessControlTreeView accessControlTreeView) {
        AccessControlDirectDTO dto = new AccessControlDirectDTO();

        dto.setId(accessControlTreeView.getParentAccessControlId());
        dto.setName(accessControlTreeView.getParentAccessControlName());
        dto.setIsDirect(accessControlTreeView.getIsDirect());

        return dto;
    }
}
