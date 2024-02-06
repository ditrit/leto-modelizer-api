package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.helper.MockHelper;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlRecord;
import com.ditrit.letomodelizerapi.persistence.model.AccessControl;
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

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("Test class: GroupController")
class GroupControllerTest extends MockHelper {

    @Mock
    UserService userService;

    @Mock
    UserPermissionService userPermissionService;

    @Mock
    AccessControlService accessControlService;

    @InjectMocks
    GroupController controller;

    @Test
    @DisplayName("Test findAll: should return valid response.")
    void testFindAll() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(this.accessControlService.findAll(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        final Response response = this.controller.findAll(request, mockUriInfo(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test getGroupById: should return valid response.")
    void testGetGroupById() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(this.accessControlService.findById(Mockito.any(), Mockito.any())).thenReturn(new AccessControl());
        final Response response = this.controller.getGroupById(request, 1l);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test createGroup: should return valid response.")
    void testCreateGroup() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(this.accessControlService.create(Mockito.any(), Mockito.any())).thenReturn(new AccessControl());
        final Response response = this.controller.createGroup(request, new AccessControlRecord("test"));

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test updateGroup: should return valid response.")
    void testUpdateGroup() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(this.accessControlService.update(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new AccessControl());
        final Response response = this.controller.updateGroup(request, 1L, new AccessControlRecord("test"));

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test deleteGroup: should return valid response.")
    void testDeleteGroup() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(this.accessControlService).delete(Mockito.any(), Mockito.any());
        final Response response = this.controller.deleteGroup(request, 1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    @DisplayName("Test getUsersByGroup: should return valid response.")
    void testGetUsersByGroup() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(this.accessControlService.findAllUsers(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Page.empty());
        final Response response = this.controller.getUsersByGroup(request, 1l, mockUriInfo(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test associateUser: should return valid response.")
    void testAssociateUser() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(this.accessControlService).associateUser(Mockito.any(), Mockito.any(), Mockito.any());
        final Response response = this.controller.associateUser(request, 1l, "login");

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertNull(response.getEntity());
    }

    @Test
    @DisplayName("Test dissociateUser: should return valid response.")
    void testDissociateUser() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(this.accessControlService).dissociateUser(Mockito.any(), Mockito.any(), Mockito.any());
        final Response response = this.controller.dissociateUser(request, 1L, "login");

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    @DisplayName("Test getSubGroupsOfGroup: should return valid response.")
    void testGetSubGroupsOfGroup() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(this.accessControlService.findAllAccessControls(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Page.empty());
        final Response response = this.controller.getSubGroupsOfGroup(request, 1l, mockUriInfo(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test associate: should return valid response.")
    void testAssociate() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(this.accessControlService).associate(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        final Response response = this.controller.associate(request, 1l, 2l);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertNull(response.getEntity());
    }

    @Test
    @DisplayName("Test dissociate: should return valid response.")
    void testDissociate() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(this.accessControlService).dissociate(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        final Response response = this.controller.dissociate(request, 1L, 2l);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    @DisplayName("Test getRolesOfGroup: should return valid response.")
    void testGetRolesOfGroup() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(this.accessControlService.findAllAccessControls(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Page.empty());
        final Response response = this.controller.getRolesOfGroup(request, 1l, mockUriInfo(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }
}
