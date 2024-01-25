package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlRecord;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.persistence.model.AccessControl;
import com.ditrit.letomodelizerapi.persistence.repository.AccessControlRepository;
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
class AccessControlServiceImplTest {

    @Mock
    AccessControlRepository accessControlRepository;

    @InjectMocks
    AccessControlServiceImpl service;

    @Test
    @DisplayName("Test findAll: should return wanted permissions")
    void testFindAll() {
        Mockito
                .when(accessControlRepository.findAll(Mockito.any(Specification.class), Mockito.any()))
                .thenReturn(Page.empty());

        assertEquals(Page.empty(), service.findAll(AccessControlType.ROLE, Map.of(), Pageable.ofSize(10)));
    }

    @Test
    @DisplayName("Test findById: should return wanted permission")
    void testFindById() {
        AccessControl expectedAccessControl = new AccessControl();
        expectedAccessControl.setId(1L);
        expectedAccessControl.setType(AccessControlType.ROLE);
        expectedAccessControl.setName("name");

        Mockito
                .when(accessControlRepository.findOne(Mockito.any(Specification.class)))
                .thenReturn(Optional.of(expectedAccessControl));

        assertEquals(expectedAccessControl, service.findById(AccessControlType.ROLE, 1l));
    }

    @Test
    @DisplayName("Test findById: should throw exception")
    void testFindByIdThrow() {
        Mockito
                .when(accessControlRepository.findOne(Mockito.any(Specification.class)))
                .thenReturn(Optional.empty());
        ApiException exception = null;

        try {
            service.findById(AccessControlType.ROLE, 1l);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getStatus(), exception.getStatus());
        assertEquals("id", exception.getError().getField());
        assertEquals("1", exception.getError().getValue());
    }

    @Test
    @DisplayName("Test create: should create access control")
    void testCreate() {
        AccessControl expectedAccessControl = new AccessControl();
        expectedAccessControl.setId(1L);
        expectedAccessControl.setType(AccessControlType.ROLE);
        expectedAccessControl.setName("name");

        Mockito
                .when(accessControlRepository.save(Mockito.any()))
                .thenReturn(expectedAccessControl);

        assertEquals(expectedAccessControl, service.create(AccessControlType.ROLE,  new AccessControlRecord("name")));
    }

    @Test
    @DisplayName("Test update: should update access control")
    void testUpdate() {
        AccessControl expectedAccessControl = new AccessControl();
        expectedAccessControl.setId(1L);
        expectedAccessControl.setType(AccessControlType.ROLE);
        expectedAccessControl.setName("name");

        Mockito
                .when(accessControlRepository.findOne(Mockito.any(Specification.class)))
                .thenReturn(Optional.of(expectedAccessControl));
        Mockito
                .when(accessControlRepository.save(Mockito.any()))
                .thenReturn(expectedAccessControl);

        assertEquals(expectedAccessControl, service.update(AccessControlType.ROLE,  1l, new AccessControlRecord("name")));
    }

    @Test
    @DisplayName("Test delete: should delete access control")
    void testDelete() {
        AccessControl expectedAccessControl = new AccessControl();
        expectedAccessControl.setId(1L);
        expectedAccessControl.setType(AccessControlType.ROLE);
        expectedAccessControl.setName("name");

        Mockito
                .when(accessControlRepository.findOne(Mockito.any(Specification.class)))
                .thenReturn(Optional.of(expectedAccessControl));
        Mockito.doNothing().when(accessControlRepository).deleteById(Mockito.any());

        service.delete(AccessControlType.ROLE,  1l);
        Mockito.verify(accessControlRepository, Mockito.times(1)).deleteById(Mockito.any());
    }
}
