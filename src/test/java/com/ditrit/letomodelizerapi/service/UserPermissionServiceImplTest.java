package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.model.permission.ActionPermission;
import com.ditrit.letomodelizerapi.model.permission.EntityPermission;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.persistence.model.UserPermission;
import com.ditrit.letomodelizerapi.persistence.repository.UserPermissionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("Test class: UserPermissionServiceImpl")
class UserPermissionServiceImplTest {

    @Mock
    UserPermissionRepository userPermissionRepository;

    @InjectMocks
    UserPermissionServiceImpl service;

    @Test
    @DisplayName("Test save: should return wanted permissions")
    void testGetAllPermissions() {
        UserPermission expectedPermission = new UserPermission();
        expectedPermission.setId("id");
        expectedPermission.setEntity(EntityPermission.ADMIN.name());
        expectedPermission.setAction(ActionPermission.ACCESS.name());

        Mockito
                .when(userPermissionRepository.findAllByUserIdAndEntityIsNot(Mockito.any(), Mockito.any()))
                .thenReturn(List.of(expectedPermission));

        assertEquals(List.of(expectedPermission), service.getAllPermissions(new User()));
    }

    @Test
    @DisplayName("Test checkPermission: should throw an exception")
    void testCheckPermission() {
        User user = new User();
        user.setId(UUID.randomUUID());
        Mockito.when(userPermissionRepository.exists(Mockito.any(Specification.class))).thenReturn(false);
        ApiException exception = null;

        try {
            service.checkPermission(user, "test", EntityPermission.ADMIN, ActionPermission.ACCESS);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getStatus(), exception.getStatus());
        assertEquals("test", exception.getError().getField());
        assertNull(exception.getError().getValue());
    }

    @Test
    @DisplayName("Test checkIsAdmin: should not throw an exception")
    void testCheckIsAdmin() {
        User user = new User();
        user.setId(UUID.randomUUID());
        Mockito.when(userPermissionRepository.exists(Mockito.any(Specification.class))).thenReturn(true);
        ApiException exception = null;

        try {
            service.checkIsAdmin(user, "test");
        } catch (ApiException e) {
            exception = e;
        }

        assertNull(exception);
    }

    @Test
    @DisplayName("Test checkLibraryPermission: should exists and not throw exception")
    void testCheckLibraryPermission() {
        User user = new User();
        user.setId(UUID.randomUUID());
        Mockito
                .when(userPermissionRepository.exists(Mockito.any(Specification.class)))
                .thenReturn(true);

        ApiException exception = null;
        try {
            service.checkLibraryPermission(user, ActionPermission.ACCESS, null);
        } catch (ApiException e) {
            exception = e;
        }

        assertNull(exception);
    }

    @Test
    @DisplayName("Test checkLibraryPermission: should throw exception on permission to create")
    void testCheckLibraryPermissionWithoutCreatePermission() {
        User user = new User();
        user.setId(UUID.randomUUID());
        Mockito
                .when(userPermissionRepository.exists(Mockito.any(Specification.class)))
                .thenReturn(false);

        ApiException exception = null;
        try {
            service.checkLibraryPermission(user, ActionPermission.CREATE, UUID.randomUUID());
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
    }

    @Test
    @DisplayName("Test checkLibraryPermission: should throw exception on other permission")
    void testCheckLibraryPermissionWithoutOtherPermission() {
        User user = new User();
        user.setId(UUID.randomUUID());
        Mockito
                .when(userPermissionRepository.exists(Mockito.any(Specification.class)))
                .thenReturn(false);

        ApiException exception = null;
        try {
            service.checkLibraryPermission(user, ActionPermission.ACCESS, UUID.randomUUID());
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }
}
