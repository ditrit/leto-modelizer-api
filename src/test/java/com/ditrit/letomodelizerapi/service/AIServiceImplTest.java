package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.ai.AIConversationRecord;
import com.ditrit.letomodelizerapi.model.ai.AICreateFileRecord;
import com.ditrit.letomodelizerapi.model.ai.AIMessageRecord;
import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.persistence.model.AIConversation;
import com.ditrit.letomodelizerapi.persistence.model.AIMessage;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.persistence.repository.AIConversationRepository;
import com.ditrit.letomodelizerapi.persistence.repository.AIMessageRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
@DisplayName("AIServiceImpl Unit Tests")
class AIServiceImplTest {

    @Mock
    AIConversationRepository aiConversationRepository;

    @Mock
    AIMessageRepository aiMessageRepository;

    AIServiceImpl newInstance() {
        return new AIServiceImpl(aiConversationRepository, aiMessageRepository, "http://localhost:8585/");
    }

    private static void mockHttpCall(MockedStatic<HttpClient> clientStatic, int expectedStatus, String expectedBody) throws IOException, InterruptedException {
        HttpResponse<Object> expectedResponse = Mockito.mock(HttpResponse.class);

        HttpClient client = Mockito.mock(HttpClient.class);
        HttpClient.Builder clientBuilder = Mockito.mock(HttpClient.Builder.class);

        Mockito.when(expectedResponse.statusCode()).thenReturn(expectedStatus);
        if (expectedBody != null){
            Mockito.when(expectedResponse.body()).thenReturn(expectedBody);
        }
        Mockito.when(client.send(Mockito.any(), Mockito.any())).thenReturn(expectedResponse);
        Mockito.when(clientBuilder.build()).thenReturn(client);

        clientStatic.when(HttpClient::newBuilder).thenReturn(clientBuilder);
    }
    
    @Test
    @DisplayName("Test createFile: should return valid response when a valid request is done")
    void testCreateFile() throws IOException, InterruptedException {
        String expectedBody = "[{\"name\": \"deployment.yaml\", \"content\": \"apiVersion: apps/v1\\nkind: Deployment\"}]";
        MockedStatic<HttpClient> clientStatic = Mockito.mockStatic(HttpClient.class);
        mockHttpCall(clientStatic, HttpStatus.OK.value(), expectedBody);

        AIServiceImpl service = newInstance();

        AICreateFileRecord aiCreateFileRecord = new AICreateFileRecord("@ditrit/kubernator-plugin", "diagram", "I want a sample of kubernetes code");
        assertEquals(expectedBody, service.createFile(aiCreateFileRecord));

        Mockito.reset();
        clientStatic.close();
    }

    @Test
    @DisplayName("Test createFile: should return a 530 due to an error in the body")
    void testCreateFileWrongBody() throws IOException, InterruptedException {

        MockedStatic<HttpClient> clientStatic = Mockito.mockStatic(HttpClient.class);
        mockHttpCall(clientStatic,530, null);

        AIServiceImpl service = newInstance();

        ApiException exception = null;

        try {
            AICreateFileRecord aiCreateFileRecord = new AICreateFileRecord("@ditrit/kubernator-plugin", "diagram", "coucou");
            service.createFile(aiCreateFileRecord);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.AI_GENERATION_ERROR.getStatus(), exception.getStatus());
        assertEquals(ErrorType.AI_GENERATION_ERROR.getMessage(), exception.getMessage());

        Mockito.reset();
        clientStatic.close();
    }

    @Test
    @DisplayName("Test createFile: should return a 206 (Wrong value) due to an error in the url")
    void testCreateFileWrongUrl() throws IOException, InterruptedException {
        MockedStatic<HttpClient> clientStatic = Mockito.mockStatic(HttpClient.class);
        mockHttpCall(clientStatic,400, null);

        AIServiceImpl service = newInstance();

        ApiException exception = null;

        try {
            AICreateFileRecord aiCreateFileRecord = new AICreateFileRecord("@ditrit/kubernator-plugin", "diagram", "coucou");
            service.createFile(aiCreateFileRecord);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.WRONG_VALUE.getStatus(), exception.getStatus());
        assertEquals(ErrorType.WRONG_VALUE.getMessage(), exception.getMessage());

        Mockito.reset();
        clientStatic.close();
    }

