package com.ditrit.letomodelizerapi.controller;


import com.ditrit.letomodelizerapi.config.Constants;
import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.model.BeanMapper;
import com.ditrit.letomodelizerapi.model.ai.AIConversationDTO;
import com.ditrit.letomodelizerapi.model.ai.AIConversationRecord;
import com.ditrit.letomodelizerapi.model.ai.AICreateFileRecord;
import com.ditrit.letomodelizerapi.model.ai.AIMessageDTO;
import com.ditrit.letomodelizerapi.model.ai.AIMessageRecord;
import com.ditrit.letomodelizerapi.model.mapper.ai.AIMessageToDTOMapper;
import com.ditrit.letomodelizerapi.model.permission.ActionPermission;
import com.ditrit.letomodelizerapi.model.permission.EntityPermission;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.AISecretService;
import com.ditrit.letomodelizerapi.service.AIService;
import com.ditrit.letomodelizerapi.service.UserPermissionService;
import com.ditrit.letomodelizerapi.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

/**
 * Controller to manage ai endpoint.
 */
@Slf4j
@RestController
@RequestMapping("/ai")
@ConditionalOnProperty(name = "ai.host", havingValue = "", matchIfMissing = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AIController implements DefaultController {

    /**
     * Service to manage user.
     */
    private UserService userService;

    /**
     * Service to manage user permissions.
     */
    private UserPermissionService userPermissionService;

    /**
     * Service to manage AI request.
     */
    private AIService aiService;

    /**
     * Service to manage AI request.
     */
    private AISecretService aiSecretService;

    /**
     * Handles a POST request to generate files with an Artificial Intelligence (AI) based on the provided
     * request details.
     * This endpoint accepts an AI request record, which includes the parameters necessary for the files generation.
     * The method retrieves the user from the session, logs the request details, and forwards the request to the AI
     * service. It then constructs and returns a response containing the AI's output.
     *
     * <p>The method uses the AI service to process the request by the user, generating a JSON response that is returned
     * to the client.
     *
     * @param request the HttpServletRequest, used to access the user's session.
     * @param aiCreateFileRecord the request details for the AI, validated to ensure it meets the expected format.
     * @return a Response object containing the AI's response in JSON format, with a status of CREATED (201).
     */
    @PostMapping("/generate")
    public ResponseEntity<String> generateFiles(final HttpServletRequest request,
                                                final @RequestBody @Valid AICreateFileRecord aiCreateFileRecord) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);

        log.info("[{}] Received POST request to generate files with AI with {}",
                user.getLogin(), aiCreateFileRecord);

        String json = aiService.createFile(aiCreateFileRecord);

        return ResponseEntity
                .status(HttpStatus.CREATED.value())
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    /**
     * Handles a POST request to create a new AI conversation.
     * This endpoint allows users to initiate a conversation with AI by providing the necessary details.
     *
     * @param request the HttpServletRequest used to access the user's session.
     * @param aiConversationRecord the details of the AI conversation to be created, validated for correctness.
     * @return a Response object containing the created AI conversation DTO with a status of CREATED (201).
     * @throws JsonProcessingException if there is an error processing the request data.
     */
    @PostMapping("/conversations")
    public ResponseEntity<AIConversationDTO> createConversations(
            final HttpServletRequest request,
            final @RequestBody @Valid AIConversationRecord aiConversationRecord)
            throws JsonProcessingException {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);

        log.info("[{}] Received POST Received GET request to create AI conversation with {}",
                user.getLogin(), aiConversationRecord);

        AIConversationDTO dto = new BeanMapper<>(AIConversationDTO.class)
                .apply(aiService.createConversation(user, aiConversationRecord));

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(dto);
    }

    /**
     * Handles a GET request to retrieve all AI conversations with optional filtering and pagination.
     * This endpoint allows administrators to view all AI conversations.
     *
     * @param request     the HttpServletRequest used to access the user's session.
     * @param filters     All query parameters for filtering results.
     * @param queryFilter the filter criteria and pagination information.
     * @return a Response object containing a paginated list of AIConversationDTOs.
     */
    @GetMapping("/conversations")
    public ResponseEntity<Page<AIConversationDTO>> findAllConversations(
            final HttpServletRequest request,
            final @RequestParam MultiValueMap<String, String> filters,
            final @ModelAttribute QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkIsAdmin(user, null);

        log.info("[{}] Received GET request to get AI conversations with the following filters: {}",
                user.getLogin(), filters);

        Page<AIConversationDTO> resources = aiService.findAllConversations(filters, queryFilter)
                .map(new BeanMapper<>(AIConversationDTO.class));

        return ResponseEntity.status(this.getStatus(resources)).body(resources);
    }

    /**
     * Handles a GET request to retrieve a specific AI conversation by its ID.
     * This endpoint allows users to view details of a specific conversation.
     *
     * @param request the HttpServletRequest used to access the user's session.
     * @param id the ID of the AI conversation to retrieve.
     * @return a Response object containing the AIConversationDTO with a status of OK (200).
     */
    @GetMapping("/conversations/{id}")
    public ResponseEntity<AIConversationDTO> getConversationById(final HttpServletRequest request,
                                                                 final @PathVariable UUID id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);

        log.info("[{}] Received GET request to get AI conversation with id {}", user.getLogin(), id);

        AIConversationDTO dto = new BeanMapper<>(AIConversationDTO.class)
                .apply(aiService.getConversationById(user, id));

        return ResponseEntity.ok(dto);
    }

    /**
     * Handles a PUT request to update an existing AI conversation by its ID.
     * This endpoint allows users to modify the details of an existing conversation.
     *
     * @param request the HttpServletRequest used to access the user's session.
     * @param id the ID of the AI conversation to update.
     * @param aiConversationRecord the new details for the AI conversation, validated for correctness.
     * @return a Response object containing the updated AIConversationDTO with a status of OK (200).
     * @throws JsonProcessingException if there is an error processing the request data.
     */
    @PutMapping("/conversations/{id}")
    public ResponseEntity<AIConversationDTO> updateConversationById(
            final HttpServletRequest request,
            final @PathVariable UUID id,
            final @RequestBody @Valid AIConversationRecord aiConversationRecord)
            throws JsonProcessingException {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);

        log.info("[{}] Received PUT request to update AI conversation with id {} with {}",
                user.getLogin(), id, aiConversationRecord);

        AIConversationDTO dto = new BeanMapper<>(AIConversationDTO.class)
                .apply(aiService.updateConversationById(user, id, aiConversationRecord));

        return ResponseEntity.ok(dto);
    }

    /**
     * Handles a DELETE request to remove a specific AI conversation by its ID.
     * This endpoint allows users to delete an AI conversation.
     *
     * @param request the HttpServletRequest used to access the user's session.
     * @param id the ID of the AI conversation to delete.
     * @return a Response object with a status of NO CONTENT (204) upon successful deletion.
     */
    @DeleteMapping("/conversations/{id}")
    public ResponseEntity<Object> deleteConversationById(final HttpServletRequest request,
                                                         final @PathVariable UUID id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);

        log.info("[{}] Received GET request to get AI conversation with id {}", user.getLogin(), id);

        if (userPermissionService.hasPermission(user, EntityPermission.ADMIN, ActionPermission.ACCESS)) {
            aiService.deleteConversationById(id);
        } else {
            aiService.deleteConversationById(user, id);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).contentType(MediaType.APPLICATION_JSON).build();
    }

    /**
     * Handles a POST request to send a message to a specific AI conversation.
     * This endpoint allows users to communicate with the AI by sending a message to an exinsting conversation.
     *
     * @param request the HttpServletRequest used to access the user's session.
     * @param id the ID of the AI conversation to which the message is sent.
     * @param aiMessage the record that contains the message to send to the AI.
     * @return a Response object containing the AI's reply in plain text with a status of CREATED (201).
     * @throws JsonProcessingException if there is an error processing the request data.
     */
    @PostMapping("/conversations/{id}/messages")
    public ResponseEntity<AIMessageDTO> createConversationMessage(final HttpServletRequest request,
                                                                  final @PathVariable UUID id,
                                                                  final @RequestBody @Valid AIMessageRecord aiMessage)
            throws IOException {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);

        log.info("[{}] Received POST request to send message to conversation id {}",
                user.getLogin(), id.toString());

        AIMessageDTO aiMessageDTO = new AIMessageToDTOMapper().apply(aiService.sendMessage(user, id, aiMessage));

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(aiMessageDTO);
    }

    /**
     * Handles a GET request to retrieve all messages from a specific AI conversation with optional filtering and
     * pagination.
     * This endpoint allows users to view the conversation history with AI.
     *
     * @param request     the HttpServletRequest used to access the user's session.
     * @param id          the ID of the AI conversation from which to retrieve messages.
     * @param filters     All query parameters for filtering results.
     * @param queryFilter the filter criteria and pagination information.
     * @return a Response object containing a paginated list of AIMessageDTOs.
     */
    @GetMapping("/conversations/{id}/messages")
    public ResponseEntity<Page<AIMessageDTO>> findAllMessages(final HttpServletRequest request,
                                                              final @PathVariable UUID id,
                                                              final @RequestParam MultiValueMap<String, String> filters,
                                                              final @ModelAttribute QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);

        log.info("[{}] Received GET request to get AI messages with conversation id {} with the following filters: {}",
                user.getLogin(), id.toString(), filters);

        Page<AIMessageDTO> resources = aiService.findAllMessages(user, id, filters, queryFilter)
                .map(new AIMessageToDTOMapper());

        return ResponseEntity.status(this.getStatus(resources)).body(resources);
    }

    /**
     * Sends the generated configuration to the AI proxy.
     * <p>
     * This method handles GET requests to the endpoint {@code /proxy/configuration}.
     * It uses the {@code aiSecretService} to generate a configuration that is then sent to the AI proxy.
     *
     * @param request the {@link HttpServletRequest} containing the current HTTP request information, used to retrieve
     *                the session details.
     * @return a {@link ResponseEntity} with a 204 (No Content) status, indicating the configuration was successfully
     * sent to the AI proxy.
     */
    @GetMapping("/proxy/configuration")
    public ResponseEntity<Object> sendConfigurationToProxy(final HttpServletRequest request) {
        HttpSession session = request.getSession();
        log.info("[{}] Received GET request to send configuration to proxy",
                session.getAttribute(Constants.DEFAULT_USER_PROPERTY));

        var configuration = aiSecretService.generateConfiguration();

        aiService.sendConfiguration(configuration);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).contentType(MediaType.APPLICATION_JSON).build();
    }

    /**
     * Retrieves configuration descriptions from the AI proxy.
     * <p>
     * This method handles GET requests to the endpoint {@code /proxy/descriptions}.
     * If the user has permission, the method retrieves and returns the configuration descriptions from the AI proxy.
     *
     * @param request the {@link HttpServletRequest} containing the current HTTP request information, used to retrieve
     *                the session details and user information.
     * @return a {@link ResponseEntity} containing the configuration descriptions in the response body with a 200 (OK)
     * status. If the user lacks permission, an appropriate error response will be returned.
     */
    @GetMapping("/proxy/descriptions")
    public ResponseEntity<String> retrieveConfigurationDescriptions(final HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkPermission(user, "id", EntityPermission.AI_SECRET, ActionPermission.ACCESS);

        log.info("[{}] Received GET request to get configuration descriptions from proxy", user.getLogin());

        var descriptions = aiService.getConfigurationDescriptions();

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(descriptions);
    }
}
