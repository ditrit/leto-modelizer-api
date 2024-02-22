package com.ditrit.letomodelizerapi.persistence.repository;

import com.ditrit.letomodelizerapi.persistence.model.UserCsrfToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;

import java.security.SecureRandom;
import java.time.Instant;

/**
 * Service implementation for managing CSRF (Cross-Site Request Forgery) tokens for users.
 * This class provides functionality to generate, save, load, and validate CSRF tokens, interfacing with the
 * UserCsrfTokenRepository for persistence. Tokens are tied to user sessions and are used to prevent CSRF attacks.
 */
@Slf4j
@AllArgsConstructor
public class DatabaseCsrfTokenRepository implements CsrfTokenRepository {

    /**
     * The length of the CSRF token string to be generated.
     */
    private static final int TOKEN_LENGTH = 255;

    /**
     * Repository for CRUD operations on UserCsrfToken entities.
     */
    private final UserCsrfTokenRepository userCsrfTokenRepository;

    /**
     * Timeout duration for CSRF tokens. Tokens older than this duration will be considered expired.
     */
    private final long csrfTokenTimeout;

    @Override
    public CsrfToken generateToken(final HttpServletRequest request) {
        String login = (String) request.getSession().getAttribute("login");

        log.info("Generate token for user {}", login);

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@-_+=!?.:[]{}()~";
        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder();

        while (builder.length() < TOKEN_LENGTH) {
            builder.append(characters.charAt(random.nextInt(characters.length())));
        }

        return new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", builder.toString());
    }

    @Override
    public void saveToken(final CsrfToken token, final HttpServletRequest request, final HttpServletResponse response) {
        String login = (String) request.getSession().getAttribute("login");

        if (token == null) {
            log.info("Delete previous token for user {}", login);
            userCsrfTokenRepository.deleteByLogin(login);
            return;
        }

        log.info("Save token for user {}", login);
        UserCsrfToken userToken = userCsrfTokenRepository.findByLogin(login)
                .orElse(new UserCsrfToken(this.csrfTokenTimeout));

        userToken.setLogin(login);
        userToken.setToken(token.getToken());

        userCsrfTokenRepository.save(userToken);
    }

    @Override
    public CsrfToken loadToken(final HttpServletRequest request) {
        String login = (String) request.getSession().getAttribute("login");

        if (login == null) {
            return null;
        }

        log.info("Load token for user {}", login);
        UserCsrfToken token = userCsrfTokenRepository.findByLogin(login).orElse(null);

        if (token == null) {
            return null;
        }

        if (isExpired(token)) {
            userCsrfTokenRepository.delete(token);
            log.info("Delete expired token for user {}", login);
            return null;
        }

        return new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", token.getToken());

    }

    /**
     * Checks if the provided UserCsrfToken is expired based on its update date and the configured token timeout.
     *
     * @param token the UserCsrfToken to check for expiration
     * @return true if the token is expired, false otherwise
     */
    boolean isExpired(final UserCsrfToken token) {
        return Instant.now().isAfter(token.getExpirationDate().toInstant());
    }
}
