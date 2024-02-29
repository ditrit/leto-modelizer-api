package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.persistence.model.AccessControl;
import com.ditrit.letomodelizerapi.persistence.model.Library;
import com.ditrit.letomodelizerapi.persistence.model.Permission;
import com.ditrit.letomodelizerapi.persistence.repository.AccessControlRepository;
import com.ditrit.letomodelizerapi.persistence.repository.PermissionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("Test class: PermissionServiceImpl")
class PermissionServiceImplTest {

    @Mock
    PermissionRepository permissionRepository;

    @Mock
    AccessControlPermissionService accessControlPermissionService;

    @Mock
    AccessControlRepository accessControlRepository;

    @InjectMocks
    PermissionServiceImpl service;

    @Test
    @DisplayName("Test findAll: should return wanted permissions")
    void testFindAll() {
        Mockito
                .when(permissionRepository.findAll(Mockito.any(Specification.class), Mockito.any()))
                .thenReturn(Page.empty());

        assertEquals(Page.empty(), service.findAll(Map.of(), Pageable.ofSize(10)));
    }

    @Test
    @DisplayName("Test findById: should return wanted permission")
    void testFindById() {
        Permission expectedPermission = new Permission();

        expectedPermission.setId(UUID.randomUUID());
        expectedPermission.setEntity("entity");
        expectedPermission.setAction("action");

        Mockito
                .when(permissionRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(expectedPermission));

        assertEquals(expectedPermission, service.findById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Test findById: should throw exception")
    void testFindByIdThrow() {
        Mockito
                .when(permissionRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());
        ApiException exception = null;
        UUID uuid = UUID.randomUUID();

        try {
            service.findById(uuid);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getStatus(), exception.getStatus());
        assertEquals("permissionId", exception.getError().getField());
        assertEquals(uuid.toString(), exception.getError().getValue());
    }

    @Test
    @DisplayName("Test createLibraryPermissions: should create and link permissions to super admin")
    void testCreateLibraryPermissions() {
        Permission permission = new Permission();
        permission.setId(UUID.randomUUID());

        Mockito.when(accessControlRepository.findByName(Mockito.any())).thenReturn(new AccessControl());
        Mockito.when(permissionRepository.save(Mockito.any())).thenReturn(permission);
        Mockito.doNothing()
                .when(accessControlPermissionService).
                associate(Mockito.any(), Mockito.any());

        Library library = new Library();
        library.setId(UUID.randomUUID());
        service.createLibraryPermissions(library, null);

        Mockito
                .verify(accessControlPermissionService, Mockito.times(3))
                .associate(Mockito.any(), Mockito.any());
        Mockito.reset(accessControlPermissionService);
    }

    @Test
    @DisplayName("Test createLibraryPermissions: should create and link permissions to super admin and given role")
    void testCreateLibraryPermissionsWithRole() {
        Permission permission = new Permission();
        permission.setId(UUID.randomUUID());

        Mockito.when(permissionRepository.save(Mockito.any())).thenReturn(permission);
        Mockito.doNothing()
                .when(accessControlPermissionService).
                associate(Mockito.any(), Mockito.any());
        Mockito.when(accessControlRepository.findByName(Mockito.any())).thenReturn(new AccessControl());

        Library library = new Library();
        library.setId(UUID.randomUUID());

        AccessControl role = new AccessControl();
        role.setId(UUID.randomUUID());
        service.createLibraryPermissions(library, role);

        Mockito
                .verify(accessControlPermissionService, Mockito.times(6))
                .associate(Mockito.any(), Mockito.any());
        Mockito.reset(accessControlPermissionService);
    }
}
