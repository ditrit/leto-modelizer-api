package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.helper.MockHelper;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDTO;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDirectDTO;
import com.ditrit.letomodelizerapi.model.ai.AIConversationDTO;
import com.ditrit.letomodelizerapi.model.permission.PermissionDTO;
import com.ditrit.letomodelizerapi.model.user.UserDTO;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;

import java.net.http.HttpHeaders;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @Mock
    AIService aiService;

    @InjectMocks
    UserController controller;

    @Test
    @DisplayName("Test getUsers: should return valid response.")
    void testGetUsers() {
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
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(this.userService.findAll(Mockito.any(), Mockito.any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        final ResponseEntity<Page<UserDTO>> response = this.controller.getUsers(request, new LinkedMultiValueMap<>(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test getUserByLogin: should return valid response.")
    void testGetUserByLogin() {
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
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(this.userService.findByLogin(Mockito.any())).thenReturn(new User());
        final ResponseEntity<UserDTO> response = this.controller.getUserByLogin(request, "test");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test deleteUserByLogin: should return valid response.")
    void testDeleteUserByLogin() {
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
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(userService).deleteByLogin("test");
        final ResponseEntity<Object> response = this.controller.deleteUserByLogin(request, "test");

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Test getRolesOfUser: should return valid response.")
    void testGetRolesOfUser() {
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
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(this.userService.findByLogin(Mockito.any())).thenReturn(new User());
        Mockito.when(this.accessControlService.findAll(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Page.empty());
        final ResponseEntity<Page<AccessControlDirectDTO>> response = this.controller.getRolesOfUser(request, "test", new LinkedMultiValueMap<>(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test getGroupsOfUser: should return valid response.")
    void testGetGroupsOfUser() {
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
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(this.userService.findByLogin(Mockito.any())).thenReturn(new User());
        Mockito.when(this.accessControlService.findAll(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Page.empty());
        final ResponseEntity<Page<AccessControlDTO>> response = this.controller.getGroupsOfUser(request, "test", new LinkedMultiValueMap<>(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test getPermissionsOfUser: should return valid response for our user.")
    void testGetPermissionsOfUser() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setLogin("login");
        user.setId(id);

        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(user);
        Mockito.when(userService.findByLogin(Mockito.any())).thenReturn(user);

        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(this.userService.findByLogin(Mockito.any())).thenReturn(new User());
        Mockito.when(this.userPermissionService.findAll(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Page.empty());
        final ResponseEntity<Page<PermissionDTO>> response = this.controller.getPermissionsOfUser(request, "login", new LinkedMultiValueMap<>(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test getPermissionsOfUser: should return valid response for another user.")
    void testGetPermissionsOfUserAnother() {
        User me = new User();
        me.setLogin("me");
        me.setId(UUID.randomUUID());

        User user = new User();
        user.setLogin("login");
        user.setId(UUID.randomUUID());

        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(me);
        Mockito.when(userService.findByLogin(Mockito.any())).thenReturn(user);

        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(this.userService.findByLogin(Mockito.any())).thenReturn(new User());
        Mockito.when(this.userPermissionService.findAll(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Page.empty());
        final ResponseEntity<Page<PermissionDTO>> response = this.controller.getPermissionsOfUser(request, "login", new LinkedMultiValueMap<>(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test getScopesOfUser: should return valid response.")
    void testGetScopesOfUser() {
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
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(this.userService.findByLogin(Mockito.any())).thenReturn(new User());
        Mockito.when(this.accessControlService.findAll(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Page.empty());
        final ResponseEntity<Page<AccessControlDTO>> response = this.controller.getScopesOfUser(request, "test", new LinkedMultiValueMap<>(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test getPicture: should return valid response.")
    void testGetPicture() {
        UserController userController = new UserController(userService, userPermissionService, accessControlService, aiService, "1");
        User user = new User();
        user.setId(UUID.randomUUID());
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
                .when(userService.getFromSession(Mockito.any()))
                .thenReturn(user);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito
                .when(userService.getPicture(Mockito.any()))
                .thenReturn(picture);

        ResponseEntity<byte[]> response = userController.getPictureOfUser(request, "test");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Test getAIConversationsOfUser: should return valid response for our user.")
    void testGetAIConversationsOfUser() {
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setLogin("login");
        user.setId(id);

        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(user);
        Mockito.when(userService.findByLogin(Mockito.any())).thenReturn(user);

        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(this.userService.findByLogin(Mockito.any())).thenReturn(new User());
        Mockito.when(this.aiService.findAll(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Page.empty());
        final ResponseEntity<Page<AIConversationDTO>> response = this.controller.getAIConversationsOfUser(request, "login", new LinkedMultiValueMap<>(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test getAIConversationsOfUser: should return valid response for another user.")
    void testGetAIConversationsOfUserAnother() {
        User me = new User();
        me.setLogin("me");
        me.setId(UUID.randomUUID());

        User user = new User();
        user.setLogin("login");
        user.setId(UUID.randomUUID());

        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(me);
        Mockito.when(userService.findByLogin(Mockito.any())).thenReturn(user);

        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(this.userService.findByLogin(Mockito.any())).thenReturn(new User());
        Mockito.when(this.aiService.findAll(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Page.empty());
        final ResponseEntity<Page<AIConversationDTO>> response = this.controller.getAIConversationsOfUser(request, "login", new LinkedMultiValueMap<>(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
