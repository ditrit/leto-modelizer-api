package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.ai.AIConversationRecord;
import com.ditrit.letomodelizerapi.model.ai.AICreateFileRecord;
import com.ditrit.letomodelizerapi.persistence.model.AIConversation;
import com.ditrit.letomodelizerapi.persistence.model.AIMessage;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * Service implementation for interacting with an Artificial Intelligence (AI).
 * This class provides concrete implementation for sending requests to an AI and based on user input.
 */
public interface AIService {

    /**
     * Sends a request to the Artificial Intelligence (AI) based on the provided user and request record.
     * This method constructs the request, sends it to the AI system, and returns the AI response as a String.
     *
     * @param aiRequest the AIRequestRecord containing details about the request to be sent to the AI.
     * @return the response from the AI as a String.
     */
    String createFile(AICreateFileRecord aiRequest);

    /**
     * Creates a new AI conversation based on the provided user and conversation details.
     *
     * @param user the user initiating the conversation.
     * @param aiConversationRecord an instance of AIConversationRecord containing the details of the conversation.
     * @return the newly created AIConversation.
     * @throws JsonProcessingException if there is an error processing the JSON data.
     */
    AIConversation createConversation(User user, AIConversationRecord aiConversationRecord)
            throws JsonProcessingException;

    /**
     * Retrieves an AI conversation by its ID for the specified user.
     *
     * @param user the user requesting the conversation.
     * @param id the ID of the conversation to retrieve.
     * @return the AIConversation with the specified ID.
     */
    AIConversation getConversationById(User user, UUID id);

    /**
     * Updates an existing AI conversation by its ID for the specified user.
     *
     * @param user the user requesting the update.
     * @param id the ID of the conversation to update.
     * @param aiConversationRecord an instance of AIConversationRecord containing the updated conversation details.
     * @return the updated AIConversation.
     * @throws JsonProcessingException if there is an error processing the JSON data.
     */
    AIConversation updateConversationById(User user, UUID id, AIConversationRecord aiConversationRecord)
            throws JsonProcessingException;

    /**
     * Deletes an AI conversation by its ID.
     *
     * @param id the ID of the conversation to delete.
     */
    void deleteConversationById(UUID id);

    /**
     * Deletes an AI conversation by its ID for the specified user.
     *
     * @param user the user requesting the deletion.
     * @param id the ID of the conversation to delete.
     */
    void deleteConversationById(User user, UUID id);

    /**
     * Sends a message to an AI conversation.
     *
     * @param user the user sending the message.
     * @param id the ID of the conversation to which the message is sent.
     * @param message the content of the message to be sent.
     * @return the AI's response to the message.
     * @throws JsonProcessingException if there is an error processing the JSON data.
     */
    AIMessage sendMessage(User user, UUID id, String message) throws IOException;

    /**
     * Finds all AI conversations for a user with optional filtering and pagination.
     *
     * @param user the user for whom to find conversations.
     * @param filters a map of filters to apply to the search.
     * @param pageable pagination information.
     * @return a Page containing the AI conversations matching the filters.
     */
    Page<AIConversation> findAll(User user, Map<String, String> filters, Pageable pageable);

    /**
     * Finds all messages for a specific AI conversation with optional filtering and pagination.
     *
     * @param user the user requesting the messages.
     * @param id the ID of the conversation for which to find messages.
     * @param filters a map of filters to apply to the search.
     * @param pageable pagination information.
     * @return a Page containing the messages for the specified conversation.
     */
    Page<AIMessage> findAllMessages(User user, UUID id, Map<String, String> filters, Pageable pageable);

    /**
     * Finds all AI conversations with optional filtering and pagination.
     *
     * @param immutableFilters a map of immutable filters to apply to the search.
     * @param pageable pagination information.
     * @return a Page containing all AI conversations matching the filters.
     */
    Page<AIConversation> findAllConversations(Map<String, String> immutableFilters,
                                              Pageable pageable);

}
