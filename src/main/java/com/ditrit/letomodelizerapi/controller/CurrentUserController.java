package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.model.BeanMapper;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDTO;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;
import java.util.List;

/**
 * Controller to manage all current user endpoints.
 */
@RestController
@RequestMapping("/users/me")
@Slf4j
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
     * Service to manage AI request.
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
     * Constructor for CurrentUserController.
     * Initializes the controller with the necessary services for managing current user information, including user
     * management, permission checking, and access control. It also sets the configuration for the maximum age of user
     * picture caching based on the application's properties. This setup allows for effective handling of operations
     * related to the current user, such as fetching user details, validating permissions, and managing access controls,
     * while also considering performance optimizations through caching strategies for user pictures.
     *
     * @param userService the service responsible for managing user details and operations.
     * @param userPermissionService the service for checking and validating user permissions.
     * @param accessControlService the service for managing access controls and security.
     * @param aiService the service for managing AI request.
     * @param userPictureCacheMaxAge the configured maximum age for caching user pictures, specified in application
     *                               properties, indicating how long client browsers should cache user pictures before
     *                               requesting them again.
     */
    @Autowired
    public CurrentUserController(final UserService userService,
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
     * Endpoint to retrieve all information of current user.
     * @param request Http request to get session.
     * @return Response with user information.
     */
    @GetMapping
    public ResponseEntity<UserDTO> getMyInformation(final HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);

        log.info("[{}] Received GET request to get current user information", user.getLogin());

        return ResponseEntity.ok(new BeanMapper<>(UserDTO.class).apply(user));
    }

    /**
     * Endpoint to retrieve user picture from GitHub.
     * @param request Http request to get session.
     * @return Response with image in body.
     */
    @GetMapping("/picture")
    public ResponseEntity<byte[]> getMyPicture(final HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        HttpResponse<byte[]> response = userService.getPicture(user);
        String contentType = response.headers()
                .firstValue("Content-Type")
                .orElse("application/octet-stream");

        log.info("[{}] Received GET request to get current user picture", user.getLogin());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setCacheControl(getCacheControl(userPictureCacheMaxAge));

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(response.body());
    }

    /**
     * Endpoint to retrieve all permissions of current user.
     * @param request Http request to get session.
     * @return Response with permissions.
     */
    @GetMapping("/permissions")
    public ResponseEntity<List<PermissionDTO>> getMyPermissions(final HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);

        log.info("[{}] Received GET request to get current user permissions", user.getLogin());

        List<PermissionDTO> permissions = userPermissionService.getAllPermissions(user)
                .stream()
                .map(new BeanMapper<>(PermissionDTO.class))
                .toList();

        return ResponseEntity.ok(permissions);
    }

    /**
     * Retrieves all roles associated to the current user.
     * This endpoint provides a paginated list of roles that the current user has, based on the provided query filters.
     * The method uses the session information from the HttpServletRequest to identify the current user and
     * then fetches their roles using the AccessControlService.
     *
     * @param request      HttpServletRequest to access the HTTP session and thereby identify the current user.
     * @param filters     All query parameters for filtering results.
     * @param queryFilter the filter criteria and pagination information.
     * @return a Response containing a paginated list of AccessControlDTO objects representing the roles of the current
     * user.
     */
    @GetMapping("/roles")
    public ResponseEntity<Page<AccessControlDTO>> getMyRoles(final HttpServletRequest request,
                                                             final @RequestParam MultiValueMap<String, String> filters,
                                                             final @ModelAttribute QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);

        log.info(
                "[{}] Received GET request to get current user roles with the following filters: {}",
                user.getLogin(),
                filters
        );

        Page<AccessControlDTO> resources = accessControlService.findAll(
                AccessControlType.ROLE,
                user,
                filters,
                queryFilter
        ).map(new BeanMapper<>(AccessControlDTO.class));

        return ResponseEntity.status(this.getStatus(resources)).body(resources);
    }

    /**
     * Retrieves all groups associated to the current user.
     * This endpoint provides a paginated list of groups that the current user has, based on the provided query filters.
     * The method uses the session information from the HttpServletRequest to identify the current user and
     * then fetches their roles using the AccessControlService.
     *
     * @param request     HttpServletRequest to access the HTTP session and thereby identify the current user.
     * @param filters     All query parameters for filtering results.
     * @param queryFilter the filter criteria and pagination information.
     * @return a Response containing a paginated list of AccessControlDTO objects representing the roles of the current
     * user.
     */
    @GetMapping("/groups")
    public ResponseEntity<Page<AccessControlDTO>> getMyGroups(final HttpServletRequest request,
                                                              final @RequestParam MultiValueMap<String, String> filters,
                                                              final @ModelAttribute QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);

        log.info(
                "[{}] Received GET request to get current user groups with the following filters: {}",
                user.getLogin(),
                filters
        );

        Page<AccessControlDTO> resources = accessControlService.findAll(
                AccessControlType.GROUP,
                user,
                filters,
                queryFilter
        ).map(new BeanMapper<>(AccessControlDTO.class));

        return ResponseEntity.status(this.getStatus(resources)).body(resources);
    }

    /**
     * Retrieves all scopes associated to the current user.
     * This endpoint provides a paginated list of scopes that the current user has, based on the provided query filters.
     * The method uses the session information from the HttpServletRequest to identify the current user and
     * then fetches their scopes using the AccessControlService.
     *
     * @param request     HttpServletRequest to access the HTTP session and thereby identify the current user.
     * @param filters     All query parameters for filtering results.
     * @param queryFilter the filter criteria and pagination information.
     * @return a Response containing a paginated list of AccessControlDTO objects representing the scopes of the current
     * user.
     */
    @GetMapping("/scopes")
    public ResponseEntity<Page<AccessControlDTO>> getMyScopes(final HttpServletRequest request,
                                                              final @RequestParam MultiValueMap<String, String> filters,
                                                              final @ModelAttribute QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);

        log.info(
                "[{}] Received GET request to get current user scopes with the following filters: {}",
                user.getLogin(),
                filters
        );

        Page<AccessControlDTO> resources = accessControlService.findAll(
                AccessControlType.SCOPE,
                user,
                filters,
                queryFilter
        ).map(new BeanMapper<>(AccessControlDTO.class));

        return ResponseEntity.status(this.getStatus(resources)).body(resources);
    }

    /**
     * Retrieves all AI conversations associated to the current user.
     * This endpoint provides a paginated list of AI conversations that the current user has, based on the provided
     * query filters.
     * The method uses the session information from the HttpServletRequest to identify the current user and
     * then fetches their AI conversations using the AIService.
     *
     * @param request     HttpServletRequest to access the HTTP session and thereby identify the current user.
     * @param filters     All query parameters for filtering results.
     * @param queryFilter the filter criteria and pagination information.
     * @return a Response containing a paginated list of AIConversationDTO objects representing the AI conversation of
     * the current user.
     */
    @GetMapping("/ai/conversations")
    public ResponseEntity<Page<AIConversationDTO>> getMyAIConversations(
            final HttpServletRequest request,
            final @RequestParam MultiValueMap<String, String> filters,
            final @ModelAttribute QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);

        log.info(
                "[{}] Received GET request to get current user AI conversations with the following filters: {}",
                user.getLogin(),
                filters
        );

        Page<AIConversationDTO> resources = aiService.findAll(
                user,
                filters,
                queryFilter
        ).map(new BeanMapper<>(AIConversationDTO.class));

        return ResponseEntity.status(this.getStatus(resources)).body(resources);
    }
}
