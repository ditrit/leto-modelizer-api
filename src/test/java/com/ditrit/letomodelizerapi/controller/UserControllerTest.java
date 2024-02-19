package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.helper.MockHelper;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.AccessControlService;
import com.ditrit.letomodelizerapi.service.UserPermissionService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;

import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("Test class: UserController")
class UserControllerTest extends MockHelper {

    @Mock
    UserService userService;

    @Mock
    UserPermissionService userPermissionService;

    @Mock
    AccessControlService accessControlService;

    @InjectMocks
    UserController controller;

    @Test
    @DisplayName("Test getUsers: should return valid response.")
    void testGetUsers() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(this.userService.findAll(Mockito.any(), Mockito.any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        final Response response = this.controller.getUsers(request, mockUriInfo(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test getUserByLogin: should return valid response.")
    void testGetUserByLogin() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(this.userService.findByLogin(Mockito.any())).thenReturn(new User());
        final Response response = this.controller.getUserByLogin(request, "test");

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test getRolesOfUser: should return valid response.")
    void testGetRolesOfUser() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(this.userService.findByLogin(Mockito.any())).thenReturn(new User());
        Mockito.when(this.accessControlService.findAll(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Page.empty());
        final Response response = this.controller.getRolesOfUser(request, "test", mockUriInfo(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test getGroupsOfUser: should return valid response.")
    void testGetGroupsOfUser() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(this.userService.findByLogin(Mockito.any())).thenReturn(new User());
        Mockito.when(this.accessControlService.findAll(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Page.empty());
        final Response response = this.controller.getGroupsOfUser(request, "test", mockUriInfo(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test getPicture: should return valid response.")
    void testGetPicture() {
        UserController controller = new UserController(userService, userPermissionService, accessControlService, "1");
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

        Response response = controller.getPictureOfUser(request, "test");

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
}
