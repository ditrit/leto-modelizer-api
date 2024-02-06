package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.helper.MockHelper;
import com.ditrit.letomodelizerapi.model.permission.ActionPermission;
import com.ditrit.letomodelizerapi.model.permission.EntityPermission;
import com.ditrit.letomodelizerapi.model.user.UserDTO;
import com.ditrit.letomodelizerapi.model.user.permission.UserPermissionDTO;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.persistence.model.UserPermission;
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
@DisplayName("Test class: CurrentUserController")
class CurrentUserControllerTest extends MockHelper {

    @Mock
    UserService userService;

    @Mock
    UserPermissionService userPermissionService;

    @Mock
    AccessControlService accessControlService;

    @InjectMocks
    CurrentUserController controller;

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

    @Test
    @DisplayName("Test getMyPermissions: should return valid response.")
    void testGetMyPermissions() {
        User user = new User();
        user.setId(1L);

        UserPermission permission = new UserPermission();
        permission.setId("id");
        permission.setEntity(EntityPermission.ADMIN.name());
        permission.setAction(ActionPermission.ACCESS.name());

        List<UserPermission> permissions = List.of(permission);

        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito
                .when(userService.getFromSession(Mockito.any()))
                .thenReturn(user);
        Mockito
                .when(userPermissionService.getAllPermissions(Mockito.any()))
                .thenReturn(permissions);

        UserPermissionDTO expectedPermission = new UserPermissionDTO();
        expectedPermission.setEntity("ADMIN");
        expectedPermission.setAction("ACCESS");
        List<UserPermissionDTO> expectedPermissions = List.of(expectedPermission);

        Response response = controller.getMyPermissions(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expectedPermissions, response.getEntity());
    }

    @Test
    @DisplayName("Test getMyRoles: should return valid response.")
    void testGetMyRoles() {
        User user = new User();
        user.setId(1L);

        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito
                .when(userService.getFromSession(Mockito.any()))
                .thenReturn(user);
        Mockito
                .when(accessControlService.findAll(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Page.empty());

        Response response = controller.getMyRoles(request, mockUriInfo(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test getMyGroups: should return valid response.")
    void testGetMyGroups() {
        User user = new User();
        user.setId(1L);

        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito
                .when(userService.getFromSession(Mockito.any()))
                .thenReturn(user);
        Mockito
                .when(accessControlService.findAll(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Page.empty());

        Response response = controller.getMyGroups(request, mockUriInfo(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }
}
