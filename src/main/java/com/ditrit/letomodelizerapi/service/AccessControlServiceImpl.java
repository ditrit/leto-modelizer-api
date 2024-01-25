package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.BeanMapper;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlRecord;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.persistence.model.AccessControl;
import com.ditrit.letomodelizerapi.persistence.repository.AccessControlRepository;
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
}
