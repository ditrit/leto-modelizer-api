package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.model.BeanMapper;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDTO;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDirectDTO;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlRecord;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import com.ditrit.letomodelizerapi.model.permission.PermissionDirectDTO;
import com.ditrit.letomodelizerapi.model.user.UserDTO;
import com.ditrit.letomodelizerapi.persistence.function.AccessControlPermissionViewToPermissionDirectDTOFunction;
import com.ditrit.letomodelizerapi.persistence.model.AccessControl;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.AccessControlPermissionService;
import com.ditrit.letomodelizerapi.service.AccessControlService;
import com.ditrit.letomodelizerapi.service.UserPermissionService;
import com.ditrit.letomodelizerapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
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
 * REST Controller for managing scope.
 * Provides endpoints for CRUD operations on scopes, including listing, retrieving, creating, updating, and deleting
 * scopes.
 * Only accessible by users with administrative permissions.
 */
@Slf4j
@RestController
@RequestMapping("/scopes")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ScopeController implements DefaultController {

    /**
     * Service to manage user.
     */
    private UserService userService;

    /**
     * Service to manage user permissions.
     */
    private UserPermissionService userPermissionService;

    /**
     * Service to manage access controls.
     */
    private AccessControlService accessControlService;

    /**
     * Service to manage permissions of access controls.
     */
    private AccessControlPermissionService accessControlPermissionService;

    /**
     * Finds and returns all scopes based on the provided query filters.
     *
     * @param request      HttpServletRequest to access the HTTP session
     * @param filters     All query parameters for filtering results.
     * @param queryFilter the filter criteria and pagination information.
     * @return a Response containing a page of AccessControlDTO objects
     */
    @GetMapping
    public ResponseEntity<Page<AccessControlDTO>> findAll(final HttpServletRequest request,
                                                          final @RequestParam MultiValueMap<String, String> filters,
                                                          final @ModelAttribute QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received GET request to get scopes with the following filters: {}", user.getLogin(), filters);
        Page<AccessControlDTO> resources = accessControlService
                .findAll(AccessControlType.SCOPE, filters, queryFilter)
                .map(new BeanMapper<>(AccessControlDTO.class));

        return ResponseEntity.status(this.getStatus(resources)).body(resources);
    }

    /**
     * Retrieves a specific scope by its ID.
     *
     * @param request HttpServletRequest to access the HTTP session
     * @param id      the ID of the scope to retrieve
     * @return a Response containing the AccessControlDTO of the requested scope
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccessControlDTO> getScopeById(final HttpServletRequest request,
                                                         final @PathVariable UUID id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received GET request to get scope with id {}", user.getLogin(), id);
        AccessControlDTO accessControlDTO = new BeanMapper<>(AccessControlDTO.class)
                .apply(accessControlService.findById(AccessControlType.SCOPE, id));

        return ResponseEntity.ok(accessControlDTO);
    }

    /**
     * Creates a new scope with the given details.
     *
     * @param request              HttpServletRequest to access the HTTP session
     * @param accessControlRecord  Data for creating the new scope
     * @return a Response indicating the outcome of the creation operation
     */
    @PostMapping
    public ResponseEntity<AccessControlDTO> createScope(
            final HttpServletRequest request,
            final @RequestBody @Valid AccessControlRecord accessControlRecord) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received POST request to create a scope with {}", user.getLogin(), accessControlRecord);
        AccessControlDTO accessControlDTO = new BeanMapper<>(AccessControlDTO.class)
                .apply(accessControlService.create(AccessControlType.SCOPE, accessControlRecord));

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(accessControlDTO);
    }

    /**
     * Updates an existing scope identified by the given ID with the provided details.
     *
     * @param request              HttpServletRequest to access the HTTP session
     * @param id                   ID of the scope to update
     * @param accessControlRecord  Data for updating the scope
     * @return a Response indicating the outcome of the update operation
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccessControlDTO> updateScope(
            final HttpServletRequest request,
            final @PathVariable UUID id,
            final @RequestBody @Valid AccessControlRecord accessControlRecord) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info(
                "[{}] Received PUT request to update a scope with id {} with {}",
                user.getLogin(),
                id,
                accessControlRecord
        );

        AccessControlDTO accessControlDTO = new BeanMapper<>(AccessControlDTO.class)
                .apply(accessControlService.update(AccessControlType.SCOPE, id, accessControlRecord));

        return ResponseEntity.ok(accessControlDTO);
    }

    /**
     * Deletes the scope with the specified ID.
     *
     * @param request HttpServletRequest to access the HTTP session
     * @param id      the ID of the scope to delete
     * @return a Response indicating the outcome of the delete operation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteScope(final HttpServletRequest request,
                                final @PathVariable UUID id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received DELETE request to delete scope with id {}", user.getLogin(), id);
        accessControlService.delete(AccessControlType.SCOPE, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).contentType(MediaType.APPLICATION_JSON).build();
    }

    /**
     * Retrieves users associated to a specific scope.
     * This endpoint fetches and returns a paginated list of users associated with the given scope ID.
     * The method is accessible only to users with administrative permissions.
     *
     * @param request     HttpServletRequest to access the HTTP session for authentication and authorization.
     * @param id          the ID of the scope for which users are to be retrieved.
     * @param filters     All query parameters for filtering results.
     * @param queryFilter the filter criteria and pagination information.
     * @return a Response containing a paginated list of UserDTO objects associated with the scope.
     */
    @GetMapping("/{id}/users")
    public ResponseEntity<Page<UserDTO>> getUsersByScope(final HttpServletRequest request,
                                                         final @PathVariable UUID id,
                                                         final @RequestParam MultiValueMap<String, String> filters,
                                                         final @ModelAttribute QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info(
                "[{}] Received GET request to get users of scope {} with the following filters: {}",
                user.getLogin(),
                id,
                filters
        );

        Page<UserDTO> resources = accessControlService
                .findAllUsers(AccessControlType.SCOPE, id, filters, queryFilter)
                .map(new BeanMapper<>(UserDTO.class));

        return ResponseEntity.status(this.getStatus(resources)).body(resources);
    }

    /**
     * Associates a user to a specified scope.
     * This endpoint allows an administrative user to associate another user, identified by their login,
     * with a specific scope, identified by its ID.
     *
     * @param request HttpServletRequest to access the HTTP session for authentication and authorization.
     * @param id      the ID of the scope to which the user is to be associated.
     * @param login   the login identifier of the user to associate with the scope.
     * @return a Response indicating the outcome of the association operation.
     */
    @PostMapping("/{id}/users")
    public ResponseEntity<Object> associateUser(final HttpServletRequest request,
                                                final @PathVariable UUID id,
                                                final @RequestBody @Valid @NotBlank String login) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received POST request to associate scope {} with user {}", user.getLogin(), id, login);
        accessControlService.associateUser(AccessControlType.SCOPE, id, login);

        return ResponseEntity.status(HttpStatus.CREATED.value()).contentType(MediaType.APPLICATION_JSON).build();
    }

    /**
     * Dissociates a user from a specified scope.
     * This endpoint allows an administrative user to remove the association of a user, identified by their login,
     * from a specific scope, identified by its ID.
     *
     * @param request HttpServletRequest to access the HTTP session for authentication and authorization.
     * @param id      the ID of the scope from which the user is to be dissociated.
     * @param login   the login identifier of the user to dissociate from the scope.
     * @return a Response indicating the outcome of the dissociation operation.
     */
    @DeleteMapping("/{id}/users/{login}")
    public ResponseEntity<Object> dissociateUser(final HttpServletRequest request,
                                                 final @PathVariable UUID id,
                                                 final @PathVariable String login) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received DELETE request to dissociate scope {} with user {}", user.getLogin(), id, login);
        accessControlService.dissociateUser(AccessControlType.SCOPE, id, login);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).contentType(MediaType.APPLICATION_JSON).build();
    }

    /**
     * Retrieves the groups of a specified scope.
     *
     * <p>This method processes a GET request to obtain groups associated with a given scope ID. It filters the
     * scopes based on the provided query parameters and pagination settings.
     *
     * @param request     the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id          the ID of the scope for which to retrieve sub-scopes. Must be a valid and non-null Long value.
     * @param filters     All query parameters for filtering results.
     * @param queryFilter the filter criteria and pagination information.
     * @return a Response object containing the requested page of AccessControlDirectDTO objects representing the
     * groups of the specified scope. The status of the response can vary based on the outcome of the request.
     */
    @GetMapping("/{id}/groups")
    public ResponseEntity<Page<AccessControlDirectDTO>> getGroupsOfScope(
            final HttpServletRequest request,
            final @PathVariable UUID id,
            final @RequestParam MultiValueMap<String, String> filters,
            final @ModelAttribute QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info(
                "[{}] Received GET request to get groups of scope {} with the following filters: {}",
                user.getLogin(),
                id,
                filters
        );

        Page<AccessControlDirectDTO> resources = accessControlService
                .findAllAccessControls(
                        AccessControlType.SCOPE,
                        id,
                        AccessControlType.GROUP,
                        filters,
                        queryFilter
                );

        return ResponseEntity.status(this.getStatus(resources)).body(resources);
    }

    /**
     * Associates a group with a scope.
     *
     * <p>This method handles a POST request to create an association between two groups, specified by their IDs.
     * It validates the user's session and ensures the user has administrative privileges before proceeding with the
     * association.
     *
     * @param request the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id the ID of the scope to which the group will be associated. Must be a valid and non-nul Long value.
     * @param groupId the ID of the group to be associated with the scope. Must be a valid and non-null Long value.
     * @return a Response object indicating the outcome of the association operation. A successful operation returns
     * a status of CREATED.
     */
    @PostMapping("/{id}/groups")
    public ResponseEntity<Object> associate(final HttpServletRequest request,
                                            final @PathVariable UUID id,
                                            final @RequestBody @Valid @NotNull String groupId) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received POST request to associate scope {} with group {}", user.getLogin(), id, groupId);
        accessControlService.associate(
                AccessControlType.SCOPE,
                id,
                AccessControlType.GROUP,
                UUID.fromString(groupId)
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).contentType(MediaType.APPLICATION_JSON).build();
    }

    /**
     * Dissociates a group from a scope.
     *
     * <p>This method facilitates the handling of a DELETE request to remove the association between group and scope,
     * identified by their respective IDs. The operation is secured, requiring validation of the user's session and
     * administrative privileges.
     *
     * @param request the HttpServletRequest used to validate the user's session.
     * @param id the ID of the scope from which the group will be dissociated. Must be a valid and non-null Long value.
     * @param groupId the ID of the second scope to be dissociated from the first scope. Must be a valid and non-null
     *                Long value.
     * @return a Response object with a status indicating the outcome of the dissociation operation. A successful
     * operation returns a status of NO_CONTENT.
     */
    @DeleteMapping("/{id}/groups/{groupId}")
    public ResponseEntity<Object> dissociate(final HttpServletRequest request,
                                             final @PathVariable UUID id,
                                             final @PathVariable UUID groupId) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info(
                "[{}] Received DELETE request to dissociate scope {} with group {}",
                user.getLogin(),
                id,
                groupId
        );

        accessControlService.dissociate(AccessControlType.SCOPE, id, AccessControlType.SCOPE, groupId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).contentType(MediaType.APPLICATION_JSON).build();
    }

    /**
     * Retrieves the roles associated with a specific scope, identified by the scope's ID. This method processes a GET
     * request and returns a paginated list of AccessControlDirectDTO objects representing the roles. It supports
     * filtering based on query parameters to allow for refined searches within the scope's associated roles.
     *
     * <p>This endpoint is secured to ensure that only users with administrative permissions can access role information
     * for a scope. It is particularly useful for managing and reviewing the roles and permissions assigned to a scope
     * within an access control system.
     *
     * @param request     The HttpServletRequest providing context to access the HTTP session.
     * @param id          The unique identifier of the scope whose roles are being retrieved.
     * @param filters     All query parameters for filtering results.
     * @param queryFilter the filter criteria and pagination information.
     * @return A Response object containing a paginated list of AccessControlDirectDTO objects representing the roles
     *         associated with the specified scope. The response includes pagination details and adheres to the
     *         HTTP status code conventions to indicate the outcome of the request.
     */
    @GetMapping("/{id}/roles")
    public ResponseEntity<Page<AccessControlDirectDTO>> getRolesOfScope(
            final HttpServletRequest request,
            final @PathVariable UUID id,
            final @RequestParam MultiValueMap<String, String> filters,
            final @ModelAttribute QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info(
                "[{}] Received GET request to get roles of scope {} with the following filters: {}",
                user.getLogin(),
                id,
                filters
        );

        Page<AccessControlDirectDTO> resources = accessControlService
                .findAllAccessControls(
                        AccessControlType.SCOPE,
                        id,
                        AccessControlType.ROLE,
                        filters,
                        queryFilter
                );

        return ResponseEntity.status(this.getStatus(resources)).body(resources);
    }

    /**
     * Retrieves the permissions of a specified scope.
     *
     * <p>This method processes a GET request to obtain permissions associated with a given scope ID. It filters the
     * permissions based on the provided query parameters and pagination settings.
     *
     * @param request     the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id          the ID of the scopes for which to retrieve permissions. Must be a valid and non-null Long
     *                    value.
     * @param filters     All query parameters for filtering results.
     * @param queryFilter the filter criteria and pagination information.
     * @return a Response object containing the requested page of PermissionDirectDTO objects representing the
     * permissions of the specified scope. The status of the response can vary based on the outcome of the request.
     */
    @GetMapping("/{id}/permissions")
    public ResponseEntity<Page<PermissionDirectDTO>> getPermissionsOfScope(
            final HttpServletRequest request,
            final @PathVariable UUID id,
            final @RequestParam MultiValueMap<String, String> filters,
            final @ModelAttribute QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info(
                "[{}] Received GET request to get permissions of scope {} with the following filters: {}",
                user.getLogin(),
                id,
                filters
        );

        AccessControl accessControl = accessControlService.findById(AccessControlType.SCOPE, id);
        Page<PermissionDirectDTO> resources = accessControlPermissionService
                .findAll(accessControl.getId(), filters, queryFilter)
                .map(new AccessControlPermissionViewToPermissionDirectDTOFunction());


        return ResponseEntity.status(this.getStatus(resources)).body(resources);
    }
}
