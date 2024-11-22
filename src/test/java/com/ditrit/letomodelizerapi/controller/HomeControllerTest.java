package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.InputStream;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("Test class: HomeController")
class HomeControllerTest {

    @Mock
    UserService userService;

    HomeController newInstance() {
        return new HomeController(userService, "url1", "url2");
    }

    @Test
    @DisplayName("Test home: should return valid response")
    void testHome() {
        HomeController controller = newInstance();
        ResponseEntity<InputStream> response = controller.home();

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test redirect: should redirect to leto-modelizer.")
    void testRedirect() throws URISyntaxException {
        User user = new User();
        user.setLogin("login");
        Mockito
                .when(userService.getFromSession(Mockito.any()))
                .thenReturn(user);
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        HomeController controller = newInstance();
        ReflectionTestUtils.setField(controller, "letoModelizerUrl", "http://localhost:8080/");
        ResponseEntity<Object> response = controller.redirect(request, null);

        assertNotNull(response);
        assertEquals("http://localhost:8080/", response.getHeaders().getLocation().toString());
    }

    @Test
    @DisplayName("Test redirect: should redirect to leto-modelizer-admin.")
    void testRedirectAdmin() throws URISyntaxException {
        User user = new User();
        user.setLogin("login");
        Mockito
                .when(userService.getFromSession(Mockito.any()))
                .thenReturn(user);
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        HomeController controller = newInstance();
        ReflectionTestUtils.setField(controller, "letoModelizerUrl", "http://localhost:8080/");
        ReflectionTestUtils.setField(controller, "letoAdminUrl", "http://localhost:9000/");
        ResponseEntity<Object> response = controller.redirect(request, "admin");

        assertNotNull(response);
        assertEquals("http://localhost:9000/", response.getHeaders().getLocation().toString());

    }

    @Test
    @DisplayName("Test redirect: should redirect to github oauth endpoint.")
    void testLogin() {
        HomeController controller = newInstance();
        ResponseEntity<Object> response = controller.login();

        assertNotNull(response);
        assertEquals("/api/oauth2/authorization/github", response.getHeaders().getLocation().toString());
    }
}
