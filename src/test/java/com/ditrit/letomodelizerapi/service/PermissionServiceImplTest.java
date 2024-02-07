package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.persistence.model.Permission;
import com.ditrit.letomodelizerapi.persistence.repository.PermissionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("Test class: PermissionServiceImpl")
class PermissionServiceImplTest {

    @Mock
    PermissionRepository permissionRepository;

    @InjectMocks
    PermissionServiceImpl service;

    @Test
    @DisplayName("Test findById: should return wanted permission")
    void testFindById() {
        Permission expectedPermission = new Permission();

        expectedPermission.setId(1L);
        expectedPermission.setEntity("entity");
        expectedPermission.setAction("action");

        Mockito
                .when(permissionRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(expectedPermission));

        assertEquals(expectedPermission, service.findById(1l));
    }

    @Test
    @DisplayName("Test findById: should throw exception")
    void testFindByIdThrow() {
        Mockito
                .when(permissionRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());
        ApiException exception = null;

        try {
            service.findById(1l);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getStatus(), exception.getStatus());
        assertEquals("permissionId", exception.getError().getField());
        assertEquals("1", exception.getError().getValue());
    }
}
