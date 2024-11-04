package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.helper.MockHelper;
import com.ditrit.letomodelizerapi.model.ai.AIConfigurationRecord;
import com.ditrit.letomodelizerapi.model.ai.UpdateMultipleAIConfigurationRecord;
import com.ditrit.letomodelizerapi.persistence.model.AIConfiguration;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.AIConfigurationService;
import com.ditrit.letomodelizerapi.service.AISecretService;
import com.ditrit.letomodelizerapi.service.AIService;
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
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("Test class: AIController")
class AIConfigurationControllerTest extends MockHelper {

    @Mock
    UserService userService;

    @Mock
    UserPermissionService userPermissionService;

    @Mock
    AIConfigurationService aiConfigurationService;

    @Mock
    AISecretService aiSecretService;

    @Mock
    AIService aiService;

    @InjectMocks
    AIConfigurationController controller;

    @Test
    @DisplayName("Test getAllConfigurations: should return valid response to get all configurations.")
    void testGetAllConfigurations() {
        User user = new User();
        user.setLogin("login");
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(user);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkPermission(Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any());
        Mockito.when(this.aiConfigurationService.findAll(Mockito.any(), Mockito.any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        final Response response = this.controller.getAllConfigurations(request, mockUriInfo(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test getConfigurationById: should return valid response to get configuration by id.")
    void testGetConfigurationById() {
        User user = new User();
        user.setLogin("login");
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(user);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkPermission(Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any());
        Mockito.when(this.aiConfigurationService.findById(Mockito.any())).thenReturn(new AIConfiguration());

        final Response response = this.controller.getConfigurationById(request, UUID.randomUUID());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test createConfiguration: should return valid response on create a configuration.")
    void testCreateConfiguration() {
        User user = new User();
        user.setLogin("login");
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(user);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkPermission(Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any());
        Mockito.when(this.aiConfigurationService.create(Mockito.any())).thenReturn(new AIConfiguration());
        Mockito.when(this.aiSecretService.generateConfiguration()).thenReturn("test".getBytes());
        Mockito.doNothing().when(this.aiService).sendConfiguration(Mockito.any());

        final Response response = this.controller.createConfiguration(request,
                new AIConfigurationRecord("handler","key", "value"));

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test updateConfiguration: should return valid response on update a configuration.")
    void testUpdateConfiguration() {
        User user = new User();
        user.setLogin("login");
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(user);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkPermission(Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any());
        Mockito.when(this.aiConfigurationService.update(Mockito.any(), Mockito.any()))
                .thenReturn(new AIConfiguration());
        Mockito.when(this.aiSecretService.generateConfiguration()).thenReturn("test".getBytes());
        Mockito.doNothing().when(this.aiService).sendConfiguration(Mockito.any());

        final Response response = this.controller.updateConfiguration(request, UUID.randomUUID(),
                new AIConfigurationRecord("handler", "key", "value"));

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test updateConfiguration: should update multiples times.")
    void testUpdateConfigurations() {
        User user = new User();
        user.setLogin("login");
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(user);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkPermission(Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any());
        Mockito.when(this.aiConfigurationService.update(Mockito.any(), Mockito.any()))
                .thenReturn(new AIConfiguration());
        Mockito.when(this.aiSecretService.generateConfiguration()).thenReturn("test".getBytes());
        Mockito.doNothing().when(this.aiService).sendConfiguration(Mockito.any());

        final Response response = this.controller.updateConfiguration(request,
                List.of(new UpdateMultipleAIConfigurationRecord(UUID.randomUUID(), "handler", "key", "value")));

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test deleteConfiguration: should return valid response on delete a configuration.")
    void testDeleteConfiguration() {
        User user = new User();
        user.setLogin("login");
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(user);
        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.doNothing().when(userPermissionService).checkPermission(Mockito.any(), Mockito.any(), Mockito.any(),
                Mockito.any());
        Mockito.doNothing().when(aiConfigurationService).delete(Mockito.any());
        Mockito.when(aiConfigurationService.findById(Mockito.any())).thenReturn(new AIConfiguration());
        Mockito.when(this.aiSecretService.generateConfiguration()).thenReturn("test".getBytes());
        Mockito.doNothing().when(this.aiService).sendConfiguration(Mockito.any());

        final Response response = this.controller.deleteConfiguration(request, UUID.randomUUID());

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        assertNull(response.getEntity());
    }
}
