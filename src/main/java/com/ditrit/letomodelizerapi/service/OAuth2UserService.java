package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.user.UserRecord;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * Custom implementation of the DefaultOAuth2UserService to handle OAuth2 user information.
 * This class extends the default user service to integrate with the application's user management,
 * allowing the saving and updating of user details during the OAuth2 authentication process.
 *
 * It is used for the authentication flow to load user-specific data from the OAuth2 provider
 * and then save or update this information in the application's own user management system.
 */
@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OAuth2UserService extends DefaultOAuth2UserService {

    /**
     * Service to manage user.
     */
    private UserService userService;

    @Override
    @SneakyThrows
    public OAuth2User loadUser(final OAuth2UserRequest oAuth2UserRequest) {
        log.trace("Load user {}", oAuth2UserRequest);
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        userService.save(new UserRecord(
            oAuth2User.getAttribute("email"),
            oAuth2User.getAttribute("login"),
            oAuth2User.getAttribute("name"),
            oAuth2User.getAttribute("avatar_url")
        ));

        return oAuth2User;
    }
}
