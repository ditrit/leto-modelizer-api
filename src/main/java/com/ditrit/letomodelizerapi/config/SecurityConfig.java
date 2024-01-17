package com.ditrit.letomodelizerapi.config;

import com.ditrit.letomodelizerapi.controller.handler.AuthenticationSuccessHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration class for Spring Security.
 * <p>The class defines security configurations for HTTP requests in a Spring Boot application, particularly
 *  configuring OAuth2 GitGub login functionality.
 */
@Slf4j
@Configuration
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfig {

    /**
     * Service to authenticate user.
     */
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;

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
        return http.build();
    }
}
