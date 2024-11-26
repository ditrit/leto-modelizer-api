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
 * REST Controller for managing groups.
 * Provides endpoints for CRUD operations on groups, including listing, retrieving, creating, updating, and deleting
 * groups.
 * Only accessible by users with administrative permissions.
 */
@Slf4j
@RestController
@RequestMapping("/groups")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class GroupController implements DefaultController {

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
     * Finds and returns all groups based on the provided query filters.
     *
     * @param request     HttpServletRequest to access the HTTP session
     * @param filters All query parameters for filtering results.
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

        log.info("[{}] Received GET request to get groups with the following filters: {}", user.getLogin(), filters);
        Page<AccessControlDTO> resources = accessControlService
                .findAll(AccessControlType.GROUP, filters, queryFilter)
                .map(new BeanMapper<>(AccessControlDTO.class));

        return ResponseEntity.status(this.getStatus(resources)).body(resources);
    }

    /**
     * Retrieves a specific group by its ID.
     *
     * @param request HttpServletRequest to access the HTTP session
     * @param id      the ID of the group to retrieve
     * @return a Response containing the AccessControlDTO of the requested group
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccessControlDTO> getGroupById(final HttpServletRequest request,
                                                         final @PathVariable UUID id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received GET request to get group with id {}", user.getLogin(), id);
        AccessControlDTO accessControlDTO = new BeanMapper<>(AccessControlDTO.class)
                .apply(accessControlService.findById(AccessControlType.GROUP, id));

        return ResponseEntity.ok(accessControlDTO);
    }

    /**
     * Creates a new group with the given details.
     *
     * @param request             HttpServletRequest to access the HTTP session
     * @param accessControlRecord Data for creating the new group
     * @return a Response indicating the outcome of the creation operation
     */
    @PostMapping
    public ResponseEntity<AccessControlDTO> createGroup(
            final HttpServletRequest request,
            final @RequestBody @Valid AccessControlRecord accessControlRecord) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received POST request to create a group with {}", user.getLogin(), accessControlRecord);
        AccessControlDTO accessControlDTO = new BeanMapper<>(AccessControlDTO.class)
                .apply(accessControlService.create(AccessControlType.GROUP, accessControlRecord));

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(accessControlDTO);
    }

    /**
     * Updates an existing group identified by the given ID with the provided details.
     *
     * @param request             HttpServletRequest to access the HTTP session
     * @param id                  ID of the group to update
     * @param accessControlRecord Data for updating the group
     * @return a Response indicating the outcome of the update operation
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccessControlDTO> updateGroup(
            final HttpServletRequest request,
            final @PathVariable UUID id,
            final @RequestBody @Valid AccessControlRecord accessControlRecord) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info(
                "[{}] Received PUT request to update a group with id {} with {}",
                user.getLogin(),
                id,
                accessControlRecord
        );

        AccessControlDTO accessControlDTO = new BeanMapper<>(AccessControlDTO.class)
                .apply(accessControlService.update(AccessControlType.GROUP, id, accessControlRecord));

        return ResponseEntity.ok(accessControlDTO);
    }

    /**
     * Deletes the group with the specified ID.
     *
     * @param request HttpServletRequest to access the HTTP session
     * @param id      the ID of the group to delete
     * @return a Response indicating the outcome of the delete operation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteGroup(final HttpServletRequest request,
                                              final @PathVariable UUID id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received DELETE request to delete group with id {}", user.getLogin(), id);
        accessControlService.delete(AccessControlType.GROUP, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).contentType(MediaType.APPLICATION_JSON).build();
    }

    /**
     * Retrieves users associated to a specific group.
     * This endpoint fetches and returns a paginated list of users associated with the given group ID.
     * The method is accessible only to users with administrative permissions.
     *
     * @param request     HttpServletRequest to access the HTTP session for authentication and authorization.
     * @param id          the ID of the group for which users are to be retrieved.
     * @param filters All query parameters for filtering results.
     * @param queryFilter the filter criteria and pagination information.
     * @return a Response containing a paginated list of UserDTO objects associated with the group.
     */
    @GetMapping("/{id}/users")
    public ResponseEntity<Page<UserDTO>> getUsersByGroup(final HttpServletRequest request,
                                                         final @PathVariable UUID id,
                                                         final @RequestParam MultiValueMap<String, String> filters,
                                                         final @ModelAttribute QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info(
                "[{}] Received GET request to get users of group {} with the following filters: {}",
                user.getLogin(),
                id,
                filters
        );

        Page<UserDTO> resources = accessControlService
                .findAllUsers(AccessControlType.GROUP, id, filters, queryFilter)
                .map(new BeanMapper<>(UserDTO.class));

        return ResponseEntity.status(this.getStatus(resources)).body(resources);
    }

    /**
     * Associates a user to a specified group.
     * This endpoint allows an administrative user to associate another user, identified by their login,
     * with a specific group, identified by its ID.
     *
     * @param request HttpServletRequest to access the HTTP session for authentication and authorization.
     * @param id      the ID of the group to which the user is to be associated.
     * @param login   the login identifier of the user to associate with the group.
     * @return a Response indicating the outcome of the association operation.
     */
    @PostMapping("/{id}/users")
    public ResponseEntity<Object> associateUser(final HttpServletRequest request,
                                                final @PathVariable UUID id,
                                                final @RequestBody @Valid @NotBlank String login) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received POST request to associate group {} with user {}", user.getLogin(), id, login);
        accessControlService.associateUser(AccessControlType.GROUP, id, login);

        return ResponseEntity.status(HttpStatus.CREATED.value()).contentType(MediaType.APPLICATION_JSON).build();
    }

    /**
     * Dissociates a user from a specified group.
     * This endpoint allows an administrative user to remove the association of a user, identified by their login,
     * from a specific group, identified by its ID.
     *
     * @param request HttpServletRequest to access the HTTP session for authentication and authorization.
     * @param id      the ID of the group from which the user is to be dissociated.
     * @param login   the login identifier of the user to dissociate from the group.
     * @return a Response indicating the outcome of the dissociation operation.
     */
    @DeleteMapping("/{id}/users/{login}")
    public ResponseEntity<Object> dissociateUser(final HttpServletRequest request,
                                                 final @PathVariable UUID id,
                                                 final @PathVariable String login) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received DELETE request to dissociate group {} with user {}", user.getLogin(), id, login);
        accessControlService.dissociateUser(AccessControlType.GROUP, id, login);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).contentType(MediaType.APPLICATION_JSON).build();
    }

    /**
     * Retrieves the sub-groups of a specified group.
     *
     * <p>This method processes a GET request to obtain sub-groups associated with a given group ID. It filters the
     * groups based on the provided query parameters and pagination settings.
     *
     * @param request     the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id          the ID of the group for which to retrieve sub-groups. Must be a valid and non-null Long value.
     * @param filters All query parameters for filtering results.
     * @param queryFilter the filter criteria and pagination information.
     * @return a Response object containing the requested page of AccessControlDirectDTO objects representing the
     * sub-groups of the specified group. The status of the response can vary based on the outcome of the request.
     */
    @GetMapping("/{id}/groups")
    public ResponseEntity<Page<AccessControlDirectDTO>> getSubGroupsOfGroup(
            final HttpServletRequest request,
            final @PathVariable UUID id,
            final @RequestParam MultiValueMap<String, String> filters,
            final @ModelAttribute QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info(
                "[{}] Received GET request to get sub-groups of group {} with the following filters: {}",
                user.getLogin(),
                id,
                filters
        );

        Page<AccessControlDirectDTO> resources = accessControlService
                .findAllAccessControls(
                        AccessControlType.GROUP,
                        id,
                        AccessControlType.GROUP,
                        filters,
                        queryFilter
                );

        return ResponseEntity.status(this.getStatus(resources)).body(resources);
    }

    /**
     * Associates a group with another group.
     *
     * <p>This method handles a POST request to create an association between two groups, specified by their IDs.
     * It validates the user's session and ensures the user has administrative privileges before proceeding with the
     * association.
     *
     * @param request    the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id         the ID of the first group to which the second group will be associated. Must be a valid and
     *                   non-null Long value.
     * @param subGroupId the ID of the second group to be associated with the first group. Must be a valid and non-null
     *                   Long value.
     * @return a Response object indicating the outcome of the association operation. A successful operation returns
     * a status of CREATED.
     */
    @PostMapping("/{id}/groups")
    public ResponseEntity<Object> associate(final HttpServletRequest request,
                                            final @PathVariable UUID id,
                                            final @RequestBody @Valid @NotNull String subGroupId) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received POST request to associate group {} with group {}", user.getLogin(), id, subGroupId);
        accessControlService.associate(
                AccessControlType.GROUP,
                id,
                AccessControlType.GROUP,
                UUID.fromString(subGroupId)
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).contentType(MediaType.APPLICATION_JSON).build();
    }

    /**
     * Dissociates a group from another group.
     *
     * <p>This method facilitates the handling of a DELETE request to remove the association between two groups,
     * identified by their respective IDs. The operation is secured, requiring validation of the user's session and
     * administrative privileges.
     *
     * @param request    the HttpServletRequest used to validate the user's session.
     * @param id         the ID of the first group from which the second group will be dissociated. Must be a valid and
     *                   non-null Long value.
     * @param subGroupId the ID of the second group to be dissociated from the first group. Must be a valid and non-null
     *                   Long value.
     * @return a Response object with a status indicating the outcome of the dissociation operation. A successful
     * operation returns a status of NO_CONTENT.
     */
    @DeleteMapping("/{id}/groups/{subGroupId}")
    public ResponseEntity<Object> dissociate(final HttpServletRequest request,
                                             final @PathVariable UUID id,
                                             final @PathVariable UUID subGroupId) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info(
                "[{}] Received DELETE request to dissociate sub-group {} with group {}",
                user.getLogin(),
                id,
                subGroupId
        );

        accessControlService.dissociate(AccessControlType.GROUP, id, AccessControlType.GROUP, subGroupId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).contentType(MediaType.APPLICATION_JSON).build();
    }

    /**
     * Retrieves the roles associated with a specific group, identified by the group's ID. This method processes a GET
     * request and returns a paginated list of AccessControlDirectDTO objects representing the roles. It supports
     * filtering based on query parameters to allow for refined searches within the group's associated roles.
     *
     * <p>This endpoint is secured to ensure that only users with administrative permissions can access role information
     * for a group. It is particularly useful for managing and reviewing the roles and permissions assigned to a group
     * within an access control system.
     *
     * @param request     The HttpServletRequest providing context to access the HTTP session.
     * @param id          The unique identifier of the group whose roles are being retrieved.
     * @param filters     All query parameters for filtering results.
     * @param queryFilter the filter criteria and pagination information.
     * @return A Response object containing a paginated list of AccessControlDirectDTO objects representing the roles
     * associated with the specified group. The response includes pagination details and adheres to the
     * HTTP status code conventions to indicate the outcome of the request.
     */
    @GetMapping("/{id}/roles")
    public ResponseEntity<Page<AccessControlDirectDTO>> getRolesOfGroup(
            final HttpServletRequest request,
            final @PathVariable UUID id,
            final @RequestParam MultiValueMap<String, String> filters,
            final @ModelAttribute QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info(
                "[{}] Received GET request to get roles of group {} with the following filters: {}",
                user.getLogin(),
                id,
                filters
        );

        Page<AccessControlDirectDTO> resources = accessControlService
                .findAllAccessControls(
                        AccessControlType.GROUP,
                        id, AccessControlType.ROLE,
                        filters,
                        queryFilter
                );

        return ResponseEntity.status(this.getStatus(resources)).body(resources);
    }

    /**
     * Retrieves the scopes associated with a specific group, identified by the group's ID. This method processes a GET
     * request and returns a paginated list of AccessControlDirectDTO objects representing the scopes. It supports
     * filtering based on query parameters to allow for refined searches within the group's associated scopes.
     *
     * <p>This endpoint is secured to ensure that only users with administrative permissions can access scope
     * information for a group. It is particularly useful for managing and reviewing the roles and permissions assigned
     * to a group within an access control system.
     *
     * @param request     The HttpServletRequest providing context to access the HTTP session.
     * @param id          The unique identifier of the group whose scopes are being retrieved.
     * @param filters     All query parameters for filtering results.
     * @param queryFilter the filter criteria and pagination information.
     * @return A Response object containing a paginated list of AccessControlDirectDTO objects representing the scopes
     * associated with the specified group. The response includes pagination details and adheres to the
     * HTTP status code conventions to indicate the outcome of the request.
     */
    @GetMapping("/{id}/scopes")
    public ResponseEntity<Page<AccessControlDirectDTO>> getScopesOfGroup(
            final HttpServletRequest request,
            final @PathVariable UUID id,
            final @RequestParam MultiValueMap<String, String> filters,
            final @ModelAttribute QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info(
                "[{}] Received GET request to get scopes of group {} with the following filters: {}",
                user.getLogin(),
                id,
                filters
        );

        Page<AccessControlDirectDTO> resources = accessControlService
                .findAllAccessControls(
                        AccessControlType.GROUP,
                        id,
                        AccessControlType.SCOPE,
                        filters,
                        queryFilter
                );

        return ResponseEntity.status(this.getStatus(resources)).body(resources);
    }

    /**
     * Retrieves the permissions of a specified group.
     *
     * <p>This method processes a GET request to obtain permissions associated with a given group ID. It filters the
     * permissions based on the provided query parameters and pagination settings.
     *
     * @param request     the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id          the ID of the groups for which to retrieve permissions. Must be a valid and non-null Long
     *                    value.
     * @param filters     All query parameters for filtering results.
     * @param queryFilter the filter criteria and pagination information.
     * @return a Response object containing the requested page of PermissionDirectDTO objects representing the
     * permissions of the specified group. The status of the response can vary based on the outcome of the request.
     */
    @GetMapping("/{id}/permissions")
    public ResponseEntity<Page<PermissionDirectDTO>> getPermissionsOfGroup(
            final HttpServletRequest request,
            final @PathVariable UUID id,
            final @RequestParam MultiValueMap<String, String> filters,
            final @ModelAttribute QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info(
                "[{}] Received GET request to get permissions of group {} with the following filters: {}",
                user.getLogin(),
                id,
                filters
        );

        AccessControl accessControl = accessControlService.findById(AccessControlType.GROUP, id);
        Page<PermissionDirectDTO> resources = accessControlPermissionService
                .findAll(accessControl.getId(), filters, queryFilter)
                .map(new AccessControlPermissionViewToPermissionDirectDTOFunction());


        return ResponseEntity.status(this.getStatus(resources)).body(resources);
    }
}
