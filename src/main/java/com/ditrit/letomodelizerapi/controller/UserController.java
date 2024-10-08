package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.config.Constants;
import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.model.BeanMapper;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDTO;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDirectDTO;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import com.ditrit.letomodelizerapi.model.ai.AIConversationDTO;
import com.ditrit.letomodelizerapi.model.permission.PermissionDTO;
import com.ditrit.letomodelizerapi.model.user.UserDTO;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.AIService;
import com.ditrit.letomodelizerapi.service.AccessControlService;
import com.ditrit.letomodelizerapi.service.UserPermissionService;
import com.ditrit.letomodelizerapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import java.net.http.HttpResponse;
import java.util.Map;

/**
 * Controller to manage all users endpoints.
 */
@Slf4j
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Controller
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
     * Service to manage access controls.
     */
    private AIService aiService;

    /**
     * The maximum age for caching user pictures.
     * This value is typically specified in application properties and injected into the controller or service
     * to determine how long user pictures should be cached by clients. It is expressed in a format understood
     * by HTTP cache control headers, such as seconds.
     */
    private String userPictureCacheMaxAge;

    /**
     * Constructor for UserController.
     * Initializes the controller with necessary services for user management, permission checking, and access control.
     * It also configures the maximum age for user picture cache based on application properties.
     *
     * @param userService the service responsible for user-related operations.
     * @param userPermissionService the service for checking user permissions.
     * @param accessControlService the service for managing access controls.
     * @param aiService the service for managing AI functionality.
     * @param userPictureCacheMaxAge the maximum age for caching user pictures, injected from application properties.
     */
    @Autowired
    public UserController(final UserService userService,
                          final UserPermissionService userPermissionService,
                          final AccessControlService accessControlService,
                          final AIService aiService,
                          final @Value("${user.picture.cache.max.age}") String userPictureCacheMaxAge) {
        this.userService = userService;
        this.userPermissionService = userPermissionService;
        this.accessControlService = accessControlService;
        this.aiService = aiService;
        this.userPictureCacheMaxAge = userPictureCacheMaxAge;
    }

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

        log.info("[{}] Received GET request to get users with the following filters: {}", user.getLogin(), filters);
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
                                   final @PathParam(Constants.DEFAULT_USER_PROPERTY) @Valid @NotBlank String login) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received GET request to get user with login {}", user.getLogin(), login);
        UserDTO userDTO = new BeanMapper<>(UserDTO.class).apply(userService.findByLogin(login));

        return Response.ok(userDTO).build();
    }

    /**
     * Deletes a user identified by their login. This operation requires administrative privileges and ensures that
     * only authorized users can delete other user accounts from the system. Upon successful deletion, this method
     * returns a no-content response to indicate the operation's completion.
     *
     * @param request The HTTP request containing the user's session, used to verify administrative privileges.
     * @param login The login identifier of the user to be deleted.
     * @return A {@link Response} with a status of 204 No Content on successful deletion of the user.
     */
    @DELETE
    @Path("/{login}")
    public Response deleteUserByLogin(final @Context HttpServletRequest request,
                                      final @PathParam(Constants.DEFAULT_USER_PROPERTY) @Valid @NotBlank String login) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received DELETE request to delete user with login {}", user.getLogin(), login);
        userService.deleteByLogin(login);

        return Response.noContent().build();
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
                                   final @PathParam(Constants.DEFAULT_USER_PROPERTY) @Valid @NotBlank String login,
                                   final @Context UriInfo uriInfo,
                                   final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User me = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(me, null);

        Map<String, String> filters = this.getFilters(uriInfo);
        log.info(
                "[{}] Received GET request to get roles of user {} with the following filters: {}",
                me.getLogin(),
                login,
                filters
        );

        User user = userService.findByLogin(login);
        Page<AccessControlDirectDTO> resources = accessControlService
                .findAll(AccessControlType.ROLE, user, filters, queryFilter.getPagination());

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
                                    final @PathParam(Constants.DEFAULT_USER_PROPERTY) @Valid @NotBlank String login,
                                    final @Context UriInfo uriInfo,
                                    final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User me = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(me, null);

        Map<String, String> filters = this.getFilters(uriInfo);
        log.info(
                "[{}] Received GET request to get groups of user {} with the following filters: {}",
                me.getLogin(),
                login,
                filters
        );

        User user = userService.findByLogin(login);
        Page<AccessControlDTO> resources = accessControlService
                .findAll(AccessControlType.GROUP, user, filters, queryFilter.getPagination())
                .map(new BeanMapper<>(AccessControlDTO.class));

        return Response.status(this.getStatus(resources)).entity(resources).build();
    }

    /**
     * Retrieves permissions associated with a user identified by their login.
     *
     * <p>This method processes a GET request to fetch permissions assigned to a user, utilizing the user's login as the
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
    @Path("/{login}/permissions")
    public Response getPermissionsOfUser(final @Context HttpServletRequest request,
                                         final @PathParam(Constants.DEFAULT_USER_PROPERTY) @Valid @NotBlank
                                         String login,
                                         final @Context UriInfo uriInfo,
                                         final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        Map<String, String> filters = this.getFilters(uriInfo);
        User me = userService.getFromSession(session);

        log.info(
                "[{}] Received GET request to get permissions of user {} with the following filters: {}",
                me.getLogin(),
                login,
                filters
        );
        User user = userService.findByLogin(login);

        if (!me.getId().equals(user.getId())) {
            userPermissionService.checkIsAdmin(me, null);
        }

        Page<PermissionDTO> resources = userPermissionService
                .findAll(user, filters, queryFilter.getPagination())
                .map(new BeanMapper<>(PermissionDTO.class));

        return Response.status(this.getStatus(resources)).entity(resources).build();
    }

    /**
     * Retrieves scopes associated with a user identified by their login.
     *
     * <p>This method processes a GET request to fetch scopes assigned to a user, utilizing the user's login as the
     * identifier. It supports filtering based on query parameters and pagination. The operation requires administrative
     * privileges to execute, as it involves accessing sensitive user role information.
     *
     * @param request the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param login the login identifier of the user whose scopes are being requested. Must be a valid, non-blank
     *              String.
     * @param uriInfo UriInfo context to extract query parameters for filtering results.
     * @param queryFilter bean parameter encapsulating filtering and pagination criteria.
     * @return a Response object containing the requested page of AccessControlDTO objects representing the scopes
     * associated with the specified user. The response status varies based on the outcome of the request.
     */
    @GET
    @Path("/{login}/scopes")
    public Response getScopesOfUser(final @Context HttpServletRequest request,
                                    final @PathParam(Constants.DEFAULT_USER_PROPERTY) @Valid @NotBlank String login,
                                    final @Context UriInfo uriInfo,
                                    final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User me = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(me, null);

        Map<String, String> filters = this.getFilters(uriInfo);
        log.info(
                "[{}] Received GET request to get scopes of user {} with the following filters: {}",
                me.getLogin(),
                login,
                filters
        );

        User user = userService.findByLogin(login);
        Page<AccessControlDTO> resources = accessControlService
                .findAll(AccessControlType.SCOPE, user, filters, queryFilter.getPagination())
                .map(new BeanMapper<>(AccessControlDTO.class));

        return Response.status(this.getStatus(resources)).entity(resources).build();
    }

    /**
     * Retrieves scopes associated with a user identified by their login.
     *
     * <p>This method processes a GET request to fetch scopes assigned to a user, utilizing the user's login as the
     * identifier. It supports filtering based on query parameters and pagination. The operation requires administrative
     * privileges to execute, as it involves accessing sensitive user role information.
     *
     * @param request the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param login the login identifier of the user whose scopes are being requested. Must be a valid, non-blank
     *              String.
     * @param uriInfo UriInfo context to extract query parameters for filtering results.
     * @param queryFilter bean parameter encapsulating filtering and pagination criteria.
     * @return a Response object containing the requested page of AccessControlDTO objects representing the scopes
     * associated with the specified user. The response status varies based on the outcome of the request.
     */
    @GET
    @Path("/{login}/ai/conversations")
    public Response getAIConversationsOfUser(final @Context HttpServletRequest request,
                                    final @PathParam(Constants.DEFAULT_USER_PROPERTY) @Valid @NotBlank String login,
                                    final @Context UriInfo uriInfo,
                                    final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User me = userService.getFromSession(session);

        Map<String, String> filters = this.getFilters(uriInfo);
        log.info(
                "[{}] Received GET request to get AI conversations of user {} with the following filters: {}",
                me.getLogin(),
                login,
                filters
        );

        User user = userService.findByLogin(login);

        if (!me.getId().equals(user.getId())) {
            userPermissionService.checkIsAdmin(me, null);
        }

        Page<AIConversationDTO> resources = aiService.findAll(user, filters, queryFilter.getPagination())
                    .map(new BeanMapper<>(AIConversationDTO.class));

        return Response.status(this.getStatus(resources)).entity(resources).build();
    }

    /**
     * Retrieves the profile picture of a user identified by their login. Access to this endpoint is restricted to
     * users with administrative privileges. The method fetches the picture from the user service and returns it
     * along with the appropriate content type. If the user does not have a profile picture or if any error occurs
     * during retrieval, this method may return an error response or a default image, depending on the implementation.
     *
     * @param request The HTTP request containing the user's session, used to check for administrative privileges.
     * @param login The login identifier of the user whose picture is being requested.
     * @return A {@link Response} containing the user's profile picture as a byte array and the content type of the
     *         image. The response status and content type may vary based on the outcome of the request.
     */
    @GET
    @Path("/{login}/picture")
    public Response getPictureOfUser(final @Context HttpServletRequest request,
                                     final @PathParam(Constants.DEFAULT_USER_PROPERTY) @Valid @NotBlank String login) {
        HttpSession session = request.getSession();
        User me = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(me, null);

        log.info("[{}] Received GET request to get picture of user {}", me.getLogin(), login);

        User user = userService.findByLogin(login);

        HttpResponse<byte[]> response = userService.getPicture(user);
        String contentType = response.headers()
                .firstValue("Content-Type")
                .orElse("application/octet-stream");

        return Response.ok(response.body(), contentType).cacheControl(getCacheControl(userPictureCacheMaxAge)).build();
    }
}
