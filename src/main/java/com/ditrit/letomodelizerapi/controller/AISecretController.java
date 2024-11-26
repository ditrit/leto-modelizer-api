package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.model.BeanMapper;
import com.ditrit.letomodelizerapi.model.ai.AISecretDTO;
import com.ditrit.letomodelizerapi.model.ai.AISecretRecord;
import com.ditrit.letomodelizerapi.model.permission.ActionPermission;
import com.ditrit.letomodelizerapi.model.permission.EntityPermission;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.AISecretService;
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

import java.util.UUID;

/**
 * REST Controller for managing ai and secrets.
 * Provides endpoints for CRUD operations on roles, including listing, retrieving, creating, updating, and deleting
 * roles.
 * Only accessible by users with administrative permissions.
 */
@Slf4j
@RestController
@RequestMapping("/ai/secrets")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AISecretController implements DefaultController {

    /**
     * Service to manage ai secret.
     */
    private final AISecretService aiSecretService;

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
     * @param queryFilter the filter criteria and pagination information.
     * @return a Response object containing the requested page of AISecretDTO objects representing the
     * secrets. The status of the response can vary based on the outcome of the request.
     */
    @GetMapping
    public ResponseEntity<Page<AISecretDTO>> getAllSecrets(final HttpServletRequest request,
                                                           final @RequestParam MultiValueMap<String, String> filters,
                                                           final @ModelAttribute QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkPermission(user, "id", EntityPermission.AI_SECRET, ActionPermission.ACCESS);

        log.info("[{}] Received GET request to get secrets with the following filters: {}", user.getLogin(), filters);

        var resources = aiSecretService.findAll(filters, queryFilter)
                .map(new BeanMapper<>(AISecretDTO.class));

        return ResponseEntity.status(this.getStatus(resources)).body(resources);
    }

    /**
     * Get secret by id.
     *
     * @param request     the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id          the ID of the secret to retrieve. Must be a valid and non-null UUID value.
     * @return a Response object containing theAISecretDTO object representing the secret.
     * The status of the response can vary based on the outcome of the request.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AISecretDTO> getSecretById(final HttpServletRequest request,
                                                     final @PathVariable UUID id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkPermission(user, "id", EntityPermission.AI_SECRET, ActionPermission.ACCESS);

        log.info("[{}] Received GET request to get secret {}", user.getLogin(), id);

        var aiSecret = aiSecretService.findById(id);

        return ResponseEntity.ok(new BeanMapper<>(AISecretDTO.class).apply(aiSecret));
    }

    /**
     * Create a secret.
     *
     * <p>This method handles a POST request to create a secret.
     * It validates the user's session and ensures the user has administrative privileges before proceeding with the
     * association.
     *
     * @param request the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param aiSecretRecord the record containing the details of the secret to be created,
     *                                  validated for correctness.
     * @return a Response object indicating the outcome of the secret creation. A successful operation returns
     * a status of CREATED.
     */
    @PostMapping
    public ResponseEntity<AISecretDTO> createSecret(final HttpServletRequest request,
                                                    final @RequestBody @Valid AISecretRecord aiSecretRecord) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkPermission(user, null, EntityPermission.AI_SECRET, ActionPermission.CREATE);

        log.info("[{}] Received POST request to create secret with key {}", user.getLogin(),
                aiSecretRecord.key());
        var aiSecret = aiSecretService.create(aiSecretRecord);

        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(new BeanMapper<>(AISecretDTO.class).apply(aiSecret));
    }

    /**
     * Update a secret.
     *
     * <p>This method handles a PUT request to update a secret.
     * It validates the user's session and ensures the user has administrative privileges before proceeding with the
     * association.
     *
     * @param request the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id      the ID of the secret . Must be a valid and non-null UUID value.
     * @param aiSecretRecord the record containing the details of the secret to be updated,
     *                                  validated for correctness.
     * @return a Response object indicating the outcome of the secret update. A successful operation returns
     * a status of OK.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AISecretDTO> updateSecret(final HttpServletRequest request,
                                                    final @PathVariable UUID id,
                                                    final @RequestBody @Valid AISecretRecord aiSecretRecord) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkPermission(user, "id", EntityPermission.AI_SECRET, ActionPermission.UPDATE);

        log.info("[{}] Received PUT request to update secret {}", user.getLogin(), id.toString());
        var aiSecret = aiSecretService.update(id, aiSecretRecord);

        return ResponseEntity.status(HttpStatus.OK.value())
                .body(new BeanMapper<>(AISecretDTO.class).apply(aiSecret));
    }

    /**
     * Delete a secret.
     *
     * <p>This method facilitates the handling of a DELETE request to delete a secret identified by its respective ID.
     * The operation is secured, requiring validation of the user's session and administrative privileges.
     *
     * @param request the HttpServletRequest used to validate the user's session.
     * @param id      the ID of the secret . Must be a valid and non-null UUID value.
     * @return a Response object with a status indicating the outcome of the deletion operation. A successful operation
     * returns a status of NO_CONTENT.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSecret(final HttpServletRequest request,
                                               final @PathVariable UUID id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkPermission(user, "id", EntityPermission.AI_SECRET, ActionPermission.DELETE);

        log.info("[{}] Received DELETE request to delete secret {}", user.getLogin(), id);
        aiSecretService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).contentType(MediaType.APPLICATION_JSON).build();
    }
}
