package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.persistence.model.AccessControlPermission;
import com.ditrit.letomodelizerapi.persistence.model.AccessControlPermissionView;
import com.ditrit.letomodelizerapi.persistence.repository.AccessControlPermissionRepository;
import com.ditrit.letomodelizerapi.persistence.repository.AccessControlPermissionViewRepository;
import com.ditrit.letomodelizerapi.persistence.specification.CustomSpringQueryFilterSpecification;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of the AccessControlService interface.
 *
 * <p>This class provides concrete implementations for the access control management operations defined in
 * AccessControlService.
 * AccessControlServiceImpl interacts with the underlying repository layer to perform these operations,
 * ensuring that business logic and data access are effectively managed.
 */
@Slf4j
@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AccessControlPermissionServiceImpl implements AccessControlPermissionService {

    /**
     * The AccessControlPermissionViewRepository instance is injected by Spring's dependency injection mechanism.
     * This repository is used for performing database operations related to AccessControlPermissionView entities,
     * such as querying, saving, and updating access control data.
     */
    private AccessControlPermissionViewRepository accessControlPermissionViewRepository;

    /**
     * The AccessControlPermissionRepository instance is injected by Spring's dependency injection mechanism.
     * This repository is used for performing database operations related to AccessControlPermission entities,
     * such as querying, saving, and updating access control data.
     */
    private AccessControlPermissionRepository accessControlPermissionRepository;

    @Override
    public Page<AccessControlPermissionView> findAll(final UUID id,
                                                     final Map<String, List<String>> immutableFilters,
                                                     final QueryFilter queryFilter) {
        var filters = new HashMap<>(immutableFilters);
        filters.put("accessControlId", List.of(id.toString()));

        return accessControlPermissionViewRepository.findAll(
                new CustomSpringQueryFilterSpecification<>(AccessControlPermissionView.class, filters),
                queryFilter.getPageable());
    }

    @Override
    public void associate(final UUID id, final UUID permissionId) {
        Optional<AccessControlPermission> accessControlPermissionOptional = accessControlPermissionRepository
                .findByAccessControlIdAndPermissionId(id, permissionId);

        if (accessControlPermissionOptional.isPresent()) {
            return;
        }

        AccessControlPermission accessControlPermission = new AccessControlPermission();
        accessControlPermission.setAccessControlId(id);
        accessControlPermission.setPermissionId(permissionId);

        accessControlPermissionRepository.save(accessControlPermission);
    }

    @Override
    public void dissociate(final UUID id, final UUID permissionId) {
        AccessControlPermission accessControlPermission = accessControlPermissionRepository
                .findByAccessControlIdAndPermissionId(id, permissionId)
                .orElseThrow(() -> new ApiException(ErrorType.ENTITY_NOT_FOUND, "association"));

        accessControlPermissionRepository.delete(accessControlPermission);
    }
}
