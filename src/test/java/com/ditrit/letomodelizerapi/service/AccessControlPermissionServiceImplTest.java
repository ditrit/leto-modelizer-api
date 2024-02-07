package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.persistence.model.AccessControlPermission;
import com.ditrit.letomodelizerapi.persistence.repository.AccessControlPermissionRepository;
import com.ditrit.letomodelizerapi.persistence.repository.AccessControlPermissionViewRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("Test class: AccessControlServiceImpl")
class AccessControlPermissionServiceImplTest {

    @Mock
    AccessControlPermissionViewRepository accessControlPermissionViewRepository;

    @Mock
    AccessControlPermissionRepository accessControlPermissionRepository;

    @InjectMocks
    AccessControlPermissionServiceImpl service;

    @Test
    @DisplayName("Test findAll: should return wanted permissions of access control")
    void testFindAll() {
        Mockito
                .when(accessControlPermissionViewRepository.findAll(Mockito.any(Specification.class), Mockito.any()))
                .thenReturn(Page.empty());

        assertEquals(Page.empty(), service.findAll(1l, Map.of(), Pageable.ofSize(10)));
    }

    @Test
    @DisplayName("Test associate: should associate access control to permission")
    void testAssociate() {
        Mockito
                .when(accessControlPermissionRepository.findByAccessControlIdAndPermissionId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito
                .when(accessControlPermissionRepository.save(Mockito.any()))
                .thenReturn(new AccessControlPermission());

        service.associate(1l, 2l);

        Mockito.verify(accessControlPermissionRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Test associate: should throw exception on already existing association")
    void testAssociateThrow() {
        AccessControlPermission accessControlPermission = new AccessControlPermission();
        accessControlPermission.setId(1L);
        accessControlPermission.setPermissionId(1L);
        accessControlPermission.setAccessControlId(1l);

        Mockito
                .when(accessControlPermissionRepository.findByAccessControlIdAndPermissionId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(accessControlPermission));
        ApiException exception = null;

        try {
            service.associate(1l, 2l);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.ENTITY_ALREADY_EXISTS.getStatus(), exception.getStatus());
        assertEquals("association", exception.getError().getField());
    }

    @Test
    @DisplayName("Test dissociate: should dissociate access control to permission")
    void testDissociate() {
        AccessControlPermission accessControlPermission = new AccessControlPermission();
        accessControlPermission.setId(1L);
        accessControlPermission.setPermissionId(1L);
        accessControlPermission.setAccessControlId(1l);

        Mockito
                .when(accessControlPermissionRepository.findByAccessControlIdAndPermissionId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(accessControlPermission));
        Mockito
                .doNothing()
                .when(accessControlPermissionRepository)
                .delete(Mockito.any());

        service.dissociate(1l, 2l);

        Mockito.verify(accessControlPermissionRepository, Mockito.times(1)).delete(Mockito.any());
    }

    @Test
    @DisplayName("Test dissociate: should throw exception on unknown association")
    void testDissociateThrow() {
        Mockito
                .when(accessControlPermissionRepository.findByAccessControlIdAndPermissionId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.empty());
        ApiException exception = null;

        try {
            service.dissociate(1l, 2l);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getStatus(), exception.getStatus());
        assertEquals("association", exception.getError().getField());
    }
}
