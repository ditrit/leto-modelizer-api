package com.ditrit.letomodelizerapi.controller;


import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.persistence.model.Permission;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.PermissionService;
import com.ditrit.letomodelizerapi.service.UserPermissionService;
import com.ditrit.letomodelizerapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller for managing permissions.
 * Provides endpoint to list permissions.
 * Only accessible by users with administrative permissions.
 */
@Slf4j
@RestController
@RequestMapping("/permissions")
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
     * @param filters     All query parameters for filtering results.
     * @param queryFilter the filter criteria and pagination information.
     * @return a Response containing a page of Permission objects
     */
    @GetMapping
    public ResponseEntity<Page<Permission>> findAll(
            final HttpServletRequest request,
            final @RequestParam MultiValueMap<String, String> filters,
            final @ModelAttribute QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info(
                "[{}] Received GET request to get permissions with the following filters: {}",
                user.getLogin(),
                filters
        );

        Page<Permission> resources = permissionService.findAll(filters, queryFilter);

        return ResponseEntity.status(this.getStatus(resources)).body(resources);
    }
}
