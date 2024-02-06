package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlRecord;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.persistence.model.AccessControl;
import com.ditrit.letomodelizerapi.persistence.model.AccessControlTree;
import com.ditrit.letomodelizerapi.persistence.model.AccessControlTreeView;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.persistence.model.UserAccessControl;
import com.ditrit.letomodelizerapi.persistence.repository.AccessControlRepository;
import com.ditrit.letomodelizerapi.persistence.repository.AccessControlTreeRepository;
import com.ditrit.letomodelizerapi.persistence.repository.AccessControlTreeViewRepository;
import com.ditrit.letomodelizerapi.persistence.repository.UserAccessControlRepository;
import com.ditrit.letomodelizerapi.persistence.repository.UserAccessControlViewRepository;
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

    @Mock
    UserAccessControlViewRepository userAccessControlViewRepository;

    @Mock
    UserAccessControlRepository userAccessControlRepository;

    @Mock
    AccessControlTreeViewRepository accessControlTreeViewRepository;

    @Mock
    AccessControlTreeRepository accessControlTreeRepository;

    @Mock
    UserService userService;

    @InjectMocks
    AccessControlServiceImpl service;

    @Test
    @DisplayName("Test findAll: should return wanted access controls")
    void testFindAll() {
        Mockito
                .when(accessControlRepository.findAll(Mockito.any(Specification.class), Mockito.any()))
                .thenReturn(Page.empty());

        assertEquals(Page.empty(), service.findAll(AccessControlType.ROLE, Map.of(), Pageable.ofSize(10)));
    }

    @Test
    @DisplayName("Test findAll: should return all access controls of user")
    void testFindAllOfUsers() {
        User user = new User();
        user.setId(1L);

        Mockito
                .when(userAccessControlViewRepository.findAll(Mockito.any(Specification.class), Mockito.any()))
                .thenReturn(Page.empty());

        assertEquals(Page.empty(), service.findAll(AccessControlType.ROLE, user, Map.of(), Pageable.ofSize(10)));
    }

    @Test
    @DisplayName("Test findAllAccessControls: should return all access controls of access control")
    void testFindAllAccessControls() {
        Mockito
                .when(accessControlTreeViewRepository.findAll(Mockito.any(Specification.class), Mockito.any()))
                .thenReturn(Page.empty());

        assertEquals(Page.empty(), service.findAllAccessControls(1l, AccessControlType.ROLE, Map.of(), Pageable.ofSize(10)));
    }

    @Test
    @DisplayName("Test findAllUsers: should return all users of access control")
    void testFindAllUsers() {
        AccessControl expectedAccessControl = new AccessControl();
        expectedAccessControl.setId(1L);
        expectedAccessControl.setType(AccessControlType.ROLE);
        expectedAccessControl.setName("name");
        Mockito
                .when(accessControlRepository.findOne(Mockito.any(Specification.class)))
                .thenReturn(Optional.of(expectedAccessControl));
        Mockito
                .when(userAccessControlViewRepository.findAll(Mockito.any(Specification.class), Mockito.any()))
                .thenReturn(Page.empty());

        assertEquals(Page.empty(), service.findAllUsers(AccessControlType.ROLE, 1l, Map.of(), Pageable.ofSize(10)));
    }

    @Test
    @DisplayName("Test findById: should return wanted access controls")
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

    @Test
    @DisplayName("Test associateUser: should associate access control to user")
    void testAssociateUser() {
        AccessControl accessControl = new AccessControl();
        accessControl.setId(1L);
        accessControl.setType(AccessControlType.ROLE);
        accessControl.setName("name");

        User user = new User();
        user.setId(1L);

        Mockito
                .when(userService.findByLogin(Mockito.any()))
                .thenReturn(user);
        Mockito
                .when(accessControlRepository.findOne(Mockito.any(Specification.class)))
                .thenReturn(Optional.of(accessControl));
        Mockito
                .when(userAccessControlRepository.findByAccessControlIdAndUserId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.empty());
        Mockito
                .when(userAccessControlRepository.save(Mockito.any()))
                .thenReturn(new UserAccessControl());

        service.associateUser(AccessControlType.ROLE, 1l, "login");

        Mockito.verify(userAccessControlRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Test associateUser: should throw exception on already existing association")
    void testAssociateUserThrow() {
        AccessControl accessControl = new AccessControl();
        accessControl.setId(1L);
        accessControl.setType(AccessControlType.ROLE);
        accessControl.setName("name");

        User user = new User();
        user.setId(1L);

        Mockito
                .when(userService.findByLogin(Mockito.any()))
                .thenReturn(user);
        Mockito
                .when(accessControlRepository.findOne(Mockito.any(Specification.class)))
                .thenReturn(Optional.of(accessControl));
        Mockito
                .when(userAccessControlRepository.findByAccessControlIdAndUserId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(new UserAccessControl()));
        ApiException exception = null;

        try {
            service.associateUser(AccessControlType.ROLE, 1l, "login");
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.ENTITY_ALREADY_EXISTS.getStatus(), exception.getStatus());
        assertEquals("association", exception.getError().getField());
    }

    @Test
    @DisplayName("Test dissociateUser: should dissociate access control to user")
    void testDissociateUser() {
        AccessControl accessControl = new AccessControl();
        accessControl.setId(1L);
        accessControl.setType(AccessControlType.ROLE);
        accessControl.setName("name");

        User user = new User();
        user.setId(1L);

        Mockito
                .when(userService.findByLogin(Mockito.any()))
                .thenReturn(user);
        Mockito
                .when(accessControlRepository.findOne(Mockito.any(Specification.class)))
                .thenReturn(Optional.of(accessControl));
        Mockito
                .when(userAccessControlRepository.findByAccessControlIdAndUserId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(new UserAccessControl()));
        Mockito
                .doNothing()
                .when(userAccessControlRepository)
                .delete(Mockito.any());

        service.dissociateUser(AccessControlType.ROLE, 1l, "login");

        Mockito.verify(userAccessControlRepository, Mockito.times(1)).delete(Mockito.any());
    }

    @Test
    @DisplayName("Test dissociateUser: should throw exception on unknown association")
    void testDissociateUserThrow() {
        AccessControl accessControl = new AccessControl();
        accessControl.setId(1L);
        accessControl.setType(AccessControlType.ROLE);
        accessControl.setName("name");

        User user = new User();
        user.setId(1L);

        Mockito
                .when(userService.findByLogin(Mockito.any()))
                .thenReturn(user);
        Mockito
                .when(accessControlRepository.findOne(Mockito.any(Specification.class)))
                .thenReturn(Optional.of(accessControl));
        Mockito
                .when(userAccessControlRepository.findByAccessControlIdAndUserId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.empty());
        ApiException exception = null;

        try {
            service.dissociateUser(AccessControlType.ROLE, 1l, "login");
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getStatus(), exception.getStatus());
        assertEquals("association", exception.getError().getField());
    }



    @Test
    @DisplayName("Test associate: should associate access control to another")
    void testAssociate() {
        AccessControl accessControl = new AccessControl();
        accessControl.setId(1L);
        accessControl.setType(AccessControlType.ROLE);
        accessControl.setName("name");

        Mockito
                .when(accessControlRepository.findOne(Mockito.any(Specification.class)))
                .thenReturn(Optional.of(accessControl));
        Mockito
                .when(accessControlTreeViewRepository.findByAccessControlIdAndParentAccessControlId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.empty());

        service.associate(AccessControlType.ROLE, 1l, AccessControlType.ROLE, 2l);

        Mockito.verify(accessControlTreeRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Test associate: should throw exception on already existing association")
    void testAssociateThrow() {
        AccessControl accessControl = new AccessControl();
        accessControl.setId(1L);
        accessControl.setType(AccessControlType.ROLE);
        accessControl.setName("name");

        Mockito
                .when(accessControlRepository.findOne(Mockito.any(Specification.class)))
                .thenReturn(Optional.of(accessControl));
        Mockito
                .when(accessControlTreeViewRepository.findByAccessControlIdAndParentAccessControlId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(new AccessControlTreeView()));
        ApiException exception = null;

        try {
            service.associate(AccessControlType.ROLE, 1l, AccessControlType.ROLE, 2l);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.ENTITY_ALREADY_EXISTS.getStatus(), exception.getStatus());
        assertEquals("association", exception.getError().getField());
    }

    @Test
    @DisplayName("Test dissociate: should dissociate access control to another")
    void testDissociate() {
        AccessControl accessControl = new AccessControl();
        accessControl.setId(1L);
        accessControl.setType(AccessControlType.ROLE);
        accessControl.setName("name");

        Mockito
                .when(accessControlRepository.findOne(Mockito.any(Specification.class)))
                .thenReturn(Optional.of(accessControl));
        Mockito
                .when(accessControlTreeRepository.findByParentAndCurrent(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(new AccessControlTree()));
        Mockito
                .doNothing()
                .when(accessControlTreeRepository)
                .delete(Mockito.any());

        service.dissociate(AccessControlType.ROLE, 1l, AccessControlType.ROLE, 2l);

        Mockito.verify(accessControlTreeRepository, Mockito.times(1)).delete(Mockito.any());
    }

    @Test
    @DisplayName("Test dissociate: should throw exception on unknown association")
    void testDissociateThrow() {
        AccessControl accessControl = new AccessControl();
        accessControl.setId(1L);
        accessControl.setType(AccessControlType.ROLE);
        accessControl.setName("name");

        Mockito
                .when(accessControlRepository.findOne(Mockito.any(Specification.class)))
                .thenReturn(Optional.of(accessControl));
        Mockito
                .when(accessControlTreeRepository.findByParentAndCurrent(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.empty());
        ApiException exception = null;

        try {
            service.dissociate(AccessControlType.ROLE, 1l, AccessControlType.ROLE, 2l);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getStatus(), exception.getStatus());
        assertEquals("association", exception.getError().getField());
    }
}
