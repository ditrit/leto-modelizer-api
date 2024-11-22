package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.helper.MockHelper;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDTO;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDirectDTO;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlRecord;
import com.ditrit.letomodelizerapi.model.permission.PermissionDirectDTO;
import com.ditrit.letomodelizerapi.model.user.UserDTO;
import com.ditrit.letomodelizerapi.persistence.model.AccessControl;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.AccessControlPermissionService;
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

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("Test class: ScopeController")
class ScopeControllerTest extends MockHelper {

    @Mock
    UserService userService;

    @Mock
    UserPermissionService userPermissionService;

    @Mock
    AccessControlService accessControlService;

    @Mock
    AccessControlPermissionService accessControlPermissionService;

    @InjectMocks
    ScopeController controller;

    @Test
    @DisplayName("Test findAll: should return valid response.")
    void testFindAll() {
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
        Mockito.when(this.accessControlService.findAll(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        final ResponseEntity<Page<AccessControlDTO>> response = this.controller.findAll(request, new LinkedMultiValueMap<>(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test getGroupById: should return valid response.")
    void testGetGroupById() {
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
        Mockito.when(this.accessControlService.findById(Mockito.any(), Mockito.any())).thenReturn(new AccessControl());
        final ResponseEntity<AccessControlDTO> response = this.controller.getScopeById(request, UUID.randomUUID());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test createGroup: should return valid response.")
    void testCreateGroup() {
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
        Mockito.when(this.accessControlService.create(Mockito.any(), Mockito.any())).thenReturn(new AccessControl());
        final ResponseEntity<AccessControlDTO> response = this.controller.createScope(request, new AccessControlRecord("test"));

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test updateScope: should return valid response.")
    void testUpdateScope() {
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
        Mockito.when(this.accessControlService.update(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new AccessControl());
        final ResponseEntity<AccessControlDTO> response = this.controller.updateScope(request, UUID.randomUUID(), new AccessControlRecord("test"));

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test deleteScope: should return valid response.")
    void testDeleteScope() {
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
        Mockito.doNothing().when(this.accessControlService).delete(Mockito.any(), Mockito.any());
        final ResponseEntity<Object> response = this.controller.deleteScope(request, UUID.randomUUID());

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Test getUsersByScope: should return valid response.")
    void testGetUsersByScope() {
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
        Mockito.when(this.accessControlService.findAllUsers(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Page.empty());
        final ResponseEntity<Page<UserDTO>> response = this.controller.getUsersByScope(request, UUID.randomUUID(), new LinkedMultiValueMap<>(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test associateUser: should return valid response.")
    void testAssociateUser() {
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
        Mockito.doNothing().when(this.accessControlService).associateUser(Mockito.any(), Mockito.any(), Mockito.any());
        final ResponseEntity<Object> response = this.controller.associateUser(request, UUID.randomUUID(), "login");

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("Test dissociateUser: should return valid response.")
    void testDissociateUser() {
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
        Mockito.doNothing().when(this.accessControlService).dissociateUser(Mockito.any(), Mockito.any(), Mockito.any());
        final ResponseEntity<Object> response = this.controller.dissociateUser(request, UUID.randomUUID(), "login");

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Test getGroupsOfScope: should return valid response.")
    void testGetGroupsOfScope() {
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
        Mockito.when(this.accessControlService.findAllAccessControls(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Page.empty());
        final ResponseEntity<Page<AccessControlDirectDTO>> response = this.controller.getGroupsOfScope(request, UUID.randomUUID(), new LinkedMultiValueMap<>(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test associate: should return valid response.")
    void testAssociate() {
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
        Mockito.doNothing().when(this.accessControlService).associate(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        final ResponseEntity<Object> response = this.controller.associate(request, UUID.randomUUID(), UUID.randomUUID().toString());

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("Test dissociate: should return valid response.")
    void testDissociate() {
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
        Mockito.doNothing().when(this.accessControlService).dissociate(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        final ResponseEntity<Object> response = this.controller.dissociate(request, UUID.randomUUID(), UUID.randomUUID());

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Test getRolesOfScope: should return valid response.")
    void testGetRolesOfScope() {
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
        Mockito.when(this.accessControlService.findAllAccessControls(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Page.empty());
        final ResponseEntity<Page<AccessControlDirectDTO>> response = this.controller.getRolesOfScope(request, UUID.randomUUID(), new LinkedMultiValueMap<>(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Test getPermissionsOfScope: should return valid response.")
    void testGetPermissionsOfScope() {
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
        Mockito
                .when(this.accessControlService.findById(Mockito.any(), Mockito.any()))
                .thenReturn(new AccessControl());
        Mockito
                .when(this.accessControlPermissionService.findAll(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Page.empty());
        final ResponseEntity<Page<PermissionDirectDTO>> response = this.controller.getPermissionsOfScope(request, UUID.randomUUID(), new LinkedMultiValueMap<>(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
