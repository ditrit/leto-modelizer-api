package com.ditrit.letomodelizerapi.controller;


import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.persistence.model.Permission;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.PermissionService;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * REST Controller for managing permissions.
 * Provides endpoint to list permissions.
 * Only accessible by users with administrative permissions.
 */
@Slf4j
@Path("/permissions")
@Produces(MediaType.APPLICATION_JSON)
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PermissionController implements DefaultController {

    /**
     * Service to manage user.
     */
    private final UserService userService;

    /**
     * Service to manage user permissions.
     */
    private final UserPermissionService userPermissionService;

    /**
     * Service to manage permissions.
     */
    private final PermissionService permissionService;

    /**
     * Finds and returns all permissions based on the provided query filters.
     * Only accessible by users with administrative permissions.
     *
     * @param request      HttpServletRequest to access the HTTP session
     * @param uriInfo      UriInfo to extract query parameters
     * @param queryFilter  BeanParam for pagination and filtering
     * @return a Response containing a page of Permission objects
     */
    @GET
    public Response findAll(final @Context HttpServletRequest request,
                            final @Context UriInfo uriInfo,
                            final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);
        Map<String, String> filters = this.getFilters(uriInfo);

        log.info("Received GET request to get permissions with the following filters: {}", filters);
        Page<Permission> resources = permissionService.findAll(filters, queryFilter.getPagination());

        return Response.status(this.getStatus(resources)).entity(resources).build();
    }
}
