package com.ditrit.letomodelizerapi.persistence.repository;

import com.ditrit.letomodelizerapi.persistence.model.UserCsrfToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.DefaultCsrfToken;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("Test class: UserCsrfTokenServiceImpl")
class DatabaseCsrfTokenRepositoryTest {

    @Mock
    UserCsrfTokenRepository userCsrfTokenRepository;

    @Test
    @DisplayName("Test generateToken: should generate token with valid size")
    void testGenerateToken() {
        DatabaseCsrfTokenRepository service = new DatabaseCsrfTokenRepository(userCsrfTokenRepository, 60);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);

        Mockito.when(session.getAttribute(Mockito.any())).thenReturn("login");
        Mockito.when(request.getSession()).thenReturn(session);

        CsrfToken token = service.generateToken(request);

        assertNotNull(token);
        assertNotNull(token.getToken());
        assertEquals(255, token.getToken().length());
        assertEquals("X-CSRF-TOKEN", token.getHeaderName());
        assertEquals("_csrf", token.getParameterName());
    }

    @Test
    @DisplayName("Test saveToken: should delete user token without token")
    void testSaveTokenDelete() {
        DatabaseCsrfTokenRepository service = new DatabaseCsrfTokenRepository(userCsrfTokenRepository, 60);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);

        Mockito.when(session.getAttribute(Mockito.any())).thenReturn("login");
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.doNothing().when(userCsrfTokenRepository).deleteByLogin("login");

        service.saveToken(null, request, null);
        Mockito.verify(userCsrfTokenRepository, Mockito.times(1)).deleteByLogin("login");
    }

    @Test
    @DisplayName("Test saveToken: should save new token without existing token")
    void testSaveTokenNew() {
        DatabaseCsrfTokenRepository service = new DatabaseCsrfTokenRepository(userCsrfTokenRepository, 60);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);

        Mockito.when(session.getAttribute(Mockito.any())).thenReturn("login");
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(userCsrfTokenRepository.findByLogin("login")).thenReturn(Optional.empty());
        Mockito.when(userCsrfTokenRepository.save(Mockito.any())).thenReturn(new UserCsrfToken(60));

        service.saveToken(new DefaultCsrfToken("header", "param", "token"), request, null);
        Mockito.verify(userCsrfTokenRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Test saveToken: should save token with existing token")
    void testSaveToken() {
        DatabaseCsrfTokenRepository service = new DatabaseCsrfTokenRepository(userCsrfTokenRepository, 60);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);

        Mockito.when(session.getAttribute(Mockito.any())).thenReturn("login");
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(userCsrfTokenRepository.findByLogin("login")).thenReturn(Optional.of(new UserCsrfToken(60)));
        Mockito.when(userCsrfTokenRepository.save(Mockito.any())).thenReturn(new UserCsrfToken(60));

        service.saveToken(new DefaultCsrfToken("header", "param", "token"), request, null);

        Mockito.verify(userCsrfTokenRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Test isExpired: should return true on expired token")
    void testIsExpired() {
        DatabaseCsrfTokenRepository service = new DatabaseCsrfTokenRepository(userCsrfTokenRepository, 60);

        UserCsrfToken token = new UserCsrfToken();
        token.setExpirationDate(Timestamp.from(Instant.MAX));
        assertTrue(service.isExpired(token));
    }

    @Test
    @DisplayName("Test isExpired: should return false on valid token")
    void testIsExpiredNot() {
        DatabaseCsrfTokenRepository service = new DatabaseCsrfTokenRepository(userCsrfTokenRepository, 60);

        UserCsrfToken token = new UserCsrfToken(60);
        token.setExpirationDate(Timestamp.from(Instant.MIN));
        assertFalse(service.isExpired(token));
    }

    @Test
    @DisplayName("Test loadToken: should return null without login in session")
    void testLoadTokenEmptyLogin() {
        DatabaseCsrfTokenRepository service = new DatabaseCsrfTokenRepository(userCsrfTokenRepository, 60);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);

        Mockito.when(session.getAttribute(Mockito.any())).thenReturn(null);
        Mockito.when(request.getSession()).thenReturn(session);
        assertNull(service.loadToken(request));
    }

    @Test
    @DisplayName("Test loadToken: should return null without user token")
    void testLoadTokenEmptyUserToken() {
        DatabaseCsrfTokenRepository service = new DatabaseCsrfTokenRepository(userCsrfTokenRepository, 60);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);

        Mockito.when(session.getAttribute(Mockito.any())).thenReturn("login");
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(userCsrfTokenRepository.findByLogin("login")).thenReturn(Optional.empty());
        assertNull(service.loadToken(request));
    }

    @Test
    @DisplayName("Test loadToken: should return null with expired token")
    void testLoadTokenExpiredToken() {
        DatabaseCsrfTokenRepository service = new DatabaseCsrfTokenRepository(userCsrfTokenRepository, 60);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        UserCsrfToken token = new UserCsrfToken(60);
        token.setExpirationDate(Timestamp.from(Instant.MAX));

        Mockito.when(session.getAttribute(Mockito.any())).thenReturn("login");
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(userCsrfTokenRepository.findByLogin("login")).thenReturn(Optional.of(token));
        Mockito.doNothing().when(userCsrfTokenRepository).delete(Mockito.any());
        assertNull(service.loadToken(request));
        Mockito.verify(userCsrfTokenRepository, Mockito.times(1)).delete(Mockito.any());
    }

    @Test
    @DisplayName("Test loadToken: should return valid token")
    void testLoadToken() {
        DatabaseCsrfTokenRepository service = new DatabaseCsrfTokenRepository(userCsrfTokenRepository, 60);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpSession session = Mockito.mock(HttpSession.class);
        UserCsrfToken token = new UserCsrfToken(60);
        token.setUpdateDate(Timestamp.from(Instant.MIN));
        token.setToken("token");

        Mockito.when(session.getAttribute(Mockito.any())).thenReturn("login");
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(userCsrfTokenRepository.findByLogin("login")).thenReturn(Optional.of(token));

        CsrfToken csrfToken = service.loadToken(request);
        assertNotNull(csrfToken);
        assertEquals("token", csrfToken.getToken());
        assertEquals("X-CSRF-TOKEN", csrfToken.getHeaderName());
        assertEquals("_csrf", csrfToken.getParameterName());
    }
}
