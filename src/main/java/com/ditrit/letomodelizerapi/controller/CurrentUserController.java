package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.model.BeanMapper;
import com.ditrit.letomodelizerapi.model.user.UserDTO;
import com.ditrit.letomodelizerapi.model.user.permission.UserPermissionDTO;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.UserPermissionService;
import com.ditrit.letomodelizerapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.net.http.HttpResponse;
import java.util.List;

/**
 * Controller to manage all users endpoints.
 */
@Path("/users/me")
@Produces(MediaType.APPLICATION_JSON)
@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CurrentUserController {

    /**
     * Service to manage user.
     */
    private UserService userService;
    /**
     * Service to manage user permissions.
     */
    private UserPermissionService userPermissionService;

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
}
