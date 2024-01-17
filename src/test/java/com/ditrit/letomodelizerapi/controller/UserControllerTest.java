package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.model.user.UserDTO;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("Test class: UserController")
class UserControllerTest {

    @Mock
    UserService userService;

    @InjectMocks
    UserController controller;

    @Test
    @DisplayName("Test getMyInformation: should return valid response.")
    void testGetMyInformation() {
        User user = new User();
        user.setId(1L);
        user.setEmail("email");
        user.setLogin("login");
        user.setName("name");
        user.setPicture("picture");

        UserDTO expectedUser = new UserDTO();
        expectedUser.setEmail("email");
        expectedUser.setLogin("login");
        expectedUser.setName("name");

        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito
                .when(userService.getFromSession(Mockito.any()))
                .thenReturn(user);

        Response response = controller.getMyInformation(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expectedUser, response.getEntity());
    }

    @Test
    @DisplayName("Test getPicture: should return valid response.")
    void testGetPicture() {
        User user = new User();
        user.setId(1L);
        user.setEmail("email");
        user.setLogin("login");
        user.setName("name");
        user.setPicture("picture");

        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpResponse<byte[]> picture = Mockito.mock(HttpResponse.class);
        Map<String, List<String>> headers = new HashMap<>();
        Mockito.when(picture.headers()).thenReturn(HttpHeaders.of(headers, (s, s2) -> true));

        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito
                .when(userService.getPicture(Mockito.any()))
                .thenReturn(picture);

        Response response = controller.getMyPicture(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
}