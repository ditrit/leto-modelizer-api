package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.model.BeanMapper;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDTO;
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
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
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
import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * Controller to manage all users endpoints.
 */
@Slf4j
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserController implements DefaultController {

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
     * Retrieves a paginated list of users based on the provided query filters.
     * This endpoint requires the requesting user to have admin privileges.
     *
     * @param request The HTTP request containing the user's session.
     * @param uriInfo URI information containing query parameters for filtering.
     * @param queryFilter Bean parameter for valid query filters including pagination.
     * @return A {@link Response} containing a paginated list of {@link UserDTO}.
     */
    @GET
    public Response getUsers(final @Context HttpServletRequest request,
                             final @Context UriInfo uriInfo,
                             final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);
        Map<String, String> filters = this.getFilters(uriInfo);

        log.info("Received GET request to get users with the following filters: {}", filters);
        Page<UserDTO> resources = userService.findAll(filters, queryFilter.getPagination())
                .map(new BeanMapper<>(UserDTO.class));

        return Response.status(this.getStatus(resources)).entity(resources).build();
    }

    /**
     * Retrieves the details of a specific user identified by their login.
     * Access to this endpoint is restricted to users with admin privileges.
     *
     * @param request The HTTP request containing the user's session.
     * @param login The login identifier of the user to retrieve.
     * @return A {@link Response} containing the {@link UserDTO} of the requested user.
     */
    @GET
    @Path("/{login}")
    public Response getUserByLogin(final @Context HttpServletRequest request,
                                   final @PathParam("login") @Valid @NotBlank String login) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("Received GET request to get user with login {}", login);
        UserDTO userDTO = new BeanMapper<>(UserDTO.class).apply(userService.findByLogin(login));

        return Response.ok(userDTO).build();
    }

    /**
     * Retrieves roles associated with a user identified by their login.
     *
     * <p>This method processes a GET request to fetch roles assigned to a user, utilizing the user's login as the
     * identifier. It supports filtering based on query parameters and pagination. The operation requires administrative
     * privileges to execute, as it involves accessing sensitive user role information.
     *
     * @param request the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param login the login identifier of the user whose roles are being requested. Must be a valid, non-blank String.
     * @param uriInfo UriInfo context to extract query parameters for filtering results.
     * @param queryFilter bean parameter encapsulating filtering and pagination criteria.
     * @return a Response object containing the requested page of AccessControlDTO objects representing the roles
     * associated with the specified user. The response status varies based on the outcome of the request.
     */
    @GET
    @Path("/{login}/roles")
    public Response getRolesOfUser(final @Context HttpServletRequest request,
                                   final @PathParam("login") @Valid @NotBlank String login,
                                   final @Context UriInfo uriInfo,
                                   final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User me = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(me, null);

        Map<String, String> filters = this.getFilters(uriInfo);
        log.info("Received GET request to get roles of user {} with the following filters: {}", login, filters);

        User user = userService.findByLogin(login);
        Page<AccessControlDTO> resources = accessControlService
                .findAll(AccessControlType.ROLE, user, filters, queryFilter.getPagination())
                .map(new BeanMapper<>(AccessControlDTO.class));

        return Response.status(this.getStatus(resources)).entity(resources).build();
    }

    /**
     * Retrieves groups associated with a user identified by their login.
     *
     * <p>This method processes a GET request to fetch groups assigned to a user, utilizing the user's login as the
     * identifier. It supports filtering based on query parameters and pagination. The operation requires administrative
     * privileges to execute, as it involves accessing sensitive user role information.
     *
     * @param request the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param login the login identifier of the user whose groups are being requested. Must be a valid, non-blank
     *              String.
     * @param uriInfo UriInfo context to extract query parameters for filtering results.
     * @param queryFilter bean parameter encapsulating filtering and pagination criteria.
     * @return a Response object containing the requested page of AccessControlDTO objects representing the groups
     * associated with the specified user. The response status varies based on the outcome of the request.
     */
    @GET
    @Path("/{login}/groups")
    public Response getGroupsOfUser(final @Context HttpServletRequest request,
                                    final @PathParam("login") @Valid @NotBlank String login,
                                    final @Context UriInfo uriInfo,
                                    final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User me = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(me, null);

        Map<String, String> filters = this.getFilters(uriInfo);
        log.info("Received GET request to get groups of user {} with the following filters: {}", login, filters);

        User user = userService.findByLogin(login);
        Page<AccessControlDTO> resources = accessControlService
                .findAll(AccessControlType.GROUP, user, filters, queryFilter.getPagination())
                .map(new BeanMapper<>(AccessControlDTO.class));

        return Response.status(this.getStatus(resources)).entity(resources).build();
    }
}
