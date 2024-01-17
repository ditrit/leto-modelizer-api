package com.ditrit.letomodelizerapi.controller;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@DisplayName("Test class: HomeController")
class HomeControllerTest {

    @Test
    @DisplayName("Test home: should return valid response")
    void testHome() throws FileNotFoundException {
        HomeController controller = new HomeController();
        Response response = controller.home();

        assertNotNull(response);
        assertNotNull(response.getEntity());
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    @DisplayName("Test redirect: should redirect to leto-modelizer.")
    void testRedirect() throws IOException, URISyntaxException {
        HomeController controller = new HomeController();
        ReflectionTestUtils.setField(controller, "letoModelizerUrl", "http://localhost:8080/");
        Response response = controller.redirect(null);

        assertNotNull(response);
        assertEquals("http://localhost:8080/", response.getLocation().toString());
    }

    @Test
    @DisplayName("Test redirect: should redirect to leto-modelizer-admin.")
    void testRedirectAdmin() throws IOException, URISyntaxException {
        HomeController controller = new HomeController();
        ReflectionTestUtils.setField(controller, "letoModelizerUrl", "http://localhost:8080/");
        ReflectionTestUtils.setField(controller, "letoAdminUrl", "http://localhost:9000/");
        Response response = controller.redirect("admin");

        assertNotNull(response);
        assertEquals("http://localhost:9000/", response.getLocation().toString());

    }

    @Test
    @DisplayName("Test redirect: should redirect to github oauth endpoint.")
    void testLogin() {
        HomeController controller = new HomeController();
        Response response = controller.login();

        assertNotNull(response);
        assertEquals("/api/oauth2/authorization/github", response.getLocation().toString());
    }
}
