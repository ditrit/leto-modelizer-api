package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.model.BeanMapper;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDTO;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDirectDTO;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlRecord;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import com.ditrit.letomodelizerapi.model.user.UserDTO;
import com.ditrit.letomodelizerapi.persistence.model.User;
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
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RoleController implements DefaultController {

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
     * Finds and returns all roles based on the provided query filters.
     * Only accessible by users with administrative permissions.
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

        log.info("Received GET request to get roles with the following filters: {}", filters);
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
                                final @PathParam("id") @Valid @NotNull Long id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("Received GET request to get role with id {}", id);
        AccessControlDTO accessControlDTO = new BeanMapper<>(AccessControlDTO.class)
                .apply(accessControlService.findById(AccessControlType.ROLE, id));

        return Response.ok(accessControlDTO).build();
    }

    /**
     * Creates a new role with the given details.
     * Only accessible by users with administrative permissions.
     *
     * @param request              HttpServletRequest to access the HTTP session
     * @param accessControlRecord  Data for creating the new role
     * @return a Response indicating the outcome of the creation operation
     */
    @POST
    public Response createRole(final @Context HttpServletRequest request,
                               final @Valid AccessControlRecord accessControlRecord) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("Received POST request to create a role with {}", accessControlRecord);
        AccessControlDTO accessControlDTO = new BeanMapper<>(AccessControlDTO.class)
                .apply(accessControlService.create(AccessControlType.ROLE, accessControlRecord));

        return Response.status(HttpStatus.CREATED.value()).entity(accessControlDTO).build();
    }

    /**
     * Updates an existing role identified by the given ID with the provided details.
     * Only accessible by users with administrative permissions.
     *
     * @param request              HttpServletRequest to access the HTTP session
     * @param id                   ID of the role to update
     * @param accessControlRecord  Data for updating the role
     * @return a Response indicating the outcome of the update operation
     */
    @PUT
    @Path("/{id}")
    public Response updateRole(final @Context HttpServletRequest request,
                               final @PathParam("id") @Valid @NotNull Long id,
                               final @Valid AccessControlRecord accessControlRecord) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("Received PUT request to update a role with id {} with {}", id, accessControlRecord);
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
                               final @PathParam("id") @Valid @NotNull Long id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("Received DELETE request to delete role with id {}", id);
        accessControlService.delete(AccessControlType.ROLE, id);

        return Response.noContent().build();
    }

    /**
     * Retrieves users associated to a specific role.
     * This endpoint fetches and returns a paginated list of users associated with the given role ID.
     * The method is accessible only to users with administrative permissions.
     *
     * @param request      HttpServletRequest to access the HTTP session for authentication and authorization.
     * @param id           the ID of the role for which users are to be retrieved.
     * @param uriInfo      UriInfo to extract query parameters for additional filtering.
     * @param queryFilter  BeanParam object for pagination and filtering purposes.
     * @return a Response containing a paginated list of UserDTO objects associated with the role.
     */
    @GET
    @Path("/{id}/users")
    public Response getUsersByRole(final @Context HttpServletRequest request,
                                   final @PathParam("id") @Valid @NotNull Long id,
                                   final @Context UriInfo uriInfo,
                                   final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        Map<String, String> filters = this.getFilters(uriInfo);
        log.info("Received GET request to get users of role {} with the following filters: {}", id, filters);
        Page<UserDTO> resources = accessControlService
                .findAllUsers(id, filters, queryFilter.getPagination())
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
                                  final @PathParam("id") @Valid @NotNull Long id,
                                  final @Valid @NotBlank String login) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("Received POST request to associate role {} with user {}", id, login);
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
                                  final @PathParam("id") @Valid @NotNull Long id,
                                  final @PathParam("login") @Valid @NotBlank String login) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("Received DELETE request to dissociate role {} with user {}", id, login);
        accessControlService.dissociateUser(AccessControlType.ROLE, id, login);

        return Response.noContent().build();
    }

    /**
     * Retrieves the sub-roles of a specified role.
     *
     * <p>This method processes a GET request to obtain sub-roles associated with a given role ID. It filters the roles
     * based on the provided query parameters and pagination settings.
     *
     * @param request the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id the ID of the role for which to retrieve sub-roles. Must be a valid and non-null Long value.
     * @param uriInfo UriInfo context to extract query parameters for filtering results.
     * @param queryFilter bean parameter encapsulating filtering and pagination criteria.
     * @return a Response object containing the requested page of AccessControlDirectDTO objects representing the
     * sub-roles of the specified role. The status of the response can vary based on the outcome of the request.
     */
    @GET
    @Path("/{id}/roles")
    public Response getSubRolesOfRole(final @Context HttpServletRequest request,
                                      final @PathParam("id") @Valid @NotNull Long id,
                                      final @Context UriInfo uriInfo,
                                      final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        Map<String, String> filters = this.getFilters(uriInfo);
        log.info("Received GET request to get sub roles of role {} with the following filters: {}", id, filters);
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
     * @param id the ID of the first role to which the second role will be associated. Must be a valid and non-null
     *           Long value.
     * @param roleId the ID of the second role to be associated with the first role. Must be a valid and non-null
     *               Long value.
     * @return a Response object indicating the outcome of the association operation. A successful operation returns
     * a status of CREATED.
     */
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Path("/{id}/roles")
    public Response associate(final @Context HttpServletRequest request,
                              final @PathParam("id") @Valid @NotNull Long id,
                              final @Valid @NotNull Long roleId) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("Received POST request to associate role {} with role {}", id, roleId);
        accessControlService.associate(AccessControlType.ROLE, id, AccessControlType.ROLE, roleId);

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
     * @param id the ID of the first role from which the second role will be dissociated. Must be a valid and non-null
     *           Long value.
     * @param roleId the ID of the second role to be dissociated from the first role. Must be a valid and non-null Long
     *               value.
     * @return a Response object with a status indicating the outcome of the dissociation operation. A successful
     * operation returns a status of NO_CONTENT.
     */
    @DELETE
    @Path("/{id}/roles/{roleId}")
    public Response dissociate(final @Context HttpServletRequest request,
                               final @PathParam("id") @Valid @NotNull Long id,
                               final @PathParam("roleId") @Valid @NotNull Long roleId) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("Received DELETE request to dissociate role {} with role {}", id, roleId);
        accessControlService.dissociate(AccessControlType.ROLE, id, AccessControlType.ROLE, roleId);

        return Response.noContent().build();
    }
}
