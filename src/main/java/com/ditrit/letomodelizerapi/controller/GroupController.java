package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.model.BeanMapper;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDTO;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDirectDTO;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlRecord;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import com.ditrit.letomodelizerapi.model.permission.PermissionDirectDTO;
import com.ditrit.letomodelizerapi.model.user.UserDTO;
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
 * REST Controller for managing groups.
 * Provides endpoints for CRUD operations on groups, including listing, retrieving, creating, updating, and deleting
 * groups.
 * Only accessible by users with administrative permissions.
 */
@Slf4j
@Path("/groups")
@Produces(MediaType.APPLICATION_JSON)
@Controller
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

        log.info("Received GET request to get groups with the following filters: {}", filters);
        Page<AccessControlDTO> resources = accessControlService
                .findAll(AccessControlType.GROUP, filters, queryFilter.getPagination())
                .map(new BeanMapper<>(AccessControlDTO.class));

        return Response.status(this.getStatus(resources)).entity(resources).build();
    }

    /**
     * Retrieves a specific group by its ID.
     *
     * @param request HttpServletRequest to access the HTTP session
     * @param id      the ID of the group to retrieve
     * @return a Response containing the AccessControlDTO of the requested group
     */
    @GET
    @Path("/{id}")
    public Response getGroupById(final @Context HttpServletRequest request,
                                final @PathParam("id") @Valid @NotNull UUID id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("Received GET request to get group with id {}", id);
        AccessControlDTO accessControlDTO = new BeanMapper<>(AccessControlDTO.class)
                .apply(accessControlService.findById(AccessControlType.GROUP, id));

        return Response.ok(accessControlDTO).build();
    }

    /**
     * Creates a new group with the given details.
     *
     * @param request              HttpServletRequest to access the HTTP session
     * @param accessControlRecord  Data for creating the new group
     * @return a Response indicating the outcome of the creation operation
     */
    @POST
    public Response createGroup(final @Context HttpServletRequest request,
                               final @Valid AccessControlRecord accessControlRecord) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("Received POST request to create a group with {}", accessControlRecord);
        AccessControlDTO accessControlDTO = new BeanMapper<>(AccessControlDTO.class)
                .apply(accessControlService.create(AccessControlType.GROUP, accessControlRecord));

        return Response.status(HttpStatus.CREATED.value()).entity(accessControlDTO).build();
    }

    /**
     * Updates an existing group identified by the given ID with the provided details.
     *
     * @param request              HttpServletRequest to access the HTTP session
     * @param id                   ID of the group to update
     * @param accessControlRecord  Data for updating the group
     * @return a Response indicating the outcome of the update operation
     */
    @PUT
    @Path("/{id}")
    public Response updateGroup(final @Context HttpServletRequest request,
                                final @PathParam("id") @Valid @NotNull UUID id,
                                final @Valid AccessControlRecord accessControlRecord) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("Received PUT request to update a group with id {} with {}", id, accessControlRecord);
        AccessControlDTO accessControlDTO = new BeanMapper<>(AccessControlDTO.class)
                .apply(accessControlService.update(AccessControlType.GROUP, id, accessControlRecord));

        return Response.ok(accessControlDTO).build();
    }

    /**
     * Deletes the group with the specified ID.
     *
     * @param request HttpServletRequest to access the HTTP session
     * @param id      the ID of the group to delete
     * @return a Response indicating the outcome of the delete operation
     */
    @DELETE
    @Path("/{id}")
    public Response deleteGroup(final @Context HttpServletRequest request,
                                final @PathParam("id") @Valid @NotNull UUID id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("Received DELETE request to delete group with id {}", id);
        accessControlService.delete(AccessControlType.GROUP, id);

        return Response.noContent().build();
    }

    /**
     * Retrieves users associated to a specific group.
     * This endpoint fetches and returns a paginated list of users associated with the given group ID.
     * The method is accessible only to users with administrative permissions.
     *
     * @param request      HttpServletRequest to access the HTTP session for authentication and authorization.
     * @param id           the ID of the group for which users are to be retrieved.
     * @param uriInfo      UriInfo to extract query parameters for additional filtering.
     * @param queryFilter  BeanParam object for pagination and filtering purposes.
     * @return a Response containing a paginated list of UserDTO objects associated with the group.
     */
    @GET
    @Path("/{id}/users")
    public Response getUsersByGroup(final @Context HttpServletRequest request,
                                   final @PathParam("id") @Valid @NotNull UUID id,
                                   final @Context UriInfo uriInfo,
                                   final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        Map<String, String> filters = this.getFilters(uriInfo);
        log.info("Received GET request to get users of group {} with the following filters: {}", id, filters);
        Page<UserDTO> resources = accessControlService
                .findAllUsers(AccessControlType.GROUP, id, filters, queryFilter.getPagination())
                .map(new BeanMapper<>(UserDTO.class));

        return Response.status(this.getStatus(resources)).entity(resources).build();
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
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/{id}/users")
    public Response associateUser(final @Context HttpServletRequest request,
                                  final @PathParam("id") @Valid @NotNull UUID id,
                                  final @Valid @NotBlank String login) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("Received POST request to associate group {} with user {}", id, login);
        accessControlService.associateUser(AccessControlType.GROUP, id, login);

        return Response.status(HttpStatus.CREATED.value()).build();
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
    @DELETE
    @Path("/{id}/users/{login}")
    public Response dissociateUser(final @Context HttpServletRequest request,
                                  final @PathParam("id") @Valid @NotNull UUID id,
                                  final @PathParam("login") @Valid @NotBlank String login) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("Received DELETE request to dissociate group {} with user {}", id, login);
        accessControlService.dissociateUser(AccessControlType.GROUP, id, login);

        return Response.noContent().build();
    }

    /**
     * Retrieves the sub-groups of a specified group.
     *
     * <p>This method processes a GET request to obtain sub-groups associated with a given group ID. It filters the
     * groups based on the provided query parameters and pagination settings.
     *
     * @param request the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id the ID of the group for which to retrieve sub-groups. Must be a valid and non-null Long value.
     * @param uriInfo UriInfo context to extract query parameters for filtering results.
     * @param queryFilter bean parameter encapsulating filtering and pagination criteria.
     * @return a Response object containing the requested page of AccessControlDirectDTO objects representing the
     * sub-groups of the specified group. The status of the response can vary based on the outcome of the request.
     */
    @GET
    @Path("/{id}/groups")
    public Response getSubGroupsOfGroup(final @Context HttpServletRequest request,
                                        final @PathParam("id") @Valid @NotNull UUID id,
                                        final @Context UriInfo uriInfo,
                                        final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        Map<String, String> filters = this.getFilters(uriInfo);
        log.info("Received GET request to get sub-groups of group {} with the following filters: {}", id, filters);
        Page<AccessControlDirectDTO> resources = accessControlService
                .findAllAccessControls(id, AccessControlType.GROUP, filters, queryFilter.getPagination());

        return Response.status(this.getStatus(resources)).entity(resources).build();
    }

    /**
     * Associates a group with another group.
     *
     * <p>This method handles a POST request to create an association between two groups, specified by their IDs.
     * It validates the user's session and ensures the user has administrative privileges before proceeding with the
     * association.
     *
     * @param request the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id the ID of the first group to which the second group will be associated. Must be a valid and non-null
     *           Long value.
     * @param subGroupId the ID of the second group to be associated with the first group. Must be a valid and non-null
     *               Long value.
     * @return a Response object indicating the outcome of the association operation. A successful operation returns
     * a status of CREATED.
     */
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/{id}/groups")
    public Response associate(final @Context HttpServletRequest request,
                              final @PathParam("id") @Valid @NotNull UUID id,
                              final @Valid @NotNull String subGroupId) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("Received POST request to associate group {} with group {}", id, subGroupId);
        accessControlService.associate(
                AccessControlType.GROUP,
                id,
                AccessControlType.GROUP,
                UUID.fromString(subGroupId)
        );

        return Response.status(HttpStatus.CREATED.value()).build();
    }

    /**
     * Dissociates a group from another group.
     *
     * <p>This method facilitates the handling of a DELETE request to remove the association between two groups,
     * identified by their respective IDs. The operation is secured, requiring validation of the user's session and
     * administrative privileges.
     *
     * @param request the HttpServletRequest used to validate the user's session.
     * @param id the ID of the first group from which the second group will be dissociated. Must be a valid and non-null
     *           Long value.
     * @param subGroupId the ID of the second group to be dissociated from the first group. Must be a valid and non-null
     *                   Long value.
     * @return a Response object with a status indicating the outcome of the dissociation operation. A successful
     * operation returns a status of NO_CONTENT.
     */
    @DELETE
    @Path("/{id}/groups/{subGroupId}")
    public Response dissociate(final @Context HttpServletRequest request,
                               final @PathParam("id") @Valid @NotNull UUID id,
                               final @PathParam("subGroupId") @Valid @NotNull UUID subGroupId) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("Received DELETE request to dissociate sub-group {} with group {}", id, subGroupId);
        accessControlService.dissociate(AccessControlType.GROUP, id, AccessControlType.GROUP, subGroupId);

        return Response.noContent().build();
    }

    /**
     * Retrieves the roles associated with a specific group, identified by the group's ID. This method processes a GET
     * request and returns a paginated list of AccessControlDirectDTO objects representing the roles. It supports
     * filtering based on query parameters to allow for refined searches within the group's associated roles.
     *
     * This endpoint is secured to ensure that only users with administrative permissions can access role information
     * for a group. It is particularly useful for managing and reviewing the roles and permissions assigned to a group
     * within an access control system.
     *
     * @param request The HttpServletRequest providing context to access the HTTP session.
     * @param id The unique identifier of the group whose roles are being retrieved.
     * @param uriInfo URI information for extracting query parameters to apply as filters.
     * @param queryFilter A BeanParam object encapsulating pagination and filtering criteria to manage result set size
     *                    and relevance.
     * @return A Response object containing a paginated list of AccessControlDirectDTO objects representing the roles
     *         associated with the specified group. The response includes pagination details and adheres to the
     *         HTTP status code conventions to indicate the outcome of the request.
     */
    @GET
    @Path("/{id}/roles")
    public Response getRolesOfGroup(final @Context HttpServletRequest request,
                                    final @PathParam("id") @Valid @NotNull UUID id,
                                    final @Context UriInfo uriInfo,
                                    final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        Map<String, String> filters = this.getFilters(uriInfo);
        log.info("Received GET request to get roles of group {} with the following filters: {}", id, filters);
        Page<AccessControlDirectDTO> resources = accessControlService
                .findAllAccessControls(id, AccessControlType.ROLE, filters, queryFilter.getPagination());

        return Response.status(this.getStatus(resources)).entity(resources).build();
    }

    /**
     * Retrieves the permissions of a specified group.
     *
     * <p>This method processes a GET request to obtain permissions associated with a given group ID. It filters the
     * permissions based on the provided query parameters and pagination settings.
     *
     * @param request the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id the ID of the groups for which to retrieve permissions. Must be a valid and non-null Long value.
     * @param uriInfo UriInfo context to extract query parameters for filtering results.
     * @param queryFilter bean parameter encapsulating filtering and pagination criteria.
     * @return a Response object containing the requested page of PermissionDirectDTO objects representing the
     * permissions of the specified group. The status of the response can vary based on the outcome of the request.
     */
    @GET
    @Path("/{id}/permissions")
    public Response getPermissionsOfGroup(final @Context HttpServletRequest request,
                                          final @PathParam("id") @Valid @NotNull UUID id,
                                          final @Context UriInfo uriInfo,
                                          final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        Map<String, String> filters = this.getFilters(uriInfo);
        log.info("Received GET request to get permissions of group {} with the following filters: {}", id, filters);
        AccessControl accessControl = accessControlService.findById(AccessControlType.GROUP, id);
        Page<PermissionDirectDTO> resources = accessControlPermissionService
                .findAll(accessControl.getId(), filters, queryFilter.getPagination())
                .map(new BeanMapper<>(PermissionDirectDTO.class));

        return Response.status(this.getStatus(resources)).entity(resources).build();
    }
}
