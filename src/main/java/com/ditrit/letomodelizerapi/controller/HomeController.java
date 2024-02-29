package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Controller to manage '/' endpoint.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Controller
@Slf4j
public class HomeController {

    /**
     * Service to manage user.
     */
    private UserService userService;

    /**
     * Configuration setting specifying the URL used to access the LetoModelizer application.
     */
    private String letoModelizerUrl;

    /**
     * Configuration setting specifying the URL used to access the LetoModelizer-admin application.
     */
    private String letoAdminUrl;

    /**
     * Constructor for HomeController.
     * Initializes the controller with the necessary services and configuration values. It sets up the UserService
     * for user management and operations, and it also configures URLs for the Leto Modelizer and Leto Admin pages,
     * which are injected from the application's properties. This setup enables the HomeController to provide
     * functionalities related to the home page, possibly including links to other parts of the application or
     * services such as the Leto Modelizer and Leto Admin interfaces.
     *
     * @param userService the UserService responsible for user-related functionalities.
     * @param letoModelizerUrl the URL to the Leto Modelizer service, injected from application properties.
     * @param letoAdminUrl the URL to the Leto Admin interface, also injected from application properties.
     */
    @Autowired
    public HomeController(final UserService userService,
                          final @Value("${leto.modelizer.url}") String letoModelizerUrl,
                          final @Value("${leto.admin.url}")String letoAdminUrl) {
        this.userService = userService;
        this.letoModelizerUrl = letoModelizerUrl;
        this.letoAdminUrl = letoAdminUrl;
    }

    /**
     * Returns the page displayed upon successful authentication.
     *
     * This method retrieves a static page that performs a redirection to the LetoModelizer
     * application when the user's authentication is successful. It serves as a post-login
     * landing page indicating a successful login process.
     *
     * @return A static page that redirects to LetoModelizer upon successful authentication.
     */
    @GET
    @PermitAll
    public Response home() {
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("index.html");

        return Response.ok(stream).type("text/html").build();
    }

    /**
     * Redirects the user to a specified authorized application.
     *
     * This method handles the redirection of a user to different applications based on the
     * provided query parameter 'app'. It supports dynamic redirection to various parts of
     * the system, such as the admin interface or the main LetoModelizer application. For
     * instance, if the 'app' parameter is set to 'admin', the user will be redirected to
     * the LetoAdmin URL, subject to access control checks.
     *
     * @param request HttpServletRequest to access the HTTP session and thereby identify the current user.
     * @param app     A query parameter that specifies the application to redirect to.
     *                This parameter dictates the target URL for redirection.
     * @return A Response object that redirects the user to the wanted application.
     */
    @GET
    @Path("/redirect")
    public Response redirect(final @Context HttpServletRequest request,
                             final @QueryParam("app") String app) throws URISyntaxException {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        String url = letoModelizerUrl;

        if ("admin".equalsIgnoreCase(app)) {
            url = letoAdminUrl;
        }

        log.info("[{}] Received GET request to redirect to {}", user.getLogin(), url);

        return Response.seeOther(new URI(url)).build();
    }

    /**
     * Provides a simplified routing mechanism for GitHub login.
     *
     * This method streamlines the process of initiating a GitHub login by redirecting the user
     * to the OAuth2 authorization endpoint specifically for GitHub. It acts as a convenience
     * endpoint, abstracting the more complex URL for easier access and a smoother user experience.
     *
     * @return A Response object that redirects the user to the GitHub OAuth2 authorization endpoint.
     */
    @GET
    @Path("/login")
    public Response login() {
        return Response.temporaryRedirect(URI.create("/api/oauth2/authorization/github")).build();
    }
}
