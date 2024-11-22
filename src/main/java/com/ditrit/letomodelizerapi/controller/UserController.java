package com.ditrit.letomodelizerapi.controller;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

/**
 * Controller to manage all users endpoints.
 */
@Slf4j
@RestController
@RequestMapping("/users")
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
     * @param filters All query parameters for filtering results.
     * @param queryFilter the filter criteria and pagination information.
     * @return A {@link ResponseEntity} containing a paginated list of {@link UserDTO}.
     */
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getUsers(final HttpServletRequest request,
                                                  final @RequestParam MultiValueMap<String, String> filters,
                                                  final @ModelAttribute QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received GET request to get users with the following filters: {}", user.getLogin(), filters);
        Page<UserDTO> resources = userService.findAll(filters, queryFilter)
                .map(new BeanMapper<>(UserDTO.class));

        return ResponseEntity.status(this.getStatus(resources)).body(resources);
    }

    /**
     * Retrieves the details of a specific user identified by their login.
     * Access to this endpoint is restricted to users with admin privileges.
     *
     * @param request The HTTP request containing the user's session.
     * @param login The login identifier of the user to retrieve.
     * @return A {@link ResponseEntity} containing the {@link UserDTO} of the requested user.
     */
    @GetMapping("/{login}")
    public ResponseEntity<UserDTO> getUserByLogin(final HttpServletRequest request,
                                   final @PathVariable String login) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received GET request to get user with login {}", user.getLogin(), login);
        UserDTO userDTO = new BeanMapper<>(UserDTO.class).apply(userService.findByLogin(login));

        return ResponseEntity.ok(userDTO);
    }

    /**
     * Deletes a user identified by their login. This operation requires administrative privileges and ensures that
     * only authorized users can delete other user accounts from the system. Upon successful deletion, this method
     * returns a no-content response to indicate the operation's completion.
     *
     * @param request The HTTP request containing the user's session, used to verify administrative privileges.
     * @param login The login identifier of the user to be deleted.
     * @return A {@link ResponseEntity} with a status of 204 No Content on successful deletion of the user.
     */
    @DeleteMapping("/{login}")
    public ResponseEntity<Object> deleteUserByLogin(final HttpServletRequest request,
                                               final @PathVariable String login) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received DELETE request to delete user with login {}", user.getLogin(), login);
        userService.deleteByLogin(login);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).contentType(MediaType.APPLICATION_JSON).build();
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
     * @param filters All query parameters for filtering results.
     * @param queryFilter the filter criteria and pagination information.
     * @return a Response object containing the requested page of AccessControlDTO objects representing the roles
     * associated with the specified user. The response status varies based on the outcome of the request.
     */
    @GetMapping("/{login}/roles")
    public ResponseEntity<Page<AccessControlDirectDTO>> getRolesOfUser(
            final HttpServletRequest request,
            final @PathVariable String login,
            final @RequestParam MultiValueMap<String, String> filters,
            final @ModelAttribute QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User me = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(me, null);

        log.info(
                "[{}] Received GET request to get roles of user {} with the following filters: {}",
                me.getLogin(),
                login,
                filters
        );

        User user = userService.findByLogin(login);
        Page<AccessControlDirectDTO> resources = accessControlService
                .findAll(AccessControlType.ROLE, user, filters, queryFilter);

        return ResponseEntity.status(this.getStatus(resources)).body(resources);
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
     * @param filters All query parameters for filtering results.
     * @param queryFilter the filter criteria and pagination information.
     * @return a Response object containing the requested page of AccessControlDTO objects representing the groups
     * associated with the specified user. The response status varies based on the outcome of the request.
     */
    @GetMapping("/{login}/groups")
    public ResponseEntity<Page<AccessControlDTO>> getGroupsOfUser(
            final HttpServletRequest request,
            final @PathVariable String login,
            final @RequestParam MultiValueMap<String, String> filters,
            final @ModelAttribute QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User me = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(me, null);

        log.info(
                "[{}] Received GET request to get groups of user {} with the following filters: {}",
                me.getLogin(),
                login,
                filters
        );

        User user = userService.findByLogin(login);
        Page<AccessControlDTO> resources = accessControlService
                .findAll(AccessControlType.GROUP, user, filters, queryFilter)
                .map(new BeanMapper<>(AccessControlDTO.class));

        return ResponseEntity.status(this.getStatus(resources)).body(resources);
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
     * @param filters All query parameters for filtering results.
     * @param queryFilter the filter criteria and pagination information.
     * @return a Response object containing the requested page of AccessControlDTO objects representing the groups
     * associated with the specified user. The response status varies based on the outcome of the request.
     */
    @GetMapping("/{login}/permissions")
    public ResponseEntity<Page<PermissionDTO>> getPermissionsOfUser(
            final HttpServletRequest request,
            final @PathVariable String login,
            final @RequestParam MultiValueMap<String, String> filters,
            final @ModelAttribute QueryFilter queryFilter) {
        HttpSession session = request.getSession();
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
                .findAll(user, filters, queryFilter)
                .map(new BeanMapper<>(PermissionDTO.class));

        return ResponseEntity.status(this.getStatus(resources)).body(resources);
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
     * @param filters All query parameters for filtering results.
     * @param queryFilter the filter criteria and pagination information.
     * @return a Response object containing the requested page of AccessControlDTO objects representing the scopes
     * associated with the specified user. The response status varies based on the outcome of the request.
     */
    @GetMapping("/{login}/scopes")
    public ResponseEntity<Page<AccessControlDTO>> getScopesOfUser(
            final HttpServletRequest request,
            final @PathVariable String login,
            final @RequestParam MultiValueMap<String, String> filters,
            final @ModelAttribute QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User me = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(me, null);

        log.info(
                "[{}] Received GET request to get scopes of user {} with the following filters: {}",
                me.getLogin(),
                login,
                filters
        );

        User user = userService.findByLogin(login);
        Page<AccessControlDTO> resources = accessControlService
                .findAll(AccessControlType.SCOPE, user, filters, queryFilter)
                .map(new BeanMapper<>(AccessControlDTO.class));

        return ResponseEntity.status(this.getStatus(resources)).body(resources);
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
     * @param filters All query parameters for filtering results.
     * @param queryFilter the filter criteria and pagination information.
     * @return a Response object containing the requested page of AccessControlDTO objects representing the scopes
     * associated with the specified user. The response status varies based on the outcome of the request.
     */
    @GetMapping("/{login}/ai/conversations")
    public ResponseEntity<Page<AIConversationDTO>> getAIConversationsOfUser(
            final HttpServletRequest request,
            final @PathVariable String login,
            final @RequestParam MultiValueMap<String, String> filters,
            final @ModelAttribute QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User me = userService.getFromSession(session);

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

        Page<AIConversationDTO> resources = aiService.findAll(user, filters, queryFilter)
                    .map(new BeanMapper<>(AIConversationDTO.class));

        return ResponseEntity.status(this.getStatus(resources)).body(resources);
    }

    /**
     * Retrieves the profile picture of a user identified by their login. Access to this endpoint is restricted to
     * users with administrative privileges. The method fetches the picture from the user service and returns it
     * along with the appropriate content type. If the user does not have a profile picture or if any error occurs
     * during retrieval, this method may return an error response or a default image, depending on the implementation.
     *
     * @param request The HTTP request containing the user's session, used to check for administrative privileges.
     * @param login The login identifier of the user whose picture is being requested.
     * @return A {@link ResponseEntity} containing the user's profile picture as a byte array and the content type of
     * the image. The response status and content type may vary based on the outcome of the request.
     */
    @GetMapping("/{login}/picture")
    public ResponseEntity<byte[]> getPictureOfUser(final HttpServletRequest request,
                                     final @PathVariable String login) {
        HttpSession session = request.getSession();
        User me = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(me, null);

        log.info("[{}] Received GET request to get picture of user {}", me.getLogin(), login);

        User user = userService.findByLogin(login);

        HttpResponse<byte[]> response = userService.getPicture(user);
        String contentType = response.headers()
                .firstValue("Content-Type")
                .orElse("application/octet-stream");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setCacheControl(getCacheControl(userPictureCacheMaxAge));

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(response.body());
    }
}
