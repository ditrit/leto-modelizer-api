package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.config.Constants;
import com.ditrit.letomodelizerapi.model.ai.AIConversationRecord;
import com.ditrit.letomodelizerapi.model.ai.AICreateFileRecord;
import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.model.file.FileRecord;
import com.ditrit.letomodelizerapi.persistence.model.AIConversation;
import com.ditrit.letomodelizerapi.persistence.model.AIMessage;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.persistence.repository.AIConversationRepository;
import com.ditrit.letomodelizerapi.persistence.repository.AIMessageRepository;
import com.ditrit.letomodelizerapi.persistence.specification.SpecificationHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPOutputStream;

/**
 * Service implementation for interacting with an Artificial Intelligence (AI).
 * This class provides concrete implementations of the methods defined in the IAService interface,
 * facilitating communication with an IA system for processing and responding to user requests.
 */
@Slf4j
@Service
@Transactional
public class AIServiceImpl implements AIService {

    /**
     * the host address of the Intelligent Assistant system, injected from application properties.
     */
    private final String aiHost;

    /**
     * The AIConversationRepository instance is injected by Spring's dependency injection mechanism.
     * This repository is used for performing database operations related to AIConversation entities, such as querying,
     * saving and updating user data.
     */
    private final AIConversationRepository aiConversationRepository;

    /**
     * The AIConversationRepository instance is injected by Spring's dependency injection mechanism.
     * This repository is used for performing database operations related to AIConversation entities, such as querying,
     * saving and updating user data.
     */
    private final AIMessageRepository aiMessageRepository;

    /**
     * Constructor for AIServiceImpl.
     * Initializes the service with the host address of the Artificial Intelligence (AI) system. This address is used to
     * configure the connection and communication with the AI, enabling the service to send requests and receive
     * responses from the AI.
     * The host address is injected from the application's configuration properties, allowing for flexible deployment
     * and configuration of the AI service endpoint.
     *
     * @param aiConversationRepository Repository to manage AIConversation.
     * @param aiMessageRepository Repository to manage AIMessage.
     * @param aiHost the host address of the Artificial Intelligence system, injected from application properties.
     */
    @Autowired
    public AIServiceImpl(final AIConversationRepository aiConversationRepository,
                         final AIMessageRepository aiMessageRepository,
                         @Value("${ai.host}") final String aiHost) {
        this.aiConversationRepository = aiConversationRepository;
        this.aiMessageRepository = aiMessageRepository;
        this.aiHost = aiHost;
    }

    /**
     * Sends a request to the AI service with the specified endpoint and request body.
     *
     * @param endpoint the URL of the AI endpoint to which the request is sent.
     * @param body the content to be sent in the body of the request.
     * @return the response body returned by the AI service.
     */
    public String sendRequest(final String endpoint, final String body) {
        try {
            URI uri = new URI(aiHost).resolve("api/").resolve(endpoint);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .headers(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                    .version(HttpClient.Version.HTTP_1_1)
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == ErrorType.AI_GENERATION_ERROR.getCode()) {
                throw new ApiException(ErrorType.AI_GENERATION_ERROR, "body", body);
            }

            if (!HttpStatus.valueOf(response.statusCode()).is2xxSuccessful()) {
                throw new ApiException(ErrorType.WRONG_VALUE, "url", uri.toString());
            }

            return response.body();
        } catch (URISyntaxException | IOException e) {
            throw new ApiException(ErrorType.WRONG_VALUE, "url", aiHost + "api/" + endpoint);
        } catch (InterruptedException e) {
            log.warn("InterruptedException during requesting ai with {} and {}", aiHost + "api/" + endpoint, body, e);
            Thread.currentThread().interrupt();
            throw new ApiException(ErrorType.INTERNAL_ERROR, "url", aiHost + "api/" + endpoint);
        }
    }

    /**
     * Sends files to the AI service with the specified conversation.
     *
     * @param conversation The conversation.
     * @param files Files to send.
     * @return The context returned by the AI response.
     */
    public String sendFiles(final AIConversation conversation, final List<FileRecord> files)
            throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();

        files.forEach(file -> {
            ObjectNode fileNode = mapper.createObjectNode();

            fileNode.put("path", file.path());
            fileNode.put("content", file.content());

            arrayNode.add(fileNode);
        });

        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("pluginName", conversation.getKey().split("/")[2]);
        json.set("files", arrayNode);

        JsonNode response = mapper.readTree(sendRequest("chat", json.toString()));

