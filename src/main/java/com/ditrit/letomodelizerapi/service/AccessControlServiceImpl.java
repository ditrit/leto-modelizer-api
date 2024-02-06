package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.BeanMapper;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDirectDTO;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlRecord;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.persistence.function.AccessControlTreeViewToAccessControlDirectDTOFunction;
import com.ditrit.letomodelizerapi.persistence.function.UserAccesControlViewToAccessControlFunction;
import com.ditrit.letomodelizerapi.persistence.function.UserAccesControlViewToUserFunction;
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
import com.ditrit.letomodelizerapi.persistence.specification.SpecificationHelper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of the AccessControlService interface.
 *
 * This class provides concrete implementations for the access control management operations defined in
 * AccessControlService.
 * AccessControlServiceImpl interacts with the underlying repository layer to perform these operations,
 * ensuring that business logic and data access are effectively managed.
 */
@Slf4j
@Service
@Transactional
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AccessControlServiceImpl implements AccessControlService {

    /**
     * The AccessControlRepository instance is injected by Spring's dependency injection mechanism.
     * This repository is used for performing database operations related to AccessControl entities, such as querying,
     * saving, and updating access control data.
     */
    private AccessControlRepository accessControlRepository;

    /**
     * The AccessControlTreeRepository instance is injected by Spring's dependency injection mechanism.
     * This repository is used for performing database operations related to AccessControlTree entities, such as
     * querying, saving, and updating access control data.
     */
    private AccessControlTreeRepository accessControlTreeRepository;

    /**
     * The AccessControlTreeViewRepository instance is injected by Spring's dependency injection mechanism.
     * This repository is used for performing database operations related to AccessControlTreeView entities, such as
     * querying, saving, and updating access control data.
     */
    private AccessControlTreeViewRepository accessControlTreeViewRepository;

    /**
     * The UserAccessControlRepository instance is injected by Spring's dependency injection mechanism.
     * This repository is used for performing database operations related to UserAccessControl entities, such as
     * querying, saving, and updating user access control data.
     */
    private UserAccessControlViewRepository userAccessControlViewRepository;

    /**
     * The UserAccessControlRepository instance is injected by Spring's dependency injection mechanism.
     * This repository is used for performing database operations related to UserAccessControl entities, such as
     * querying, saving, and updating user access control data.
     */
    private UserAccessControlRepository userAccessControlRepository;

    /**
     * The UserService instance is injected by Spring's dependency injection mechanism.
     * This service is responsible for handling all user-related business logic and operations.
     * It typically includes functionalities such as user authentication, user data retrieval,
     * user profile updates, and other user-centric business processes.
     */
    private UserService userService;

    @Override
    public Page<AccessControl> findAll(final AccessControlType type,
                                       final Map<String, String> immutableFilters,
                                       final Pageable pageable) {
        Map<String, String> filters = new HashMap<>(immutableFilters);
        filters.put("type", type.name());

        return accessControlRepository.findAll(new SpecificationHelper<>(AccessControl.class, filters), PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))));
    }

    @Override
    public Page<AccessControl> findAll(final AccessControlType type,
                                       final User user,
                                       final Map<String, String> immutableFilters,
                                       final Pageable pageable) {
        Map<String, String> filters = new HashMap<>(immutableFilters);
        filters.put("type", type.name());
        filters.put("userId", user.getId().toString());

        return userAccessControlViewRepository.findAll(
                new SpecificationHelper<>(UserAccessControlView.class, filters),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "accessControlName"))
                )
        ).map(new UserAccesControlViewToAccessControlFunction());
    }

    @Override
    public Page<AccessControlDirectDTO> findAllChildren(final AccessControlType type,
                                                        final Long id,
                                                        final AccessControlType childrenType,
                                                        final Map<String, String> immutableFilters,
                                                        final Pageable pageable) {
        AccessControl accessControl = findById(type, id);
        Map<String, String> filters = new HashMap<>(immutableFilters);
        filters.put("type", childrenType.name());
        filters.put("parentAccessControlId", accessControl.getId().toString());

        return accessControlTreeViewRepository.findAll(
                new SpecificationHelper<>(AccessControlTreeView.class, filters),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "parentAccessControlName"))
                )
        ).map(new AccessControlTreeViewToAccessControlDirectDTOFunction(false));
    }

    @Override
    public Page<User> findAllUsers(final AccessControlType type,
                                   final Long id,
                                   final Map<String, String> immutableFilters,
                                   final Pageable pageable) {
        AccessControl accessControl = findById(type, id);
        Map<String, String> filters = new HashMap<>(immutableFilters);
        filters.put("accessControlId", accessControl.getId().toString());

        return userAccessControlViewRepository.findAll(new SpecificationHelper<>(UserAccessControlView.class, filters),
                PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    pageable.getSortOr(Sort.by(Sort.Direction.ASC, "userName")))
        ).map(new UserAccesControlViewToUserFunction());
    }

    @Override
    public Page<AccessControlDirectDTO> findAllAccessControls(final Long id,
                                                              final AccessControlType type,
                                                              final Map<String, String> immutableFilters,
                                                              final Pageable pageable) {
        Map<String, String> filters = new HashMap<>(immutableFilters);
        filters.put("accessControlId", id.toString());
        filters.put("parentAccessControlType", type.name());

        return accessControlTreeViewRepository.findAll(new SpecificationHelper<>(AccessControlTreeView.class, filters),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "parentAccessControlName")))
        ).map(new AccessControlTreeViewToAccessControlDirectDTOFunction());
    }

    @Override
    public AccessControl findById(final AccessControlType type, final Long id) {
        Map<String, String> filters = Map.of("id", id.toString(), "type", type.name());
        return accessControlRepository.findOne(new SpecificationHelper<>(AccessControl.class, filters))
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
                                final Long id,
                                final AccessControlRecord accessControlRecord) {
        AccessControl accessControl = findById(type, id);
        accessControl.setName(accessControlRecord.name());

        return accessControlRepository.save(accessControl);
    }

    @Override
    public void delete(final AccessControlType type, final Long id) {
        AccessControl accessControl = findById(type, id);
        accessControlRepository.deleteById(accessControl.getId());
    }

    @Override
    public void associate(final AccessControlType parentType,
                          final Long id,
                          final AccessControlType type,
                          final Long roleId) {
        AccessControl parentAccessControl = findById(parentType, id);
        AccessControl accessControl = findById(type, roleId);
        Optional<AccessControlTreeView> userAccessControlOptional = accessControlTreeViewRepository
                .findByAccessControlIdAndParentAccessControlId(parentAccessControl.getId(), accessControl.getId());

        if (userAccessControlOptional.isPresent()) {
            throw new ApiException(ErrorType.ENTITY_ALREADY_EXISTS, "association");
        }

        AccessControlTree accessControlTree = new AccessControlTree();
        accessControlTree.setParent(parentAccessControl.getId());
        accessControlTree.setCurrent(accessControl.getId());

        accessControlTreeRepository.save(accessControlTree);
    }

    @Override
    public void dissociate(final AccessControlType parentType,
                           final Long id,
                           final AccessControlType type,
                           final Long roleId) {
        AccessControl parentAccessControl = findById(parentType, id);
        AccessControl accessControl = findById(type, roleId);
        AccessControlTree accessControlTree = accessControlTreeRepository
                .findByParentAndCurrent(parentAccessControl.getId(), accessControl.getId())
                .orElseThrow(() -> new ApiException(ErrorType.ENTITY_NOT_FOUND, "association"));

        accessControlTreeRepository.delete(accessControlTree);
    }

    @Override
    public void associateUser(final AccessControlType type, final Long id, final String login) {
        AccessControl accessControl = findById(type, id);
        User user = userService.findByLogin(login);
        Optional<UserAccessControl> userAccessControlOptional = userAccessControlRepository
                .findByAccessControlIdAndUserId(accessControl.getId(), user.getId());

        if (userAccessControlOptional.isPresent()) {
            throw new ApiException(ErrorType.ENTITY_ALREADY_EXISTS, "association");
        }

        UserAccessControl userAccessControl = new UserAccessControl();
        userAccessControl.setAccessControlId(accessControl.getId());
        userAccessControl.setUserId(user.getId());

        userAccessControlRepository.save(userAccessControl);
    }

    @Override
    public void dissociateUser(final AccessControlType type, final Long id, final String login) {
        AccessControl accessControl = findById(type, id);
        User user = userService.findByLogin(login);
        UserAccessControl userAccessControl = userAccessControlRepository
                .findByAccessControlIdAndUserId(accessControl.getId(), user.getId())
                .orElseThrow(() -> new ApiException(ErrorType.ENTITY_NOT_FOUND, "association"));

        userAccessControlRepository.delete(userAccessControl);
    }
}
