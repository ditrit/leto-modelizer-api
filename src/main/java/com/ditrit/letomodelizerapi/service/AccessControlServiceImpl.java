package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.config.Constants;
import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.model.BeanMapper;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDirectDTO;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlRecord;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.persistence.function.AccessControlTreeViewToAccessControlDirectDTOFunction;
import com.ditrit.letomodelizerapi.persistence.function.UserAccesControlViewToUserFunction;
import com.ditrit.letomodelizerapi.persistence.function.UserAccessControlViewToAccessControlDirectDTOFunction;
import com.ditrit.letomodelizerapi.persistence.model.AccessControl;
import com.ditrit.letomodelizerapi.persistence.model.AccessControlTree;
import com.ditrit.letomodelizerapi.persistence.model.AccessControlTreeView;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.persistence.model.UserAccessControl;
import com.ditrit.letomodelizerapi.persistence.model.UserAccessControlView;
import com.ditrit.letomodelizerapi.persistence.repository.AccessControlRepository;
import com.ditrit.letomodelizerapi.persistence.repository.AccessControlTreeRepository;
import com.ditrit.letomodelizerapi.persistence.repository.AccessControlTreeViewRepository;
import com.ditrit.letomodelizerapi.persistence.repository.UserAccessControlRepository;
import com.ditrit.letomodelizerapi.persistence.repository.UserAccessControlViewRepository;
import com.ditrit.letomodelizerapi.persistence.specification.CustomSpringQueryFilterSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
 * <p>
 * This class provides concrete implementations for the access control management operations defined in
 * AccessControlService.
 * </p>
 * AccessControlServiceImpl interacts with the underlying repository layer to perform these operations,
 * ensuring that business logic and data access are effectively managed.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccessControlServiceImpl implements AccessControlService {

    /**
     * The AccessControlRepository instance is injected by Spring's dependency injection mechanism.
     * This repository is used for performing database operations related to AccessControl entities, such as querying,
     * saving, and updating access control data.
     */
    private final AccessControlRepository accessControlRepository;

    /**
     * The AccessControlTreeRepository instance is injected by Spring's dependency injection mechanism.
     * This repository is used for performing database operations related to AccessControlTree entities, such as
     * querying, saving, and updating access control data.
     */
    private final AccessControlTreeRepository accessControlTreeRepository;

    /**
     * The AccessControlTreeViewRepository instance is injected by Spring's dependency injection mechanism.
     * This repository is used for performing database operations related to AccessControlTreeView entities, such as
     * querying, saving, and updating access control data.
     */
    private final AccessControlTreeViewRepository accessControlTreeViewRepository;

    /**
     * The UserAccessControlRepository instance is injected by Spring's dependency injection mechanism.
     * This repository is used for performing database operations related to UserAccessControl entities, such as
     * querying, saving, and updating user access control data.
     */
    private final UserAccessControlViewRepository userAccessControlViewRepository;

    /**
     * The UserAccessControlRepository instance is injected by Spring's dependency injection mechanism.
     * This repository is used for performing database operations related to UserAccessControl entities, such as
     * querying, saving, and updating user access control data.
     */
    private final UserAccessControlRepository userAccessControlRepository;

    /**
     * The UserService instance is injected by Spring's dependency injection mechanism.
     * This service is responsible for handling all user-related business logic and operations.
     * It typically includes functionalities such as user authentication, user data retrieval,
     * user profile updates, and other user-centric business processes.
     */
    private final UserService userService;

    /**
     * The unique identifier (UUID) for the super administrator.
     * This field stores the UUID of the super administrator, a predefined user or role with the highest
     * level of access and permissions within the application. It is utilized to identify the super administrator
     * in various security, authorization, and administrative checks throughout the application's operations.
     */
    private UUID superAdministratorId;

    @Override
    public Page<AccessControl> findAll(final AccessControlType type,
                                       final Map<String, List<String>> immutableFilters,
                                       final QueryFilter queryFilter) {
        Map<String, List<String>> filters = new HashMap<>(immutableFilters);
        filters.put("type", List.of(type.toString()));

        return accessControlRepository.findAll(
                new CustomSpringQueryFilterSpecification<>(AccessControl.class, filters),
                queryFilter.getPageable(true, "name")
        );
    }

    @Override
    public Page<AccessControlDirectDTO> findAll(final AccessControlType type,
                                       final User user,
                                       final Map<String, List<String>> immutableFilters,
                                       final QueryFilter queryFilter) {
        var filters = new HashMap<>(immutableFilters);
        filters.put("type", List.of(type.name()));
        filters.put("userId", List.of(user.getId().toString()));

        return userAccessControlViewRepository.findAll(
                new CustomSpringQueryFilterSpecification<>(UserAccessControlView.class, filters),
                queryFilter.getPageable(true, "name")
        ).map(new UserAccessControlViewToAccessControlDirectDTOFunction());
    }

    @Override
    public Page<AccessControlDirectDTO> findAllChildren(final AccessControlType type,
                                                        final UUID id,
                                                        final AccessControlType childrenType,
                                                        final Map<String, List<String>> immutableFilters,
                                                        final QueryFilter queryFilter) {
        AccessControl accessControl = findById(type, id);
        var filters = new HashMap<>(immutableFilters);
        filters.put("type", List.of(childrenType.name()));
        filters.put("parentId", List.of(accessControl.getId().toString()));

        return accessControlTreeViewRepository.findAll(
                new CustomSpringQueryFilterSpecification<>(AccessControlTreeView.class, filters),
                queryFilter.getPageable(true, "parentName")
        ).map(new AccessControlTreeViewToAccessControlDirectDTOFunction(false));
    }

    @Override
    public Page<User> findAllUsers(final AccessControlType type,
                                   final UUID id,
                                   final Map<String, List<String>> immutableFilters,
                                   final QueryFilter queryFilter) {
        AccessControl accessControl = findById(type, id);
        var filters = new HashMap<>(immutableFilters);
        filters.put("accessControlId", List.of(accessControl.getId().toString()));

        return userAccessControlViewRepository.findAll(
                new CustomSpringQueryFilterSpecification<>(UserAccessControlView.class, filters),
                queryFilter.getPageable(true, "userName")
        ).map(new UserAccesControlViewToUserFunction());
    }

    @Override
    public Page<AccessControlDirectDTO> findAllAccessControls(final AccessControlType type,
                                                              final UUID id,
                                                              final AccessControlType subType,
                                                              final Map<String, List<String>> immutableFilters,
                                                              final QueryFilter queryFilter) {
        AccessControl accessControl = this.findById(type, id);
        var filters = new HashMap<>(immutableFilters);
        filters.put("id", List.of(accessControl.getId().toString()));
        filters.put("parentType", List.of(subType.name()));

        return accessControlTreeViewRepository.findAll(
                new CustomSpringQueryFilterSpecification<>(AccessControlTreeView.class, filters),
                queryFilter.getPageable(true, "parentName")
        ).map(new AccessControlTreeViewToAccessControlDirectDTOFunction());
    }

    @Override
    public AccessControl findById(final AccessControlType type, final UUID id) {
        Map<String, List<String>> filters = Map.of(
            "id",
            List.of(id.toString()),
            "type",
            List.of(type.name())
        );
        return accessControlRepository.findOne(
                new CustomSpringQueryFilterSpecification<>(AccessControl.class, filters))
                .orElseThrow(() -> new ApiException(ErrorType.ENTITY_NOT_FOUND, "id", id.toString()));
    }

    @Override
    public AccessControl create(final AccessControlType type, final AccessControlRecord accessControlRecord) {
        AccessControl accessControl = new BeanMapper<>(AccessControl.class).apply(accessControlRecord);
        accessControl.setType(type);

        return accessControlRepository.save(accessControl);
    }

    @Override
    public AccessControl update(final AccessControlType type,
                                final UUID id,
                                final AccessControlRecord accessControlRecord) {
        AccessControl accessControl = findById(type, id);
        accessControl.setName(accessControlRecord.name());

        return accessControlRepository.save(accessControl);
    }

    @Override
    public void delete(final AccessControlType type, final UUID id) {
        AccessControl accessControl = findById(type, id);
        accessControlRepository.deleteById(accessControl.getId());
    }

    @Override
    public void associate(final AccessControlType parentType,
                          final UUID id,
                          final AccessControlType type,
                          final UUID roleId) {
        AccessControl parentAccessControl = findById(parentType, id);
        AccessControl accessControl = findById(type, roleId);
        Optional<AccessControlTreeView> userAccessControlOptional = accessControlTreeViewRepository
                .findByIdAndParentId(parentAccessControl.getId(), accessControl.getId());

        if (userAccessControlOptional.isPresent()) {
            return;
        }

        AccessControlTree accessControlTree = new AccessControlTree();
        accessControlTree.setParent(parentAccessControl.getId());
        accessControlTree.setCurrent(accessControl.getId());

        accessControlTreeRepository.save(accessControlTree);
    }

    @Override
    public void dissociate(final AccessControlType parentType,
                           final UUID id,
                           final AccessControlType type,
                           final UUID roleId) {
        AccessControl parentAccessControl = findById(parentType, id);
        AccessControl accessControl = findById(type, roleId);
        AccessControlTree accessControlTree = accessControlTreeRepository
                .findByParentAndCurrent(parentAccessControl.getId(), accessControl.getId())
                .orElseThrow(() -> new ApiException(ErrorType.ENTITY_NOT_FOUND, "association"));

        accessControlTreeRepository.delete(accessControlTree);
    }

    @Override
    public void associateUser(final AccessControlType type, final UUID id, final String login) {
        AccessControl accessControl = findById(type, id);
        User user = userService.findByLogin(login);
        Optional<UserAccessControl> userAccessControlOptional = userAccessControlRepository
                .findByAccessControlIdAndUserId(accessControl.getId(), user.getId());

        if (userAccessControlOptional.isPresent()) {
            return;
        }

        UserAccessControl userAccessControl = new UserAccessControl();
        userAccessControl.setAccessControlId(accessControl.getId());
        userAccessControl.setUserId(user.getId());

        userAccessControlRepository.save(userAccessControl);
    }

    @Override
    public void dissociateUser(final AccessControlType type, final UUID id, final String login) {
        AccessControl accessControl = findById(type, id);
        User user = userService.findByLogin(login);
        UserAccessControl userAccessControl = userAccessControlRepository
                .findByAccessControlIdAndUserId(accessControl.getId(), user.getId())
                .orElseThrow(() -> new ApiException(ErrorType.ENTITY_NOT_FOUND, "association"));

        userAccessControlRepository.delete(userAccessControl);
    }

    @Override
    public UUID getSuperAdministratorId() {
        if (superAdministratorId == null) {
            superAdministratorId = accessControlRepository.findByName(Constants.SUPER_ADMINISTRATOR_ROLE_NAME).getId();
        }

        return superAdministratorId;
    }
}
