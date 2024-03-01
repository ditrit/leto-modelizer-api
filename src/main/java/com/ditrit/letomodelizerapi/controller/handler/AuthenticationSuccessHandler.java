package com.ditrit.letomodelizerapi.controller.handler;

import com.ditrit.letomodelizerapi.config.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

/**
 * Custom authentication success handler extending {@link SimpleUrlAuthenticationSuccessHandler}.
 *
 * <p>This handler is invoked upon successful authentication. It extends {@link SimpleUrlAuthenticationSuccessHandler}
 * from Spring Security framework, providing additional behavior to process after the user is authenticated.
 *
 * <p>The main purpose of this class is to handle the post-authentication process, which involves extracting user
 * details (like email) from the {@link OAuth2AuthenticationToken} and storing them in the HTTP session.
 */
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request,
                                        final HttpServletResponse response,
                                        final Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        // Extract user details and store in session
        String login = oauthToken.getPrincipal().getAttribute(Constants.DEFAULT_USER_PROPERTY);
        session.setAttribute(Constants.DEFAULT_USER_PROPERTY, login);

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
