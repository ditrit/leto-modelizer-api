package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.helper.MockHelper;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDTO;
import com.ditrit.letomodelizerapi.model.ai.AIConversationDTO;
import com.ditrit.letomodelizerapi.model.permission.ActionPermission;
import com.ditrit.letomodelizerapi.model.permission.EntityPermission;
import com.ditrit.letomodelizerapi.model.permission.PermissionDTO;
import com.ditrit.letomodelizerapi.model.user.UserDTO;
import com.ditrit.letomodelizerapi.persistence.model.Permission;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.AIService;
import com.ditrit.letomodelizerapi.service.AccessControlService;
import com.ditrit.letomodelizerapi.service.UserPermissionService;
import com.ditrit.letomodelizerapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;

import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @Mock
    AIService aiService;

    @InjectMocks
    CurrentUserController currentUserController;

    @Test
    @DisplayName("Test getMyInformation: should return valid response.")
    void testGetMyInformation() {
        User user = new User();
        user.setId(UUID.randomUUID());
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

        ResponseEntity<UserDTO> response = currentUserController.getMyInformation(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUser, response.getBody());
    }

    @Test
    @DisplayName("Test getPicture: should return valid response.")
    void testGetPicture() {
        CurrentUserController controller = new CurrentUserController(
            userService,
            userPermissionService,
            accessControlService,
            aiService,
            "1"
        );
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("email");
        user.setLogin("login");
        user.setName("name");
        user.setPicture("picture");

        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpResponse<byte[]> picture = Mockito.mock(HttpResponse.class);

        Mockito
                .when(userService.getFromSession(Mockito.any()))
                .thenReturn(user);
        Map<String, List<String>> headers = new HashMap<>();
        Mockito.when(picture.headers()).thenReturn(HttpHeaders.of(headers, (s, s2) -> true));

        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito
                .when(userService.getPicture(Mockito.any()))
                .thenReturn(picture);

        ResponseEntity<byte[]> response = controller.getMyPicture(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test getMyPermissions: should return valid response.")
    void testGetMyPermissions() {
        User user = new User();
        user.setId(UUID.randomUUID());

        Permission permission = new Permission();
        permission.setId(UUID.randomUUID());
        permission.setLibraryId(UUID.randomUUID());
        permission.setEntity(EntityPermission.ADMIN);
        permission.setAction(ActionPermission.ACCESS);

        List<Permission> permissions = List.of(permission);

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

        PermissionDTO expectedPermission = new PermissionDTO();
        expectedPermission.setId(permission.getId());
        expectedPermission.setEntity("ADMIN");
        expectedPermission.setAction("ACCESS");
        expectedPermission.setLibraryId(permission.getLibraryId());
        List<PermissionDTO> expectedPermissions = List.of(expectedPermission);

        ResponseEntity<List<PermissionDTO>> response = currentUserController.getMyPermissions(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedPermissions, response.getBody());
    }

    @Test
    @DisplayName("Test getMyRoles: should return valid response.")
    void testGetMyRoles() {
        User user = new User();
        user.setId(UUID.randomUUID());

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

        ResponseEntity<Page<AccessControlDTO>> response = currentUserController.getMyRoles(request, new LinkedMultiValueMap<>(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test getMyGroups: should return valid response.")
    void testGetMyGroups() {
        User user = new User();
        user.setId(UUID.randomUUID());

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

        ResponseEntity<Page<AccessControlDTO>> response = currentUserController.getMyGroups(request, new LinkedMultiValueMap<>(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test getMyScopes: should return valid response.")
    void testGetMyScopes() {
        User user = new User();
        user.setId(UUID.randomUUID());

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

        ResponseEntity<Page<AccessControlDTO>> response = currentUserController.getMyScopes(request, new LinkedMultiValueMap<>(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test getMyAIConversations: should return valid response.")
    void testMyAIConversations() {
        User user = new User();
        user.setId(UUID.randomUUID());

        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito
                .when(userService.getFromSession(Mockito.any()))
                .thenReturn(user);
        Mockito
                .when(aiService.findAll(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Page.empty());

        ResponseEntity<Page<AIConversationDTO>> response = currentUserController.getMyAIConversations(request, new LinkedMultiValueMap<>(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