    @Test
    @DisplayName("Test createConversation: should throw ENTITY_ALREADY_EXISTS")
    void testCreateConversation() throws JsonProcessingException {
        Mockito.when(aiConversationRepository.existsByUserIdAndKey(Mockito.any(), Mockito.any())).thenReturn(true);
        AIServiceImpl service = newInstance();
        ApiException exception = null;
        User user = new User();
        user.setId(UUID.randomUUID());

        AIConversationRecord conversationRecord = new AIConversationRecord("test", "test", "test", "checksum", List.of());

        try {
            service.createConversation(user, conversationRecord);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.ENTITY_ALREADY_EXISTS.getStatus(), exception.getStatus());
        assertEquals(ErrorType.ENTITY_ALREADY_EXISTS.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("Test createConversation: should conversation with new context")
    void testCreateConversationWithNewContext() throws IOException, InterruptedException {
        MockedStatic<HttpClient> clientStatic = Mockito.mockStatic(HttpClient.class);
        mockHttpCall(clientStatic,200, "{\"context\": \"[\\\"newContext\\\"]\"}");

        AIConversation conversation = new AIConversation();
        conversation.setContext("newContext");

        Mockito.when(aiConversationRepository.existsByUserIdAndKey(Mockito.any(), Mockito.any())).thenReturn(false);
        Mockito.when(aiConversationRepository.save(Mockito.any())).thenReturn(conversation);
        AIServiceImpl service = newInstance();
        User user = new User();
        user.setId(UUID.randomUUID());

        AIConversationRecord conversationRecord = new AIConversationRecord("test", "test", "test", "checksum", List.of());

        AIConversation expectedConversation = service.createConversation(user, conversationRecord);

        assertEquals("newContext", expectedConversation.getContext());

        Mockito.reset();
        clientStatic.close();
    }

    @Test
    @DisplayName("Test getConversationById: should return conversation")
    void testGetConversationById() {
        AIConversation expectedConversation = new AIConversation();

        Mockito.when(aiConversationRepository.findByIdAndUserId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(expectedConversation));
        AIServiceImpl service = newInstance();
        User user = new User();
        user.setId(UUID.randomUUID());

        AIConversation conversation = service.getConversationById(user, UUID.randomUUID());

        assertEquals(conversation, expectedConversation);
    }

    @Test
    @DisplayName("Test getConversationById: should throw exception on unknown id")
    void testGetConversationByIdThrowException() {
        Mockito.when(aiConversationRepository.findByIdAndUserId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.empty());
        AIServiceImpl service = newInstance();
        User user = new User();
        user.setId(UUID.randomUUID());

        ApiException exception = null;

        try {
            service.getConversationById(user, UUID.randomUUID());
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getStatus(), exception.getStatus());
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("Test updateConversationById: should return updated conversation")
    void testUpdateConversationById() throws IOException, InterruptedException {
        MockedStatic<HttpClient> clientStatic = Mockito.mockStatic(HttpClient.class);
        mockHttpCall(clientStatic,200, "{\"context\": \"[\\\"newContext\\\"]\"}");
        AIConversation expectedConversation = new AIConversation();
        expectedConversation.setKey("test/test/test");
        expectedConversation.setSize(0L);

        Mockito.when(aiConversationRepository.findByIdAndUserId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(expectedConversation));
        Mockito.when(aiConversationRepository.save(Mockito.any())).thenReturn(expectedConversation);
        AIServiceImpl service = newInstance();
        User user = new User();
        user.setId(UUID.randomUUID());
        AIConversationRecord conversationRecord = new AIConversationRecord("test", "test", "test", "checksum", List.of());

        AIConversation conversation = service.updateConversationById(user, UUID.randomUUID(), conversationRecord);

        assertEquals(conversation, expectedConversation);

        Mockito.reset();
        clientStatic.close();
    }

    @Test
    @DisplayName("Test updateConversationById: should throw exception on unknown id")
    void testUpdateConversationByIdThrowException() throws IOException {
        Mockito.when(aiConversationRepository.findByIdAndUserId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.empty());
        AIServiceImpl service = newInstance();
        User user = new User();
        user.setId(UUID.randomUUID());
        AIConversationRecord conversationRecord = new AIConversationRecord("test", "test", "test", "checksum", List.of());

        ApiException exception = null;

        try {
            service.updateConversationById(user, UUID.randomUUID(), conversationRecord);
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getStatus(), exception.getStatus());
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("Test deleteConversationById: should delete conversation")
    void testDeleteConversationById() {
        AIConversation expectedConversation = new AIConversation();

        Mockito.when(aiConversationRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(expectedConversation));
        Mockito.doNothing().when(aiConversationRepository).delete(Mockito.any());
        AIServiceImpl service = newInstance();
        ApiException exception = null;

        try {
            service.deleteConversationById(UUID.randomUUID());
        } catch (ApiException e) {
            exception = e;
        }

        assertNull(exception);
    }

    @Test
    @DisplayName("Test deleteConversationById: should throw exception on unknown id")
    void testDeleteConversationByIdThrowException() {
        Mockito.when(aiConversationRepository.findById(Mockito.any()))
                .thenReturn(Optional.empty());
        AIServiceImpl service = newInstance();
        ApiException exception = null;

        try {
            service.deleteConversationById(UUID.randomUUID());
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getStatus(), exception.getStatus());
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("Test deleteConversationById: should delete conversation")
    void testDeleteConversationByIdAndUser() {
        AIConversation expectedConversation = new AIConversation();

        Mockito.when(aiConversationRepository.findByIdAndUserId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(expectedConversation));
        Mockito.doNothing().when(aiConversationRepository).delete(Mockito.any());
        AIServiceImpl service = newInstance();
        User user = new User();
        user.setId(UUID.randomUUID());
        ApiException exception = null;

        try {
            service.deleteConversationById(user, UUID.randomUUID());
        } catch (ApiException e) {
            exception = e;
        }

        assertNull(exception);
    }

    @Test
    @DisplayName("Test deleteConversationById: should throw exception on unknown id")
    void testDeleteConversationByIdAndUserThrowException() {
        Mockito.when(aiConversationRepository.findByIdAndUserId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.empty());
        AIServiceImpl service = newInstance();
        User user = new User();
        user.setId(UUID.randomUUID());
        ApiException exception = null;

        try {
            service.deleteConversationById(user, UUID.randomUUID());
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getStatus(), exception.getStatus());
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("Test sendMessage: should AI message")
    void testSendMessage() throws IOException, InterruptedException {
        MockedStatic<HttpClient> clientStatic = Mockito.mockStatic(HttpClient.class);
        mockHttpCall(clientStatic,200, "{\"context\": \"[\\\"newContext\\\"]\", \"message\": \"test\"}");
        AIConversation expectedConversation = new AIConversation();
        expectedConversation.setId(UUID.randomUUID());
        expectedConversation.setSize(0L);

        AIMessage expectedMessage = new AIMessage();

        Mockito.when(aiConversationRepository.findByIdAndUserId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.of(expectedConversation));
        Mockito.when(aiConversationRepository.save(Mockito.any())).thenReturn(expectedConversation);
        Mockito.when(aiMessageRepository.save(Mockito.any())).thenReturn(expectedMessage);
        AIServiceImpl service = newInstance();
        User user = new User();
        user.setId(UUID.randomUUID());

        AIMessage message = service.sendMessage(user, UUID.randomUUID(), new AIMessageRecord("ok", "plugin"));

        assertEquals(message, expectedMessage);

        Mockito.reset();
        clientStatic.close();
    }

    @Test
    @DisplayName("Test sendMessage: should throw exception on unknown id")
    void testSendMessageThrowException() throws IOException {
        Mockito.when(aiConversationRepository.findByIdAndUserId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.empty());
        AIServiceImpl service = newInstance();
        User user = new User();
        user.setId(UUID.randomUUID());
        ApiException exception = null;

        try {
            service.sendMessage(user, UUID.randomUUID(), new AIMessageRecord("test", "plugin"));
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getStatus(), exception.getStatus());
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("Test findAll: should return conversations")
    void testFindAll() {
        AIConversation expectedConversation = new AIConversation();
        expectedConversation.setId(UUID.randomUUID());

        Mockito.when(aiConversationRepository.findAll(Mockito.any(Specification.class), Mockito.any()))
                .thenReturn(Page.empty());
        AIServiceImpl service = newInstance();
        User user = new User();
        user.setId(UUID.randomUUID());

        assertEquals(Page.empty(), service.findAll(user, Map.of(), Pageable.ofSize(10)));
    }

    @Test
    @DisplayName("Test findAllMessages: should return messages")
    void testFindAllMessages() {
        AIConversation expectedConversation = new AIConversation();
        expectedConversation.setId(UUID.randomUUID());

        Mockito.when(aiConversationRepository.findByIdAndUserId(Mockito.any(), Mockito.any()))
                        .thenReturn(Optional.of(expectedConversation));
        Mockito.when(aiMessageRepository.findAll(Mockito.any(Specification.class), Mockito.any()))
                .thenReturn(Page.empty());
        AIServiceImpl service = newInstance();
        User user = new User();
        user.setId(UUID.randomUUID());

        assertEquals(Page.empty(), service.findAllMessages(user, UUID.randomUUID(), Map.of(), Pageable.ofSize(10)));
    }

    @Test
    @DisplayName("Test findAllMessages: should return messages")
    void testFindAllMessagesThrowException() {
        Mockito.when(aiConversationRepository.findByIdAndUserId(Mockito.any(), Mockito.any()))
                .thenReturn(Optional.empty());

        AIServiceImpl service = newInstance();
        User user = new User();
        user.setId(UUID.randomUUID());
        ApiException exception = null;

        try {
            service.findAllMessages(user, UUID.randomUUID(), Map.of(), Pageable.ofSize(10));
        } catch (ApiException e) {
            exception = e;
        }

        assertNotNull(exception);
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getStatus(), exception.getStatus());
        assertEquals(ErrorType.ENTITY_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("Test findAllConversations: should return conversations")
    void testFindAllConversations() {
        AIConversation expectedConversation = new AIConversation();
        expectedConversation.setId(UUID.randomUUID());

        Mockito.when(aiConversationRepository.findAll(Mockito.any(Specification.class), Mockito.any()))
                .thenReturn(Page.empty());
        AIServiceImpl service = newInstance();

        assertEquals(Page.empty(), service.findAllConversations(Map.of(), Pageable.ofSize(10)));
    }
}
