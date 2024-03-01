package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.config.Constants;
import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.model.BeanMapper;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDTO;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDirectDTO;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlRecord;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.model.permission.PermissionDirectDTO;
import com.ditrit.letomodelizerapi.model.user.UserDTO;
import com.ditrit.letomodelizerapi.persistence.model.AccessControl;
import com.ditrit.letomodelizerapi.persistence.model.Permission;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.AccessControlPermissionService;
import com.ditrit.letomodelizerapi.service.AccessControlService;
import com.ditrit.letomodelizerapi.service.PermissionService;
import com.ditrit.letomodelizerapi.service.UserPermissionService;
import com.ditrit.letomodelizerapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * REST Controller for managing roles.
 * Provides endpoints for CRUD operations on roles, including listing, retrieving, creating, updating, and deleting
 * roles.
 * Only accessible by users with administrative permissions.
 */
@Slf4j
@Path("/roles")
@Produces(MediaType.APPLICATION_JSON)
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleController implements DefaultController {

    /**
     * Service to manage user.
     */
    private final UserService userService;

    /**
     * Service to manage user permissions.
     */
    private final UserPermissionService userPermissionService;

    /**
     * Service to manage access controls.
     */
    private final AccessControlService accessControlService;

    /**
     * Service to manage permissions.
     */
    private final PermissionService permissionService;

    /**
     * Service to manage permissions of access controls.
     */
    private final AccessControlPermissionService accessControlPermissionService;

    /**
     * Finds and returns all roles based on the provided query filters.
     * Only accessible by users with administrative permissions.
     *
     * @param request     HttpServletRequest to access the HTTP session
     * @param uriInfo     UriInfo to extract query parameters
     * @param queryFilter BeanParam for pagination and filtering
     * @return a Response containing a page of AccessControlDTO objects
     */
    @GET
    public Response findAll(final @Context HttpServletRequest request,
                            final @Context UriInfo uriInfo,
                            final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);
        Map<String, String> filters = this.getFilters(uriInfo);

        log.info("[{}] Received GET request to get roles with the following filters: {}", user.getLogin(), filters);
        Page<AccessControlDTO> resources = accessControlService
                .findAll(AccessControlType.ROLE, filters, queryFilter.getPagination())
                .map(new BeanMapper<>(AccessControlDTO.class));

        return Response.status(this.getStatus(resources)).entity(resources).build();
    }

    /**
     * Retrieves a specific role by its ID.
     * Only accessible by users with administrative permissions.
     *
     * @param request HttpServletRequest to access the HTTP session
     * @param id      the ID of the role to retrieve
     * @return a Response containing the AccessControlDTO of the requested role
     */
    @GET
    @Path("/{id}")
    public Response getRoleById(final @Context HttpServletRequest request,
                                final @PathParam("id") @Valid @NotNull UUID id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received GET request to get role with id {}", user.getLogin(), id);
        AccessControlDTO accessControlDTO = new BeanMapper<>(AccessControlDTO.class)
                .apply(accessControlService.findById(AccessControlType.ROLE, id));

        return Response.ok(accessControlDTO).build();
    }

    /**
     * Creates a new role with the given details.
     * Only accessible by users with administrative permissions.
     *
     * @param request             HttpServletRequest to access the HTTP session
     * @param accessControlRecord Data for creating the new role
     * @return a Response indicating the outcome of the creation operation
     */
    @POST
    public Response createRole(final @Context HttpServletRequest request,
                               final @Valid AccessControlRecord accessControlRecord) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received POST request to create a role with {}", user.getLogin(), accessControlRecord);
        AccessControlDTO accessControlDTO = new BeanMapper<>(AccessControlDTO.class)
                .apply(accessControlService.create(AccessControlType.ROLE, accessControlRecord));

        return Response.status(HttpStatus.CREATED.value()).entity(accessControlDTO).build();
    }

    /**
     * Updates an existing role identified by the given ID with the provided details.
     * Only accessible by users with administrative permissions.
     *
     * @param request             HttpServletRequest to access the HTTP session
     * @param id                  ID of the role to update
     * @param accessControlRecord Data for updating the role
     * @return a Response indicating the outcome of the update operation
     */
    @PUT
    @Path("/{id}")
    public Response updateRole(final @Context HttpServletRequest request,
                               final @PathParam("id") @Valid @NotNull UUID id,
                               final @Valid AccessControlRecord accessControlRecord) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);
        checkSuperAdmin(id);

        log.info(
                "[{}] Received PUT request to update a role with id {} with {}",
                user.getLogin(),
                id,
                accessControlRecord
        );

        AccessControlDTO accessControlDTO = new BeanMapper<>(AccessControlDTO.class)
                .apply(accessControlService.update(AccessControlType.ROLE, id, accessControlRecord));

        return Response.ok(accessControlDTO).build();
    }

    /**
     * Deletes the role with the specified ID.
     * Only accessible by users with administrative permissions.
     *
     * @param request HttpServletRequest to access the HTTP session
     * @param id      the ID of the role to delete
     * @return a Response indicating the outcome of the delete operation
     */
    @DELETE
    @Path("/{id}")
    public Response deleteRole(final @Context HttpServletRequest request,
                               final @PathParam("id") @Valid @NotNull UUID id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);
        checkSuperAdmin(id);

        log.info("[{}] Received DELETE request to delete role with id {}", user.getLogin(), id);
        accessControlService.delete(AccessControlType.ROLE, id);

        return Response.noContent().build();
    }

    /**
     * Retrieves users associated to a specific role.
     * This endpoint fetches and returns a paginated list of users associated with the given role ID.
     * The method is accessible only to users with administrative permissions.
     *
     * @param request     HttpServletRequest to access the HTTP session for authentication and authorization.
     * @param id          the ID of the role for which users are to be retrieved.
     * @param uriInfo     UriInfo to extract query parameters for additional filtering.
     * @param queryFilter BeanParam object for pagination and filtering purposes.
     * @return a Response containing a paginated list of UserDTO objects associated with the role.
     */
    @GET
    @Path("/{id}/users")
    public Response getUsersByRole(final @Context HttpServletRequest request,
                                   final @PathParam("id") @Valid @NotNull UUID id,
                                   final @Context UriInfo uriInfo,
                                   final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        Map<String, String> filters = this.getFilters(uriInfo);

        log.info(
                "[{}] Received GET request to get users of role {} with the following filters: {}",
                user.getLogin(),
                id,
                filters
        );

        Page<UserDTO> resources = accessControlService
                .findAllUsers(AccessControlType.ROLE, id, filters, queryFilter.getPagination())
                .map(new BeanMapper<>(UserDTO.class));

        return Response.status(this.getStatus(resources)).entity(resources).build();
    }

    /**
     * Associates a user to a specified role.
     * This endpoint allows an administrative user to associate another user, identified by their login,
     * with a specific role, identified by its ID.
     *
     * @param request HttpServletRequest to access the HTTP session for authentication and authorization.
     * @param id      the ID of the role to which the user is to be associated.
     * @param login   the login identifier of the user to associate with the role.
     * @return a Response indicating the outcome of the association operation.
     */
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/{id}/users")
    public Response associateUser(final @Context HttpServletRequest request,
                                  final @PathParam("id") @Valid @NotNull UUID id,
                                  final @Valid @NotBlank String login) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received POST request to associate role {} with user {}", user.getLogin(), id, login);
        accessControlService.associateUser(AccessControlType.ROLE, id, login);

        return Response.status(HttpStatus.CREATED.value()).build();
    }

    /**
     * Dissociates a user from a specified role.
     * This endpoint allows an administrative user to remove the association of a user, identified by their login,
     * from a specific role, identified by its ID.
     *
     * @param request HttpServletRequest to access the HTTP session for authentication and authorization.
     * @param id      the ID of the role from which the user is to be dissociated.
     * @param login   the login identifier of the user to dissociate from the role.
     * @return a Response indicating the outcome of the dissociation operation.
     */
    @DELETE
    @Path("/{id}/users/{login}")
    public Response dissociateUser(final @Context HttpServletRequest request,
                                   final @PathParam("id") @Valid @NotNull UUID id,
                                   final @PathParam(Constants.DEFAULT_USER_PROPERTY) @Valid @NotBlank String login) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received DELETE request to dissociate role {} with user {}", user.getLogin(), id, login);
        accessControlService.dissociateUser(AccessControlType.ROLE, id, login);

        return Response.noContent().build();
    }

    /**
     * Retrieves the sub-roles of a specified role.
     *
     * <p>This method processes a GET request to obtain sub-roles associated with a given role ID. It filters the roles
     * based on the provided query parameters and pagination settings.
     *
     * @param request     the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id          the ID of the role for which to retrieve sub-roles. Must be a valid and non-null Long value.
     * @param uriInfo     UriInfo context to extract query parameters for filtering results.
     * @param queryFilter bean parameter encapsulating filtering and pagination criteria.
     * @return a Response object containing the requested page of AccessControlDirectDTO objects representing the
     * sub-roles of the specified role. The status of the response can vary based on the outcome of the request.
     */
    @GET
    @Path("/{id}/roles")
    public Response getSubRolesOfRole(final @Context HttpServletRequest request,
                                      final @PathParam("id") @Valid @NotNull UUID id,
                                      final @Context UriInfo uriInfo,
                                      final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        Map<String, String> filters = this.getFilters(uriInfo);

        log.info(
                "[{}] Received GET request to get sub roles of role {} with the following filters: {}",
                user.getLogin(),
                id,
                filters
        );

        Page<AccessControlDirectDTO> resources = accessControlService
                .findAllAccessControls(id, AccessControlType.ROLE, filters, queryFilter.getPagination());

        return Response.status(this.getStatus(resources)).entity(resources).build();
    }

    /**
     * Associates a role with another role.
     *
     * <p>This method handles a POST request to create an association between two roles, specified by their IDs.
     * It validates the user's session and ensures the user has administrative privileges before proceeding with the
     * association.
     *
     * @param request the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id      the ID of the first role to which the second role will be associated. Must be a valid and non-null
     *                Long value.
     * @param roleId  the ID of the second role to be associated with the first role. Must be a valid and non-null
     *                Long value.
     * @return a Response object indicating the outcome of the association operation. A successful operation returns
     * a status of CREATED.
     */
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/{id}/roles")
    public Response associate(final @Context HttpServletRequest request,
                              final @PathParam("id") @Valid @NotNull UUID id,
                              final @Valid @NotNull String roleId) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);
        checkSuperAdmin(id);

        log.info("[{}] Received POST request to associate role {} with role {}", user.getLogin(), id, roleId);
        accessControlService.associate(AccessControlType.ROLE, id, AccessControlType.ROLE, UUID.fromString(roleId));

        return Response.status(HttpStatus.CREATED.value()).build();
    }

    /**
     * Dissociates a role from another role.
     *
     * <p>This method facilitates the handling of a DELETE request to remove the association between two roles,
     * identified by their respective IDs. The operation is secured, requiring validation of the user's session and
     * administrative privileges.
     *
     * @param request the HttpServletRequest used to validate the user's session.
     * @param id      the ID of the first role from which the second role will be dissociated. Must be a valid and
     *                non-null Long value.
     * @param roleId  the ID of the second role to be dissociated from the first role. Must be a valid and non-null Long
     *                value.
     * @return a Response object with a status indicating the outcome of the dissociation operation. A successful
     * operation returns a status of NO_CONTENT.
     */
    @DELETE
    @Path("/{id}/roles/{roleId}")
    public Response dissociate(final @Context HttpServletRequest request,
                               final @PathParam("id") @Valid @NotNull UUID id,
                               final @PathParam("roleId") @Valid @NotNull UUID roleId) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);
        checkSuperAdmin(id);

        log.info("[{}] Received DELETE request to dissociate role {} with role {}", user.getLogin(), id, roleId);
        accessControlService.dissociate(AccessControlType.ROLE, id, AccessControlType.ROLE, roleId);

        return Response.noContent().build();
    }

    /**
     * Retrieves the groups of a specified role.
     *
     * <p>This method processes a GET request to obtain groups associated with a given role ID. It filters the groups
     * based on the provided query parameters and pagination settings.
     *
     * @param request     the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id          the ID of the role for which to retrieve sub-roles. Must be a valid and non-null Long value.
     * @param uriInfo     UriInfo context to extract query parameters for filtering results.
     * @param queryFilter bean parameter encapsulating filtering and pagination criteria.
     * @return a Response object containing the requested page of AccessControlDirectDTO objects representing the
     * groups of the specified role. The status of the response can vary based on the outcome of the request.
     */
    @GET
    @Path("/{id}/groups")
    public Response getGroupsOfRole(final @Context HttpServletRequest request,
                                    final @PathParam("id") @Valid @NotNull UUID id,
                                    final @Context UriInfo uriInfo,
                                    final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        Map<String, String> filters = new HashMap<>(this.getFilters(uriInfo));
        filters.put("parentAccessControlId", id.toString());
        filters.put("parentAccessControlType", AccessControlType.ROLE.name());

        log.info(
                "[{}] Received GET request to get groups of role {} with the following filters: {}",
                user.getLogin(),
                id,
                filters
        );

        Page<AccessControlDirectDTO> resources = accessControlService
                .findAllChildren(
                        AccessControlType.ROLE,
                        id,
                        AccessControlType.GROUP,
                        filters,
                        queryFilter.getPagination()
                );

        return Response.status(this.getStatus(resources)).entity(resources).build();
    }

    /**
     * Associates a group with a role.
     *
     * <p>This method handles a POST request to create an association between group and role, specified by their IDs.
     * It validates the user's session and ensures the user has administrative privileges before proceeding with the
     * association.
     *
     * @param request the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id      the ID of the role to which the group will be associated. Must be a valid and non-null Long value.
     * @param groupId the ID of the group to be associated to the role. Must be a valid and non-null Long value.
     * @return a Response object indicating the outcome of the association operation. A successful operation returns
     * a status of CREATED.
     */
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/{id}/groups")
    public Response associateGroup(final @Context HttpServletRequest request,
                                   final @PathParam("id") @Valid @NotNull UUID id,
                                   final @Valid @NotNull String groupId) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);
        checkSuperAdmin(id);

        log.info("[{}] Received POST request to associate role {} with group {}", user.getLogin(), id, groupId);
        accessControlService.associate(AccessControlType.ROLE, id, AccessControlType.GROUP, UUID.fromString(groupId));

        return Response.status(HttpStatus.CREATED.value()).build();
    }

    /**
     * Dissociates a group from role.
     *
     * <p>This method facilitates the handling of a DELETE request to remove the association between group and role,
     * identified by their respective IDs. The operation is secured, requiring validation of the user's session and
     * administrative privileges.
     *
     * @param request the HttpServletRequest used to validate the user's session.
     * @param id      the ID of the role from which the group will be dissociated. Must be a valid and non-null Long
     *                value.
     * @param groupId the ID of the group to be dissociated from the role. Must be a valid and non-null Long value.
     * @return a Response object with a status indicating the outcome of the dissociation operation. A successful
     * operation returns a status of NO_CONTENT.
     */
    @DELETE
    @Path("/{id}/groups/{groupId}")
    public Response dissociateGroup(final @Context HttpServletRequest request,
                                    final @PathParam("id") @Valid @NotNull UUID id,
                                    final @PathParam("groupId") @Valid @NotNull UUID groupId) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);
        checkSuperAdmin(id);

        log.info("[{}] Received DELETE request to dissociate role {} with group {}", user.getLogin(), id, groupId);
        accessControlService.dissociate(AccessControlType.ROLE, id, AccessControlType.GROUP, groupId);

        return Response.noContent().build();
    }

    /**
     * Retrieves the permissions of a specified role.
     *
     * <p>This method processes a GET request to obtain permissions associated with a given role ID. It filters the
     * permissions based on the provided query parameters and pagination settings.
     *
     * @param request     the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id          the ID of the role for which to retrieve sub-roles. Must be a valid and non-null Long value.
     * @param uriInfo     UriInfo context to extract query parameters for filtering results.
     * @param queryFilter bean parameter encapsulating filtering and pagination criteria.
     * @return a Response object containing the requested page of AccessControlDirectDTO objects representing the
     * permissions of the specified role. The status of the response can vary based on the outcome of the request.
     */
    @GET
    @Path("/{id}/permissions")
    public Response getPermissionsOfRole(final @Context HttpServletRequest request,
                                         final @PathParam("id") @Valid @NotNull UUID id,
                                         final @Context UriInfo uriInfo,
                                         final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        Map<String, String> filters = this.getFilters(uriInfo);

        log.info(
                "[{}] Received GET request to get permissions of role {} with the following filters: {}",
                user.getLogin(),
                id,
                filters
        );

        AccessControl accessControl = accessControlService.findById(AccessControlType.ROLE, id);
        Page<PermissionDirectDTO> resources = accessControlPermissionService
                .findAll(accessControl.getId(), filters, queryFilter.getPagination())
                .map(accessControlPermissionView -> {
                    PermissionDirectDTO dto = new PermissionDirectDTO();

                    dto.setId(accessControlPermissionView.getPermissionId());
                    dto.setAction(accessControlPermissionView.getAction());
                    dto.setEntity(accessControlPermissionView.getEntity());
                    dto.setLibraryId(accessControlPermissionView.getLibraryId());
                    dto.setIsDirect(accessControlPermissionView.getIsDirect());

                    return dto;
                });

        return Response.status(this.getStatus(resources)).entity(resources).build();
    }

    /**
     * Associates a permission with a role.
     *
     * <p>This method handles a POST request to create an association between permission and role, specified by their
     * IDs.
     * It validates the user's session and ensures the user has administrative privileges before proceeding with the
     * association.
     *
     * @param request      the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id           the ID of the role to which the role will be associated. Must be a valid and non-null Long
     *                     value.
     * @param permissionId the ID of the permission to be associated to the role. Must be a valid and non-null Long
     *                     value.
     * @return a Response object indicating the outcome of the association operation. A successful operation returns
     * a status of CREATED.
     */
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/{id}/permissions")
    public Response associatePermission(final @Context HttpServletRequest request,
                                        final @PathParam("id") @Valid @NotNull UUID id,
                                        final @Valid @NotNull String permissionId) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);
        checkSuperAdmin(id);

        log.info(
                "[{}] Received POST request to associate role {} with permission {}",
                user.getLogin(),
                id,
                permissionId
        );

        AccessControl accessControl = accessControlService.findById(AccessControlType.ROLE, id);
        Permission permission = permissionService.findById(UUID.fromString(permissionId));
        accessControlPermissionService.associate(accessControl.getId(), permission.getId());

        return Response.status(HttpStatus.CREATED.value()).build();
    }

    /**
     * Dissociates a permission from role.
     *
     * <p>This method facilitates the handling of a DELETE request to remove the association between permission and
     * role, identified by their respective IDs. The operation is secured, requiring validation of the user's session
     * and administrative privileges.
     *
     * @param request      the HttpServletRequest used to validate the user's session.
     * @param id           the ID of the role from which the permission will be dissociated. Must be a valid and
     *                     non-null Long value.
     * @param permissionId the ID of the permission to be dissociated from the role. Must be a valid and non-null Long
     *                     value.
     * @return a Response object with a status indicating the outcome of the dissociation operation. A successful
     * operation returns a status of NO_CONTENT.
     */
    @DELETE
    @Path("/{id}/permissions/{permissionId}")
    public Response dissociatePermission(final @Context HttpServletRequest request,
                                         final @PathParam("id") @Valid @NotNull UUID id,
                                         final @PathParam("permissionId") @Valid @NotNull UUID permissionId) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);
        checkSuperAdmin(id);

        log.info(
                "[{}] Received DELETE request to dissociate role {} with permission {}",
                user.getLogin(),
                id,
                permissionId
        );

        AccessControl accessControl = accessControlService.findById(AccessControlType.ROLE, id);
        Permission permission = permissionService.findById(permissionId);
        accessControlPermissionService.dissociate(accessControl.getId(), permission.getId());

        return Response.noContent().build();
    }

    /**
     * Checks if the provided ID matches the ID of the super administrator role.
     * This method is designed to enforce a check that prevents operations or actions that should not be allowed
     * on the super administrator role, based on its unique ID. If the provided ID matches that of the super
     * administrator, an ApiException is thrown to indicate an incorrect or unauthorized value.
     *
     * @param id the ID to check against the super administrator role ID.
     * @throws ApiException if the provided ID matches the super administrator role ID, indicating an operation
     *                      that is not allowed or is incorrect based on the application's business rules.
     */
    public void checkSuperAdmin(final UUID id) {
        UUID superAdministratorId = accessControlService.getSuperAdministratorId();

        if (superAdministratorId.equals(id)) {
            throw new ApiException(ErrorType.WRONG_VALUE, "id", id.toString());
        }
    }
}
