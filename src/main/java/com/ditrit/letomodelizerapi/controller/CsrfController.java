package com.ditrit.letomodelizerapi.controller;


import com.ditrit.letomodelizerapi.model.csrf.UserCsrfTokenDTO;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.persistence.model.UserCsrfToken;
import com.ditrit.letomodelizerapi.service.UserCsrfTokenService;
import com.ditrit.letomodelizerapi.service.UserPermissionService;
import com.ditrit.letomodelizerapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to manage '/csrf' endpoint.
 */
@Slf4j
@RestController
@RequestMapping("/csrf")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CsrfController {

    /**
     * Service to manage user.
     */
    private UserService userService;

    /**
     * Service to manage user permissions.
     */
    private UserPermissionService userPermissionService;

    /**
     * Service to manage user.
     */
    private UserCsrfTokenService userCsrfTokenService;

    /**
     * Retrieves the CSRF (Cross-Site Request Forgery) token for the current user session and encapsulates it in a
     * UserCsrfTokenDTO.
     * This endpoint is secured to ensure that only users with administrative permissions can access their CSRF token.
     * The CSRF token is fetched from the HttpServletRequest's attributes and then mapped to a UserCsrfTokenDTO,
     * which is returned to the client. This process includes checking if the user has administrative privileges and
     * retrieving the user-specific CSRF token from the service layer.
     *
     * @param request the HttpServletRequest from which to retrieve the current user session and CSRF token.
     * @return a Response object containing the UserCsrfTokenDTO, which includes the CSRF token details.
     */
    @GetMapping
    public ResponseEntity<UserCsrfTokenDTO> getCsrfToken(final HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received GET request to get csrf token", user.getLogin());

        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");

        UserCsrfTokenDTO token = new UserCsrfTokenDTO();
        token.setToken(csrfToken.getToken());
        token.setHeaderName(csrfToken.getHeaderName());

        UserCsrfToken userToken = userCsrfTokenService.findByLogin(user.getLogin());

        token.setExpirationDate(userToken.getExpirationDate());

        return ResponseEntity.ok(token);
    }
}
