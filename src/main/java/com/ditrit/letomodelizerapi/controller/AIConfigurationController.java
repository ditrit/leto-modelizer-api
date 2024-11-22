package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.model.BeanMapper;
import com.ditrit.letomodelizerapi.model.ai.AIConfigurationDTO;
import com.ditrit.letomodelizerapi.model.ai.AIConfigurationRecord;
import com.ditrit.letomodelizerapi.model.ai.UpdateMultipleAIConfigurationRecord;
import com.ditrit.letomodelizerapi.model.permission.ActionPermission;
import com.ditrit.letomodelizerapi.model.permission.EntityPermission;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.AIConfigurationService;
import com.ditrit.letomodelizerapi.service.AISecretService;
import com.ditrit.letomodelizerapi.service.AIService;
import com.ditrit.letomodelizerapi.service.UserPermissionService;
import com.ditrit.letomodelizerapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST Controller for managing ai and configurations.
 * Provides endpoints for CRUD operations on roles, including listing, retrieving, creating, updating, and deleting
 * roles.
 * Only accessible by users with administrative permissions.
 */
@Slf4j
@RestController
@RequestMapping("/ai/configurations")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AIConfigurationController implements DefaultController {

    /**
     * Service to manage AI configuration.
     */
    private final AIConfigurationService aiConfigurationService;

    /**
     * Service to manage AI secrets.
     */
    private final AISecretService aiSecretService;

    /**
     * Service to manage AI configuration.
     */
    private final AIService aiService;

    /**
     * Service to manage user.
     */
    private final UserService userService;

    /**
     * Service to manage user permissions.
     */
    private final UserPermissionService userPermissionService;

    /**
     * Retrieves the scopes of a specified role.
     *
     * <p>This method processes a GET request to obtain scopes associated with a given role ID. It filters the scopes
     * based on the provided query parameters and pagination settings.
     *
     * @param request     the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param filters     All query parameters for filtering results.
     * @param queryFilter bean parameter encapsulating filtering and pagination criteria.
     * @return a Response object containing the requested page of AIConfigurationDTO objects representing the
     * configurations. The status of the response can vary based on the outcome of the request.
     */
    @GetMapping
    public ResponseEntity<Page<AIConfigurationDTO>> getAllConfigurations(
            final HttpServletRequest request,
            final @RequestParam MultiValueMap<String, String> filters,
            final @ModelAttribute QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkPermission(user, "id", EntityPermission.AI_CONFIGURATION, ActionPermission.ACCESS);

        log.info("[{}] Received GET request to get configurations with the following filters: {}", user.getLogin(),
                filters);

        var resources = aiConfigurationService.findAll(filters, queryFilter)
                .map(new BeanMapper<>(AIConfigurationDTO.class));

        return ResponseEntity.status(this.getStatus(resources)).body(resources);
    }

    /**
     * Get configuration by id.
     *
     * @param request     the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id          the ID of the configuration to retrieve. Must be a valid and non-null UUID value.
     * @return a Response object containing theAIConfigurationDTO object representing the configuration.
     * The status of the response can vary based on the outcome of the request.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AIConfigurationDTO> getConfigurationById(final HttpServletRequest request,
                                                                   final @PathVariable UUID id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkPermission(user, "id", EntityPermission.AI_CONFIGURATION, ActionPermission.ACCESS);

        log.info("[{}] Received GET request to get configuration {}", user.getLogin(), id);

        var aiConfiguration = aiConfigurationService.findById(id);

        return ResponseEntity.ok(new BeanMapper<>(AIConfigurationDTO.class).apply(aiConfiguration));
    }

    /**
     * Create a configuration.
     *
     * <p>This method handles a POST request to create a configuration.
     * It validates the user's session and ensures the user has administrative privileges before proceeding with the
     * association.
     *
     * @param request the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param aiConfigurationRecord the record containing the details of the configuration to be created,
     *                                  validated for correctness.
     * @return a Response object indicating the outcome of the configuration creation. A successful operation returns
     * a status of CREATED.
     */
    @PostMapping
    public ResponseEntity<AIConfigurationDTO> createConfiguration(
            final HttpServletRequest request,
            final @RequestBody @Valid AIConfigurationRecord aiConfigurationRecord) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkPermission(user, null, EntityPermission.AI_CONFIGURATION, ActionPermission.CREATE);

        log.info("[{}] Received POST request to create configuration with key {}", user.getLogin(),
                aiConfigurationRecord.key());
        var aiConfiguration = aiConfigurationService.create(aiConfigurationRecord);

        var configuration = aiSecretService.generateConfiguration();

        aiService.sendConfiguration(configuration);

        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(new BeanMapper<>(AIConfigurationDTO.class).apply(aiConfiguration));
    }

    /**
     * Update multiple configurations.
     *
     * <p>This method handles a PUT request to update multiple configurations.
     * It validates the user's session and ensures the user has administrative privileges before proceeding with the
     * association.
     *
     * @param request the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param aiConfigurationRecords the record containing list of configurations to be updated,
     *                               validated for correctness.
     * @return a Response object indicating the outcome of configurations update. A successful operation returns
     * a status of OK.
     */
    @PutMapping
    public ResponseEntity<List<AIConfigurationDTO>> updateConfiguration(
            final HttpServletRequest request,
            final @RequestBody @Valid List<UpdateMultipleAIConfigurationRecord> aiConfigurationRecords) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkPermission(user, "id", EntityPermission.AI_CONFIGURATION, ActionPermission.UPDATE);

        log.info("[{}] Received PUT request to update configurations {}", user.getLogin(),
                aiConfigurationRecords.stream()
                        .map(UpdateMultipleAIConfigurationRecord::id)
                        .map(UUID::toString)
                        .collect(Collectors.joining(",")));

        List<AIConfigurationDTO> configurations = new ArrayList<>();

        aiConfigurationRecords.forEach(aiConfigurationRecord -> {

            var aiConfiguration = aiConfigurationService.update(aiConfigurationRecord.id(), new AIConfigurationRecord(
                    aiConfigurationRecord.handler(),
                    aiConfigurationRecord.key(),
                    aiConfigurationRecord.value()
                    ));

            configurations.add(new BeanMapper<>(AIConfigurationDTO.class).apply(aiConfiguration));
        });

        var configuration = aiSecretService.generateConfiguration();

        aiService.sendConfiguration(configuration);

        return ResponseEntity.ok(configurations);
    }

    /**
     * Update a configuration.
     *
     * <p>This method handles a PUT request to update a configuration.
     * It validates the user's session and ensures the user has administrative privileges before proceeding with the
     * association.
     *
     * @param request the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id      the ID of the configuration . Must be a valid and non-null UUID value.
     * @param aiConfigurationRecord the record containing the details of the configuration to be updated,
     *                                  validated for correctness.
     * @return a Response object indicating the outcome of the configuration update. A successful operation returns
     * a status of OK.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AIConfigurationDTO> updateConfiguration(
            final HttpServletRequest request,
            final @PathVariable UUID id,
            final @RequestBody @Valid AIConfigurationRecord aiConfigurationRecord) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkPermission(user, "id", EntityPermission.AI_CONFIGURATION, ActionPermission.UPDATE);

        log.info("[{}] Received PUT request to update configuration {}", user.getLogin(), id.toString());

        var aiConfiguration = aiConfigurationService.update(id, aiConfigurationRecord);
        var configuration = aiSecretService.generateConfiguration();

        aiService.sendConfiguration(configuration);

        return ResponseEntity.ok(new BeanMapper<>(AIConfigurationDTO.class).apply(aiConfiguration));
    }

    /**
     * Delete a configuration.
     *
     * <p>This method facilitates the handling of a DELETE request to delete a configuration identified by its
     * respective ID.
     * The operation is secured, requiring validation of the user's session and administrative privileges.
     *
     * @param request the HttpServletRequest used to validate the user's session.
     * @param id      the ID of the configuration . Must be a valid and non-null UUID value.
     * @return a Response object with a status indicating the outcome of the deletion operation. A successful operation
     * returns a status of NO_CONTENT.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteConfiguration(final HttpServletRequest request,
                                                      final @PathVariable UUID id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkPermission(user, "id", EntityPermission.AI_CONFIGURATION, ActionPermission.DELETE);

        log.info("[{}] Received DELETE request to delete configuration {}", user.getLogin(), id);
        aiConfigurationService.findById(id);
        var configuration = aiSecretService.generateConfiguration();

        aiService.sendConfiguration(configuration);

        aiConfigurationService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).contentType(MediaType.APPLICATION_JSON).build();
    }
}
