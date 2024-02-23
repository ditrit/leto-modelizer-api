package com.ditrit.letomodelizerapi.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Controller to manage '/' endpoint.
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Controller
public class HomeController {

    /**
     * Configuration setting specifying the URL used to access the LetoModelizer application.
     */
    @Value("${leto.modelizer.url}")
    private String letoModelizerUrl;

    /**
     * Configuration setting specifying the URL used to access the LetoModelizer-admin application.
     */
    @Value("${leto.admin.url}")
    private String letoAdminUrl;

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
     * @param app A query parameter that specifies the application to redirect to.
     *            This parameter dictates the target URL for redirection.
     * @return A Response object that redirects the user to the wanted application.
     * @throws IOException If an input or output exception occurs during the redirection process.
     */
    @GET
    @Path("/redirect")
    public Response redirect(final @QueryParam("app") String app) throws IOException, URISyntaxException {
        String url = letoModelizerUrl;

        if ("admin".equalsIgnoreCase(app)) {
            url = letoAdminUrl;
        }

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
