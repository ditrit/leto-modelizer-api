package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.helper.MockHelper;
import com.ditrit.letomodelizerapi.model.ai.AIConversationRecord;
import com.ditrit.letomodelizerapi.model.ai.AICreateFileRecord;
import com.ditrit.letomodelizerapi.model.ai.AIMessageRecord;
import com.ditrit.letomodelizerapi.persistence.model.AIConversation;
import com.ditrit.letomodelizerapi.persistence.model.AIMessage;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.AIService;
import com.ditrit.letomodelizerapi.service.UserPermissionService;
import com.ditrit.letomodelizerapi.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("Test class: AIController")
class AIControllerTest extends MockHelper {

    @Mock
    UserService userService;
    @Mock
    UserPermissionService userPermissionService;
    @Mock
    AIService aiService;

    @InjectMocks
    AIController controller;

    @Test
    @DisplayName("Test generateFiles: should return valid response.")
    void testGenerateFiles() {
        User user = new User();
        user.setLogin("login");
        AICreateFileRecord aiCreateFileRecord = new AICreateFileRecord("@ditrit/kubernator-plugin", "diagram", "I want a sample of kubernetes code");

        Mockito
                .when(userService.getFromSession(Mockito.any()))
                .thenReturn(user);
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito
                .when(request.getSession())
                .thenReturn(session);

        Mockito.when(aiService.createFile(aiCreateFileRecord)).thenReturn("OK");
        final Response response = this.controller.generateFiles(request, aiCreateFileRecord);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test createConversations: should return valid response.")
    void testCreateConversations() throws JsonProcessingException {
        User user = new User();
        user.setLogin("login");
        AIConversationRecord aiConversationRecord = new AIConversationRecord(
                "project",
                "diagram",
                "@ditrit/kubernator-plugin",
                "checksum",
                List.of());
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(user);
        Mockito.when(aiService.createConversation(user, aiConversationRecord)).thenReturn(new AIConversation());

        Response response = this.controller.createConversations(request, aiConversationRecord);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test findAllConversations: should return valid response.")
    void testFindAllConversations() {
        User user = new User();
        user.setLogin("login");
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(user);
        Mockito.doNothing().when(userPermissionService).checkIsAdmin(Mockito.any(), Mockito.any());
        Mockito.when(aiService.findAllConversations(Mockito.any(), Mockito.any())).thenReturn(Page.empty());

        Response response = this.controller.findAllConversations(request, mockUriInfo(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test getConversationById: should return valid response.")
    void testGetConversationById() {
        User user = new User();
        user.setLogin("login");
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(user);
        Mockito.when(aiService.getConversationById(Mockito.any(), Mockito.any())).thenReturn(new AIConversation());

        Response response = this.controller.getConversationById(request, UUID.randomUUID());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test updateConversationById: should return valid response.")
    void testUpdateConversationById() throws JsonProcessingException {
        User user = new User();
        user.setLogin("login");
        AIConversationRecord aiConversationRecord = new AIConversationRecord(
          "project",
          "diagram",
          "plugin",
          "checksum",
          List.of()
        );
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(user);
        Mockito.when(aiService.updateConversationById(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(new AIConversation());

        Response response = this.controller.updateConversationById(request, UUID.randomUUID(), aiConversationRecord);

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test deleteConversationById: should return valid response for simple user.")
    void testDeleteConversationById() {
        User user = new User();
        user.setLogin("login");
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(user);
        Mockito.when(userPermissionService.hasPermission(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(false);
        Mockito.doNothing().when(aiService).deleteConversationById(Mockito.any(), Mockito.any());

        Response response = this.controller.deleteConversationById(request, UUID.randomUUID());

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        Mockito.verify(aiService, Mockito.times(0)).deleteConversationById(Mockito.any());
        Mockito.verify(aiService, Mockito.times(1)).deleteConversationById(Mockito.any(), Mockito.any());
    }

    @Test
    @DisplayName("Test deleteConversationById: should return valid response for admin.")
    void testDeleteConversationByIdAsAdmin() {
        User user = new User();
        user.setLogin("login");
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(user);
        Mockito.when(userPermissionService.hasPermission(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
        Mockito.doNothing().when(aiService).deleteConversationById(Mockito.any());

        Response response = this.controller.deleteConversationById(request, UUID.randomUUID());

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        Mockito.verify(aiService, Mockito.times(1)).deleteConversationById(Mockito.any());
        Mockito.verify(aiService, Mockito.times(0)).deleteConversationById(Mockito.any(), Mockito.any());
    }

    @Test
    @DisplayName("Test createConversationMessage: should return valid response.")
    void testCreateConversationMessage() throws IOException {
        User user = new User();
        user.setLogin("login");
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        AIMessage message = new AIMessage();
        message.setMessage("test".getBytes());

        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(user);
        Mockito.when(aiService.sendMessage(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(message);

        Response response = this.controller.createConversationMessage(request, UUID.randomUUID(), new AIMessageRecord("ok", "plugin"));

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    @DisplayName("Test findAllMessages: should return valid response.")
    void testFindAllMessages() {
        User user = new User();
        user.setLogin("login");
        HttpSession session = Mockito.mock(HttpSession.class);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(request.getSession()).thenReturn(session);
        Mockito.when(userService.getFromSession(Mockito.any())).thenReturn(user);
        Mockito.when(aiService.findAllMessages(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Page.empty());

        Response response = this.controller.findAllMessages(request, UUID.randomUUID(), mockUriInfo(), new QueryFilter());

        assertNotNull(response);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertNotNull(response.getEntity());
    }
}
