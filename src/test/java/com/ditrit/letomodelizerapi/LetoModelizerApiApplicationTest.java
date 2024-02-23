package com.ditrit.letomodelizerapi;

import com.ditrit.letomodelizerapi.config.Constants;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.AccessControlService;
import com.ditrit.letomodelizerapi.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("Test class: LetoModelizerApiApplication")
class LetoModelizerApiApplicationTest {

    @Mock
    UserService userService;

    @Mock
    AccessControlService accessControlService;

    @Test
    @DisplayName("Test initSuperAdministrator: should not init with empty login")
    void testInitSuperAdministratorNotInit() {
        LetoModelizerApiApplication application = new LetoModelizerApiApplication();
        Environment environment = Mockito.mock(Environment.class);

        Mockito.when(environment.getProperty(Mockito.any())).thenReturn(null);
        assertFalse(application.initSuperAdministrator(environment, userService, accessControlService));

        Mockito.when(environment.getProperty(Mockito.any())).thenReturn("");
        assertFalse(application.initSuperAdministrator(environment, userService, accessControlService));

        Mockito.when(environment.getProperty(Mockito.any())).thenReturn(" ");
        assertFalse(application.initSuperAdministrator(environment, userService, accessControlService));
    }

    @Test
    @DisplayName("Test initSuperAdministrator: should init with login")
    void testInitSuperAdministrator() {
        LetoModelizerApiApplication application = new LetoModelizerApiApplication();
        Environment environment = Mockito.mock(Environment.class);

        Mockito.when(environment.getProperty(Mockito.any())).thenReturn("login");
        Mockito.when(userService.createAdmin("login")).thenReturn(new User());
        Mockito.doNothing().when(accessControlService).associateUser(AccessControlType.ROLE, Constants.SUPER_ADMINISTRATOR_ROLE_ID, "login");

        assertTrue(application.initSuperAdministrator(environment, userService, accessControlService));
    }
}