        return response.get(Constants.DEFAULT_CONTEXT_PROPERTY).asText();
    }

    /**
     * Compress message with GZIP.
     * @param message Message to compress.
     * @return Compressed message.
     * @throws IOException If an I/O error has occurred.
     */
    public byte[] compress(final String message) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(outputStream);

        gzip.write(message.getBytes(StandardCharsets.UTF_8));
        gzip.close();

        return outputStream.toByteArray();
    }

    @Override
    public String createFile(final AICreateFileRecord createFileRecord) {
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("pluginName", createFileRecord.plugin());
        json.put("description", createFileRecord.description());

        return sendRequest(createFileRecord.type(), json.toString());
    }

    @Override
    public AIConversation createConversation(final User user, final AIConversationRecord aiConversationRecord)
            throws JsonProcessingException {
        String key = String.format("%s/%s/%s",
                aiConversationRecord.project(), aiConversationRecord.diagram(), aiConversationRecord.plugin());
        if (aiConversationRepository.existsByUserIdAndKey(user.getId(), key)) {
            throw new ApiException(ErrorType.ENTITY_ALREADY_EXISTS, "key", key);
        }

        AIConversation conversation = new AIConversation();
        conversation.setUserId(user.getId());
        conversation.setKey(key);
        conversation.setChecksum(aiConversationRecord.checksum());
        conversation.setSize(0L);

        String context = sendFiles(conversation, aiConversationRecord.files());

        conversation.setContext(context);

        return aiConversationRepository.save(conversation);
    }

    @Override
    public AIConversation getConversationById(final User user, final UUID id) {
        return aiConversationRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ApiException(ErrorType.ENTITY_NOT_FOUND, "id", id.toString()));
    }

    @Override
    public AIConversation updateConversationById(final User user,
                                                 final UUID id,
                                                 final AIConversationRecord aiConversationRecord)
            throws JsonProcessingException {
        AIConversation conversation = aiConversationRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ApiException(ErrorType.ENTITY_NOT_FOUND, "id", id.toString()));

        String context = sendFiles(conversation, aiConversationRecord.files());

        conversation.setChecksum(aiConversationRecord.checksum());
        conversation.setContext(context);

        return aiConversationRepository.save(conversation);
    }

    @Override
    public void deleteConversationById(final UUID id) {
        AIConversation conversation = aiConversationRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorType.ENTITY_NOT_FOUND, "id", id.toString()));

        aiConversationRepository.delete(conversation);
    }

    @Override
    public void deleteConversationById(final User user, final UUID id) {
        AIConversation conversation = aiConversationRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ApiException(ErrorType.ENTITY_NOT_FOUND, "id", id.toString()));

        aiConversationRepository.delete(conversation);
    }

    @Override
    public AIMessage sendMessage(final User user, final UUID id, final String message) throws IOException {
        AIConversation conversation = aiConversationRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ApiException(ErrorType.ENTITY_NOT_FOUND, "id", id.toString()));

        byte[] compressedMessage = compress(message);
        Long size = conversation.getSize() + compressedMessage.length;

        AIMessage userMessage = new AIMessage();
        userMessage.setAiConversation(conversation.getId());
        userMessage.setIsUser(true);
        userMessage.setMessage(compressedMessage);
        aiMessageRepository.save(userMessage);

        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put(Constants.DEFAULT_CONTEXT_PROPERTY, conversation.getContext());
        json.put("message", message);

        JsonNode response = new ObjectMapper().readTree(sendRequest("chat", json.toString()));

        compressedMessage = compress(response.get("message").asText());
        size += compressedMessage.length;

        AIMessage aiMessage = new AIMessage();
        aiMessage.setAiConversation(conversation.getId());
        aiMessage.setIsUser(false);
        aiMessage.setMessage(compressedMessage);
        aiMessage = aiMessageRepository.save((aiMessage));

        conversation.setContext(response.get(Constants.DEFAULT_CONTEXT_PROPERTY).asText());
        conversation.setSize(size);
        aiConversationRepository.save(conversation);

        return aiMessage;
    }

    @Override
    public Page<AIConversation> findAll(final User user,
                                        final Map<String, String> immutableFilters,
                                        final Pageable pageable) {
        Map<String, String> filters = new HashMap<>(immutableFilters);
        filters.put("userId", user.getId().toString());

        return aiConversationRepository.findAll(
                new SpecificationHelper<>(AIConversation.class, filters),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.DESC, Constants.DEFAULT_UPDATE_DATE_PROPERTY))
                )
        );
    }

    @Override
    public Page<AIMessage> findAllMessages(final User user,
                                           final UUID id,
                                           final Map<String, String> immutableFilters,
                                           final Pageable pageable) {
        AIConversation conversation = aiConversationRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ApiException(ErrorType.ENTITY_NOT_FOUND, "id", id.toString()));

        Map<String, String> filters = new HashMap<>(immutableFilters);
        filters.put("aiConversation", conversation.getId().toString());

        return aiMessageRepository.findAll(
                new SpecificationHelper<>(AIMessage.class, filters),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.DESC, Constants.DEFAULT_UPDATE_DATE_PROPERTY)))
        );
    }

    @Override
    public Page<AIConversation> findAllConversations(final Map<String, String> immutableFilters,
                                                     final Pageable pageable) {

        return aiConversationRepository.findAll(
                new SpecificationHelper<>(AIConversation.class, immutableFilters),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.DESC, Constants.DEFAULT_UPDATE_DATE_PROPERTY))
        ));
    }
}
