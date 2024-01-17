package com.ditrit.letomodelizerapi.controller.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.IOException;

@Tag("unit")
@DisplayName("Test class: AuthenticationSuccessHandler")
class AuthenticationSuccessHandlerTest {

    @Test
    @DisplayName("test onAuthenticationSuccess: should init session")
    void testOnAuthenticationSuccess() throws ServletException, IOException {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        OAuth2AuthenticationToken authentication = Mockito.mock(OAuth2AuthenticationToken.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        OAuth2User user = Mockito.mock(OAuth2User.class);

        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(user.getAttribute(Mockito.any())).thenReturn("login_value");
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        Mockito.doNothing().when(session).setAttribute(Mockito.any(), Mockito.any());

        new AuthenticationSuccessHandler().onAuthenticationSuccess(request, response, authentication);

        Mockito
            .verify(session, Mockito.times(1))
            .setAttribute("login", "login_value");
    }
}
