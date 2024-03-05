package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.config.Constants;
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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.util.Map;
import java.util.UUID;

/**
 * REST Controller for managing scope.
 * Provides endpoints for CRUD operations on scopes, including listing, retrieving, creating, updating, and deleting
 * scopes.
 * Only accessible by users with administrative permissions.
 */
@Slf4j
@Path("/scopes")
@Produces(MediaType.APPLICATION_JSON)
@Controller
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
     * @param uriInfo      UriInfo to extract query parameters
     * @param queryFilter  BeanParam for pagination and filtering
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

        log.info("[{}] Received GET request to get scopes with the following filters: {}", user.getLogin(), filters);
        Page<AccessControlDTO> resources = accessControlService
                .findAll(AccessControlType.SCOPE, filters, queryFilter.getPagination())
                .map(new BeanMapper<>(AccessControlDTO.class));

        return Response.status(this.getStatus(resources)).entity(resources).build();
    }

    /**
     * Retrieves a specific scope by its ID.
     *
     * @param request HttpServletRequest to access the HTTP session
     * @param id      the ID of the scope to retrieve
     * @return a Response containing the AccessControlDTO of the requested scope
     */
    @GET
    @Path("/{id}")
    public Response getScopeById(final @Context HttpServletRequest request,
                                final @PathParam("id") @Valid @NotNull UUID id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received GET request to get scope with id {}", user.getLogin(), id);
        AccessControlDTO accessControlDTO = new BeanMapper<>(AccessControlDTO.class)
                .apply(accessControlService.findById(AccessControlType.SCOPE, id));

        return Response.ok(accessControlDTO).build();
    }

    /**
     * Creates a new scope with the given details.
     *
     * @param request              HttpServletRequest to access the HTTP session
     * @param accessControlRecord  Data for creating the new scope
     * @return a Response indicating the outcome of the creation operation
     */
    @POST
    public Response createScope(final @Context HttpServletRequest request,
                               final @Valid AccessControlRecord accessControlRecord) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received POST request to create a scope with {}", user.getLogin(), accessControlRecord);
        AccessControlDTO accessControlDTO = new BeanMapper<>(AccessControlDTO.class)
                .apply(accessControlService.create(AccessControlType.SCOPE, accessControlRecord));

        return Response.status(HttpStatus.CREATED.value()).entity(accessControlDTO).build();
    }

    /**
     * Updates an existing scope identified by the given ID with the provided details.
     *
     * @param request              HttpServletRequest to access the HTTP session
     * @param id                   ID of the scope to update
     * @param accessControlRecord  Data for updating the scope
     * @return a Response indicating the outcome of the update operation
     */
    @PUT
    @Path("/{id}")
    public Response updateScope(final @Context HttpServletRequest request,
                                final @PathParam("id") @Valid @NotNull UUID id,
                                final @Valid AccessControlRecord accessControlRecord) {
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

        return Response.ok(accessControlDTO).build();
    }

    /**
     * Deletes the scope with the specified ID.
     *
     * @param request HttpServletRequest to access the HTTP session
     * @param id      the ID of the scope to delete
     * @return a Response indicating the outcome of the delete operation
     */
    @DELETE
    @Path("/{id}")
    public Response deleteScope(final @Context HttpServletRequest request,
                                final @PathParam("id") @Valid @NotNull UUID id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received DELETE request to delete scope with id {}", user.getLogin(), id);
        accessControlService.delete(AccessControlType.SCOPE, id);

        return Response.noContent().build();
    }

    /**
     * Retrieves users associated to a specific scope.
     * This endpoint fetches and returns a paginated list of users associated with the given scope ID.
     * The method is accessible only to users with administrative permissions.
     *
     * @param request      HttpServletRequest to access the HTTP session for authentication and authorization.
     * @param id           the ID of the scope for which users are to be retrieved.
     * @param uriInfo      UriInfo to extract query parameters for additional filtering.
     * @param queryFilter  BeanParam object for pagination and filtering purposes.
     * @return a Response containing a paginated list of UserDTO objects associated with the scope.
     */
    @GET
    @Path("/{id}/users")
    public Response getUsersByScope(final @Context HttpServletRequest request,
                                   final @PathParam("id") @Valid @NotNull UUID id,
                                   final @Context UriInfo uriInfo,
                                   final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        Map<String, String> filters = this.getFilters(uriInfo);

        log.info(
                "[{}] Received GET request to get users of scope {} with the following filters: {}",
                user.getLogin(),
                id,
                filters
        );

        Page<UserDTO> resources = accessControlService
                .findAllUsers(AccessControlType.SCOPE, id, filters, queryFilter.getPagination())
                .map(new BeanMapper<>(UserDTO.class));

        return Response.status(this.getStatus(resources)).entity(resources).build();
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
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/{id}/users")
    public Response associateUser(final @Context HttpServletRequest request,
                                  final @PathParam("id") @Valid @NotNull UUID id,
                                  final @Valid @NotBlank String login) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received POST request to associate scope {} with user {}", user.getLogin(), id, login);
        accessControlService.associateUser(AccessControlType.SCOPE, id, login);

        return Response.status(HttpStatus.CREATED.value()).build();
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
    @DELETE
    @Path("/{id}/users/{login}")
    public Response dissociateUser(final @Context HttpServletRequest request,
                                  final @PathParam("id") @Valid @NotNull UUID id,
                                  final @PathParam(Constants.DEFAULT_USER_PROPERTY) @Valid @NotBlank String login) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received DELETE request to dissociate scope {} with user {}", user.getLogin(), id, login);
        accessControlService.dissociateUser(AccessControlType.SCOPE, id, login);

        return Response.noContent().build();
    }

    /**
     * Retrieves the groups of a specified scope.
     *
     * <p>This method processes a GET request to obtain groups associated with a given scope ID. It filters the
     * scopes based on the provided query parameters and pagination settings.
     *
     * @param request the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id the ID of the scope for which to retrieve sub-scopes. Must be a valid and non-null Long value.
     * @param uriInfo UriInfo context to extract query parameters for filtering results.
     * @param queryFilter bean parameter encapsulating filtering and pagination criteria.
     * @return a Response object containing the requested page of AccessControlDirectDTO objects representing the
     * groups of the specified scope. The status of the response can vary based on the outcome of the request.
     */
    @GET
    @Path("/{id}/groups")
    public Response getGroupsOfScope(final @Context HttpServletRequest request,
                                        final @PathParam("id") @Valid @NotNull UUID id,
                                        final @Context UriInfo uriInfo,
                                        final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        Map<String, String> filters = this.getFilters(uriInfo);

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
                        queryFilter.getPagination()
                );

        return Response.status(this.getStatus(resources)).entity(resources).build();
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
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/{id}/groups")
    public Response associate(final @Context HttpServletRequest request,
                              final @PathParam("id") @Valid @NotNull UUID id,
                              final @Valid @NotNull String groupId) {
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

        return Response.status(HttpStatus.CREATED.value()).build();
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
    @DELETE
    @Path("/{id}/groups/{groupId}")
    public Response dissociate(final @Context HttpServletRequest request,
                               final @PathParam("id") @Valid @NotNull UUID id,
                               final @PathParam("groupId") @Valid @NotNull UUID groupId) {
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

        return Response.noContent().build();
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
     * @param request The HttpServletRequest providing context to access the HTTP session.
     * @param id The unique identifier of the scope whose roles are being retrieved.
     * @param uriInfo URI information for extracting query parameters to apply as filters.
     * @param queryFilter A BeanParam object encapsulating pagination and filtering criteria to manage result set size
     *                    and relevance.
     * @return A Response object containing a paginated list of AccessControlDirectDTO objects representing the roles
     *         associated with the specified scope. The response includes pagination details and adheres to the
     *         HTTP status code conventions to indicate the outcome of the request.
     */
    @GET
    @Path("/{id}/roles")
    public Response getRolesOfScope(final @Context HttpServletRequest request,
                                    final @PathParam("id") @Valid @NotNull UUID id,
                                    final @Context UriInfo uriInfo,
                                    final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        Map<String, String> filters = this.getFilters(uriInfo);

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
                        queryFilter.getPagination()
                );

        return Response.status(this.getStatus(resources)).entity(resources).build();
    }

    /**
     * Retrieves the permissions of a specified scope.
     *
     * <p>This method processes a GET request to obtain permissions associated with a given scope ID. It filters the
     * permissions based on the provided query parameters and pagination settings.
     *
     * @param request the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id the ID of the scopes for which to retrieve permissions. Must be a valid and non-null Long value.
     * @param uriInfo UriInfo context to extract query parameters for filtering results.
     * @param queryFilter bean parameter encapsulating filtering and pagination criteria.
     * @return a Response object containing the requested page of PermissionDirectDTO objects representing the
     * permissions of the specified scope. The status of the response can vary based on the outcome of the request.
     */
    @GET
    @Path("/{id}/permissions")
    public Response getPermissionsOfScope(final @Context HttpServletRequest request,
                                          final @PathParam("id") @Valid @NotNull UUID id,
                                          final @Context UriInfo uriInfo,
                                          final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        Map<String, String> filters = this.getFilters(uriInfo);

        log.info(
                "[{}] Received GET request to get permissions of scope {} with the following filters: {}",
                user.getLogin(),
                id,
                filters
        );

        AccessControl accessControl = accessControlService.findById(AccessControlType.SCOPE, id);
        Page<PermissionDirectDTO> resources = accessControlPermissionService
                .findAll(accessControl.getId(), filters, queryFilter.getPagination())
                .map(new AccessControlPermissionViewToPermissionDirectDTOFunction());


        return Response.status(this.getStatus(resources)).entity(resources).build();
    }
}
