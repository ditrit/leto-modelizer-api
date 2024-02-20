package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.helper.MockHelper;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlRecord;
import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.persistence.model.AccessControl;
import com.ditrit.letomodelizerapi.persistence.model.Permission;
import com.ditrit.letomodelizerapi.service.AccessControlPermissionService;
import com.ditrit.letomodelizerapi.service.AccessControlService;
import com.ditrit.letomodelizerapi.service.PermissionService;
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
@DisplayName("Test class: RoleController")
class RoleControllerTest extends MockHelper {

    @Mock
    UserService userService;

    @Mock
    UserPermissionService userPermissionService;

    @Mock
    PermissionService permissionService;

    @Mock
    AccessControlService accessControlService;

    @Mock
    AccessControlPermissionService accessControlPermissionService;

    @InjectMocks
    RoleController controller;

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
    @DisplayName("Test getRoleById: should return valid response.")
    void testGetRoleById() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(this.accessControlService.findById(Mockito.any(), Mockito.any())).thenReturn(new AccessControl());
        final Response response = this.controller.getRoleById(request, 1l);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test createRole: should return valid response.")
    void testCreateRole() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(this.accessControlService.create(Mockito.any(), Mockito.any())).thenReturn(new AccessControl());
        final Response response = this.controller.createRole(request, new AccessControlRecord("test"));

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test updateRole: should return valid response.")
    void testUpdateRole() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(this.accessControlService.update(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new AccessControl());
        final Response response = this.controller.updateRole(request, 2L, new AccessControlRecord("test"));

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test deleteRole: should return valid response.")
    void testDeleteRole() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(this.accessControlService).delete(Mockito.any(), Mockito.any());
        final Response response = this.controller.deleteRole(request, 2L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    @DisplayName("Test getUsersByRole: should return valid response.")
    void testGetUsersByRole() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(this.accessControlService.findAllUsers(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Page.empty());
        final Response response = this.controller.getUsersByRole(request, 2l, mockUriInfo(), new QueryFilter());

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
        final Response response = this.controller.associateUser(request, 2l, "login");

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
        final Response response = this.controller.dissociateUser(request, 2L, "login");

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    @DisplayName("Test getSubRolesOfRole: should return valid response.")
    void testGetSubRolesOfRole() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(this.accessControlService.findAllAccessControls(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Page.empty());
        final Response response = this.controller.getSubRolesOfRole(request, 2l, mockUriInfo(), new QueryFilter());

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
        final Response response = this.controller.associate(request, 2l, 3l);

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
        final Response response = this.controller.dissociate(request, 2l, 3l);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    @DisplayName("Test getGroupsOfRole: should return valid response.")
    void testGetGroupsOfRole() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(this.accessControlService.findAllChildren(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Page.empty());
        final Response response = this.controller.getGroupsOfRole(request, 2l, mockUriInfo(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test associateGroup: should return valid response.")
    void testAssociateGroup() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(this.accessControlService).associate(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        final Response response = this.controller.associateGroup(request, 2l, 2l);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertNull(response.getEntity());
    }

    @Test
    @DisplayName("Test dissociateGroup: should return valid response.")
    void testDissociateGroup() {
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(this.accessControlService).dissociate(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
        final Response response = this.controller.dissociateGroup(request, 2L, 3l);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    @DisplayName("Test getPermissionsOfRole: should return valid response.")
    void testGetPermissionsOfRole() {
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
        final Response response = this.controller.getPermissionsOfRole(request, 2l, mockUriInfo(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test associatePermission: should return valid response.")
    void testAssociatePermission() {
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
                .when(this.permissionService.findById(Mockito.any()))
                .thenReturn(new Permission());
        Mockito.doNothing().when(this.accessControlPermissionService).associate(Mockito.any(), Mockito.any());
        final Response response = this.controller.associatePermission(request, 2l, 3l);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertNull(response.getEntity());
    }

    @Test
    @DisplayName("Test dissociatePermission: should return valid response.")
    void testDissociatePermission() {
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
                .when(this.permissionService.findById(Mockito.any()))
                .thenReturn(new Permission());
        Mockito.doNothing().when(this.accessControlPermissionService).dissociate(Mockito.any(), Mockito.any());
        final Response response = this.controller.dissociatePermission(request, 2L, 3l);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    @DisplayName("Test checkSuperAdmin: should do nothing on valid id")
    void testCheckSuperAdmin() {
        ApiException exception = null;

        try {
            controller.checkSuperAdmin(2L);
        } catch (ApiException e) {
            exception = e;
        }

        assertNull(exception);
    }

    @Test
    @DisplayName("Test checkSuperAdmin: should throw an error on invalid id")
    void testCheckSuperAdminThrowError() {
        ApiException exception = null;

        try {
            controller.checkSuperAdmin(1L);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.WRONG_VALUE.getStatus(), exception.getStatus());
        assertEquals(ErrorType.WRONG_VALUE.getMessage(), exception.getMessage());
        assertEquals("id", exception.getError().getField());
        assertEquals("1", exception.getError().getValue());
    }
}
