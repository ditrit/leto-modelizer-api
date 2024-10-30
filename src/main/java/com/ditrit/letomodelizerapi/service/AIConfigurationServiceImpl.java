package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.ai.AIConfigurationRecord;
import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.persistence.model.AIConfiguration;
import com.ditrit.letomodelizerapi.persistence.repository.AIConfigurationRepository;
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

import java.util.Map;
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
public class AIConfigurationServiceImpl implements AIConfigurationService {

    /**
     * The AIConfigurationRepository instance is injected by Spring's dependency injection mechanism.
     * This repository is used for performing database operations related to AIConfiguration entities,
     * such as querying, saving, and updating access control data.
     */
    private final AIConfigurationRepository aiConfigurationRepository;

    @Override
    public Page<AIConfiguration> findAll(final Map<String, String> filters, final Pageable pageable) {
        return aiConfigurationRepository.findAll(new SpecificationHelper<>(AIConfiguration.class, filters),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "key"))
                )
        );
    }

    @Override
    public AIConfiguration findById(final UUID id) {
        return aiConfigurationRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorType.ENTITY_NOT_FOUND, "id", id.toString()));
    }

    @Override
    public AIConfiguration create(final AIConfigurationRecord aiConfigurationRecord) {
        if (aiConfigurationRepository.existsByHandlerAndKey(aiConfigurationRecord.handler(),
                aiConfigurationRecord.key())) {
            throw new ApiException(ErrorType.ENTITY_ALREADY_EXISTS, "key", aiConfigurationRecord.key());
        }

        var aiConfiguration = new AIConfiguration();
        aiConfiguration.setHandler(aiConfigurationRecord.handler());
        aiConfiguration.setKey(aiConfigurationRecord.key());
        aiConfiguration.setValue(aiConfigurationRecord.value());

        return aiConfigurationRepository.save(aiConfiguration);
    }

    @Override
    public AIConfiguration update(final UUID id, final AIConfigurationRecord aiConfigurationRecord) {
        AIConfiguration aiConfiguration = findById(id);

        aiConfiguration.setValue(aiConfigurationRecord.value());

        return aiConfigurationRepository.save(aiConfiguration);
    }

    @Override
    public void delete(final UUID id) {
        AIConfiguration aiConfiguration = findById(id);

        aiConfigurationRepository.delete(aiConfiguration);
    }
}
