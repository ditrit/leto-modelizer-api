package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.model.BeanMapper;
import com.ditrit.letomodelizerapi.model.user.UserDTO;
import com.ditrit.letomodelizerapi.persistence.model.User;
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
}
