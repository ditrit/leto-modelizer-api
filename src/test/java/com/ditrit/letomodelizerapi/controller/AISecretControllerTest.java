package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.helper.MockHelper;
import com.ditrit.letomodelizerapi.model.ai.AISecretRecord;
import com.ditrit.letomodelizerapi.persistence.model.AISecret;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.AISecretService;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("Test class: AIController")
class AISecretControllerTest extends MockHelper {

    @Mock
    UserService userService;

    @Mock
    UserPermissionService userPermissionService;

    @Mock
    AISecretService aiSecretService;

    @InjectMocks
    AISecretController controller;

    @Test
    @DisplayName("Test getAllSecrets: should return valid response to get all secrets.")
    void testGetAllSecrets() {
        User user = new User();
        user.setLogin("login");
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(user);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkPermission(Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any());
        Mockito.when(this.aiSecretService.findAll(Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        final Response response = this.controller.getAllSecrets(request, mockUriInfo(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test getSecretById: should return valid response to get secret by id.")
    void testGetSecretById() {
        User user = new User();
        user.setLogin("login");
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(user);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkPermission(Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any());
        Mockito.when(this.aiSecretService.findById(Mockito.any())).thenReturn(new AISecret());

        final Response response = this.controller.getSecretById(request, UUID.randomUUID());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test createSecret: should return valid response on create a secret.")
    void testCreateSecret() {
        User user = new User();
        user.setLogin("login");
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(user);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkPermission(Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any());
        Mockito.when(this.aiSecretService.create(Mockito.any())).thenReturn(new AISecret());

        final Response response = this.controller.createSecret(request, new AISecretRecord("key", "value"));

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test updateSecret: should return valid response on update a secret.")
    void testUpdateSecret() {
        User user = new User();
        user.setLogin("login");
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(user);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkPermission(Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any());
        Mockito.when(this.aiSecretService.update(Mockito.any(), Mockito.any()))
                .thenReturn(new AISecret());

        final Response response = this.controller.updateSecret(request, UUID.randomUUID(),
                new AISecretRecord("key", "value"));

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test updateSecret: should return valid response on delete a secret.")
    void testDeleteSecret() {
        User user = new User();
        user.setLogin("login");
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(user);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkPermission(Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any());
        Mockito.doNothing().when(aiSecretService).delete(Mockito.any());

        final Response response = this.controller.deleteSecret(request, UUID.randomUUID());

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        assertNull(response.getEntity());
    }
}
