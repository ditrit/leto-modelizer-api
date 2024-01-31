package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.model.BeanMapper;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDTO;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import com.ditrit.letomodelizerapi.model.user.UserDTO;
import com.ditrit.letomodelizerapi.model.user.permission.UserPermissionDTO;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.AccessControlService;
import com.ditrit.letomodelizerapi.service.UserPermissionService;
import com.ditrit.letomodelizerapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

/**
 * Controller to manage all current user endpoints.
 */
@Path("/users/me")
@Produces(MediaType.APPLICATION_JSON)
@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CurrentUserController implements DefaultController {

    /**
     * Service to manage user.
     */
    private UserService userService;
    /**
     * Service to manage user permissions.
     */
    private UserPermissionService userPermissionService;
    /**
     * Service to manage access control.
     */
    private AccessControlService accessControlService;

    /**
     * Endpoint to retrieve all information of current user.
     * @param request Http request to get session.
     * @return Response with user information.
     */
    @GET
    public Response getMyInformation(final @Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);

        return Response
            .status(HttpStatus.OK.value())
            .entity(new BeanMapper<>(UserDTO.class).apply(user))
            .build();
    }

    /**
     * Endpoint to retrieve user picture from GitHub.
     * @param request Http request to get session.
     * @return Response with image in body.
     */
    @GET
    @Path("/picture")
    public Response getMyPicture(final @Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        HttpResponse<byte[]> response = userService.getPicture(user);
        String contentType = response.headers()
                .firstValue("Content-Type")
                .orElse("application/octet-stream");

        return Response.ok(response.body(), contentType).build();
    }

    /**
     * Endpoint to retrieve all permissions of current user.
     * @param request Http request to get session.
     * @return Response with permissions.
     */
    @GET
    @Path("/permissions")
    public Response getMyPermissions(final @Context HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        List<UserPermissionDTO> permissions = userPermissionService.getAllPermissions(user)
                .stream()
                .map(new BeanMapper<>(UserPermissionDTO.class))
                .toList();

        return Response.ok(permissions).build();
    }

    /**
     * Retrieves all roles associated to the current user.
     * This endpoint provides a paginated list of roles that the current user has, based on the provided query filters.
     * The method uses the session information from the HttpServletRequest to identify the current user and
     * then fetches their roles using the AccessControlService.
     *
     * @param request      HttpServletRequest to access the HTTP session and thereby identify the current user.
     * @param uriInfo      UriInfo to extract query parameters for additional filtering.
     * @param queryFilter  BeanParam object for pagination and filtering purposes.
     * @return a Response containing a paginated list of AccessControlDTO objects representing the roles of the current
     * user.
     */
    @GET
    @Path("/roles")
    public Response getMyRoles(final @Context HttpServletRequest request,
                               final @Context UriInfo uriInfo,
                               final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        Map<String, String> filters = this.getFilters(uriInfo);

        Page<AccessControlDTO> resources = accessControlService.findAll(
                AccessControlType.ROLE,
                user,
                filters,
                queryFilter.getPagination()
            ).map(new BeanMapper<>(AccessControlDTO.class));

        return Response.status(this.getStatus(resources)).entity(resources).build();
    }
}
