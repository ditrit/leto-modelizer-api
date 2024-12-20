package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertNull;
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

        assertEquals(Page.empty(), service.findAll(AccessControlType.ROLE, new LinkedMultiValueMap<>(), new QueryFilter()));
    }

    @Test
    @DisplayName("Test findAll: should return all access controls of user")
    void testFindAllOfUsers() {
        User user = new User();
        user.setId(UUID.randomUUID());

        Mockito
                .when(userAccessControlViewRepository.findAll(Mockito.any(Specification.class), Mockito.any()))
                .thenReturn(Page.empty());

        assertEquals(Page.empty(), service.findAll(AccessControlType.ROLE, user, new LinkedMultiValueMap<>(), new QueryFilter()));
    }

    @Test
    @DisplayName("Test findAllAccessControls: should return all access controls of access control")
    void testFindAllAccessControls() {
        UUID id = UUID.randomUUID();
        AccessControl accessControl = new AccessControl();
        accessControl.setId(id);

        Mockito
                .when(accessControlTreeViewRepository.findAll(Mockito.any(Specification.class), Mockito.any()))
                .thenReturn(Page.empty());
        Mockito
                .when(accessControlRepository.findOne(Mockito.any(Specification.class)))
                .thenReturn(Optional.of(accessControl));

        assertEquals(Page.empty(), service.findAllAccessControls(AccessControlType.ROLE, id, AccessControlType.ROLE, new LinkedMultiValueMap<>(), new QueryFilter()));
    }

    @Test
    @DisplayName("Test findAllChildren: should return all users of access control")
    void testFindAllChildren() {
        UUID id = UUID.randomUUID();
        AccessControl expectedAccessControl = new AccessControl();
        expectedAccessControl.setId(id);
        expectedAccessControl.setType(AccessControlType.ROLE);
        expectedAccessControl.setName("name");
        Mockito
                .when(accessControlRepository.findOne(Mockito.any(Specification.class)))
                .thenReturn(Optional.of(expectedAccessControl));
        Mockito
                .when(accessControlTreeViewRepository.findAll(Mockito.any(Specification.class), Mockito.any()))
                .thenReturn(Page.empty());

        assertEquals(Page.empty(), service.findAllChildren(AccessControlType.ROLE, id, AccessControlType.GROUP, new LinkedMultiValueMap<>(), new QueryFilter()));
    }

    @Test
    @DisplayName("Test findAllUsers: should return all users of access control")
    void testFindAllUsers() {
        AccessControl expectedAccessControl = new AccessControl();
        expectedAccessControl.setId(UUID.randomUUID());
        expectedAccessControl.setType(AccessControlType.ROLE);
        expectedAccessControl.setName("name");
        Mockito
                .when(accessControlRepository.findOne(Mockito.any(Specification.class)))
                .thenReturn(Optional.of(expectedAccessControl));
        Mockito
                .when(userAccessControlViewRepository.findAll(Mockito.any(Specification.class), Mockito.any()))
                .thenReturn(Page.empty());

        assertEquals(Page.empty(), service.findAllUsers(AccessControlType.ROLE, UUID.randomUUID(), new LinkedMultiValueMap<>(), new QueryFilter()));
    }

    @Test
    @DisplayName("Test findById: should return wanted access controls")
    void testFindById() {
        AccessControl expectedAccessControl = new AccessControl();
        expectedAccessControl.setId(UUID.randomUUID());
        expectedAccessControl.setType(AccessControlType.ROLE);
        expectedAccessControl.setName("name");

        Mockito
                .when(accessControlRepository.findOne(Mockito.any(Specification.class)))
                .thenReturn(Optional.of(expectedAccessControl));

        assertEquals(expectedAccessControl, service.findById(AccessControlType.ROLE, UUID.randomUUID()));
    }

    @Test
    @DisplayName("Test findById: should throw exception")
    void testFindByIdThrow() {
        Mockito
                .when(accessControlRepository.findOne(Mockito.any(Specification.class)))
                .thenReturn(Optional.empty());
        ApiException exception = null;
        UUID uuid = UUID.randomUUID();

        try {
            service.findById(AccessControlType.ROLE, uuid);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getStatus(), exception.getStatus());
        assertEquals("id", exception.getError().getField());
        assertEquals(uuid.toString(), exception.getError().getValue());
    }

    @Test
    @DisplayName("Test create: should create access control")
    void testCreate() {
        AccessControl expectedAccessControl = new AccessControl();
        expectedAccessControl.setId(UUID.randomUUID());
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
        expectedAccessControl.setId(UUID.randomUUID());
        expectedAccessControl.setType(AccessControlType.ROLE);
        expectedAccessControl.setName("name");

        Mockito
                .when(accessControlRepository.findOne(Mockito.any(Specification.class)))
                .thenReturn(Optional.of(expectedAccessControl));
        Mockito
                .when(accessControlRepository.save(Mockito.any()))
                .thenReturn(expectedAccessControl);

        assertEquals(expectedAccessControl, service.update(AccessControlType.ROLE,  UUID.randomUUID(), new AccessControlRecord("name")));
    }

    @Test
    @DisplayName("Test delete: should delete access control")
    void testDelete() {
        AccessControl expectedAccessControl = new AccessControl();
        expectedAccessControl.setId(UUID.randomUUID());
        expectedAccessControl.setType(AccessControlType.ROLE);
        expectedAccessControl.setName("name");

        Mockito
                .when(accessControlRepository.findOne(Mockito.any(Specification.class)))
                .thenReturn(Optional.of(expectedAccessControl));
        Mockito.doNothing().when(accessControlRepository).deleteById(Mockito.any());

        service.delete(AccessControlType.ROLE,  UUID.randomUUID());
        Mockito.verify(accessControlRepository, Mockito.times(1)).deleteById(Mockito.any());
    }

    @Test
    @DisplayName("Test associateUser: should associate access control to user")
    void testAssociateUser() {
        AccessControl accessControl = new AccessControl();
        accessControl.setId(UUID.randomUUID());
        accessControl.setType(AccessControlType.ROLE);
        accessControl.setName("name");

        User user = new User();
        user.setId(UUID.randomUUID());

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

        service.associateUser(AccessControlType.ROLE, UUID.randomUUID(), "login");

        Mockito.verify(userAccessControlRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Test associateUser: should do nothing on already existing association")
    void testAssociateUserDoNothing() {
        AccessControl accessControl = new AccessControl();
        accessControl.setId(UUID.randomUUID());
        accessControl.setType(AccessControlType.ROLE);
        accessControl.setName("name");

        User user = new User();
        user.setId(UUID.randomUUID());

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
            service.associateUser(AccessControlType.ROLE, UUID.randomUUID(), "login");
        } catch (ApiException e) {
            exception = e;
        }

        assertNull(exception);
        Mockito.verify(userAccessControlRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    @DisplayName("Test dissociateUser: should dissociate access control to user")
    void testDissociateUser() {
        AccessControl accessControl = new AccessControl();
        accessControl.setId(UUID.randomUUID());
        accessControl.setType(AccessControlType.ROLE);
        accessControl.setName("name");

        User user = new User();
        user.setId(UUID.randomUUID());

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

        service.dissociateUser(AccessControlType.ROLE, UUID.randomUUID(), "login");

        Mockito.verify(userAccessControlRepository, Mockito.times(1)).delete(Mockito.any());
    }

    @Test
    @DisplayName("Test dissociateUser: should throw exception on unknown association")
    void testDissociateUserThrow() {
        AccessControl accessControl = new AccessControl();
        accessControl.setId(UUID.randomUUID());
        accessControl.setType(AccessControlType.ROLE);
        accessControl.setName("name");

        User user = new User();
        user.setId(UUID.randomUUID());

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
            service.dissociateUser(AccessControlType.ROLE, UUID.randomUUID(), "login");
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
        accessControl.setId(UUID.randomUUID());
        accessControl.setType(AccessControlType.ROLE);
        accessControl.setName("name");

        Mockito
                .when(accessControlRepository.findOne(Mockito.any(Specification.class)))
                .thenReturn(Optional.of(accessControl));
        Mockito
                .when(accessControlTreeViewRepository.findByIdAndParentId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.empty());

        service.associate(AccessControlType.ROLE, UUID.randomUUID(), AccessControlType.ROLE, UUID.randomUUID());

        Mockito.verify(accessControlTreeRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("Test associate: should do nothing on already existing association")
    void testAssociateDoNothing() {
        AccessControl accessControl = new AccessControl();
        accessControl.setId(UUID.randomUUID());
        accessControl.setType(AccessControlType.ROLE);
        accessControl.setName("name");

        Mockito
                .when(accessControlRepository.findOne(Mockito.any(Specification.class)))
                .thenReturn(Optional.of(accessControl));
        Mockito
                .when(accessControlTreeViewRepository.findByIdAndParentId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(new AccessControlTreeView()));
        ApiException exception = null;

        try {
            service.associate(AccessControlType.ROLE, UUID.randomUUID(), AccessControlType.ROLE, UUID.randomUUID());
        } catch (ApiException e) {
            exception = e;
        }

        assertNull(exception);
        Mockito.verify(accessControlTreeRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    @DisplayName("Test dissociate: should dissociate access control to another")
    void testDissociate() {
        AccessControl accessControl = new AccessControl();
        accessControl.setId(UUID.randomUUID());
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

        service.dissociate(AccessControlType.ROLE, UUID.randomUUID(), AccessControlType.ROLE, UUID.randomUUID());

        Mockito.verify(accessControlTreeRepository, Mockito.times(1)).delete(Mockito.any());
    }

    @Test
    @DisplayName("Test dissociate: should throw exception on unknown association")
    void testDissociateThrow() {
        AccessControl accessControl = new AccessControl();
        accessControl.setId(UUID.randomUUID());
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
            service.dissociate(AccessControlType.ROLE, UUID.randomUUID(), AccessControlType.ROLE, UUID.randomUUID());
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getStatus(), exception.getStatus());
        assertEquals("association", exception.getError().getField());
    }

    @Test
    @DisplayName("Test getSuperAdministratorId: should return id")
    void testGetSuperAdministratorId() {
        UUID id = UUID.randomUUID();
        AccessControl accessControl = new AccessControl();
        accessControl.setId(id);

        Mockito.when(accessControlRepository.findByName(Mockito.any())).thenReturn(accessControl);

        assertEquals(id, service.getSuperAdministratorId());
        // Check that retrieve saved uuid instead of re call database.
        assertEquals(id, service.getSuperAdministratorId());

        Mockito.verify(accessControlRepository, Mockito.times(1)).findByName(Mockito.any());
    }
}
