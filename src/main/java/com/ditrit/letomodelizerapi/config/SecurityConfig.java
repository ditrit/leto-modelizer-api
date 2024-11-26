package com.ditrit.letomodelizerapi.config;

import com.ditrit.letomodelizerapi.controller.handler.AuthenticationSuccessHandler;
import com.ditrit.letomodelizerapi.persistence.repository.DatabaseCsrfTokenRepository;
import com.ditrit.letomodelizerapi.persistence.repository.UserCsrfTokenRepository;
import com.ditrit.letomodelizerapi.service.OAuth2UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for Spring Security.
 * <p>The class defines security configurations for HTTP requests in a Spring Boot application, particularly
 *  configuring OAuth2 GitGub login functionality.
 */
@Slf4j
@Configuration
public class SecurityConfig {

    /**
     * Service to authenticate user.
     */
    private final OAuth2UserService oAuth2UserService;

    /**
     * Repository for CRUD operations on UserCsrfToken entities.
     */
    private final UserCsrfTokenRepository userCsrfTokenRepository;

    /**
     * Timeout duration for CSRF tokens. Tokens older than this duration will be considered expired.
     */
    private final long csrfTokenTimeout;

    /**
     * Constructor for SecurityConfig.
     * Initializes the security configuration with the necessary components for OAuth2 user authentication,
     * CSRF token management, and configuring CSRF token timeout. This setup ensures that the application
     * is secured with OAuth2 authentication mechanism and protects against CSRF attacks by managing CSRF tokens
     * effectively.
     *
     * @param oAuth2UserService the OAuth2 user service for loading user details during OAuth2 authentication.
     * @param userCsrfTokenRepository the repository for managing CSRF tokens associated with users.
     * @param csrfTokenTimeout the timeout value for CSRF tokens, injected from application properties,
     *        indicating how long (in seconds) the tokens are considered valid.
     */
    @Autowired
    public SecurityConfig(final OAuth2UserService oAuth2UserService,
                          final UserCsrfTokenRepository userCsrfTokenRepository,
                          @Value("${csrf.token.timeout}") final long csrfTokenTimeout) {
        this.oAuth2UserService = oAuth2UserService;
        this.userCsrfTokenRepository = userCsrfTokenRepository;
        this.csrfTokenTimeout = csrfTokenTimeout;
    }

    /**
     * Setup security on http request.
     * @param http Http request to set security.
     * @return Applied securities on http request.
     * @throws Exception On applied security.
     */
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
        log.trace("Configuring http filterChain");
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/login")
                .permitAll()
                .anyRequest()
                .authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .userInfoEndpoint(infoEndpoint -> infoEndpoint.userService(oAuth2UserService))
                .successHandler(new AuthenticationSuccessHandler())
            )
            .exceptionHandling(handler -> handler.authenticationEntryPoint(new DefaultAuthenticationEntryPoint()));

        http.csrf(csrf -> csrf
                .csrfTokenRepository(new DatabaseCsrfTokenRepository(userCsrfTokenRepository, csrfTokenTimeout)));

        return http.build();
    }
}
