package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.model.ai.AIConversationRecord;
import com.ditrit.letomodelizerapi.model.ai.AICreateFileRecord;
import com.ditrit.letomodelizerapi.model.ai.AIMessageRecord;
import com.ditrit.letomodelizerapi.persistence.model.AIConversation;
import com.ditrit.letomodelizerapi.persistence.model.AIMessage;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;
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
     * @param aiMessage the record that contains the message to be sent.
     * @return the AI's response to the message.
     * @throws JsonProcessingException if there is an error processing the JSON data.
     */
    AIMessage sendMessage(User user, UUID id, AIMessageRecord aiMessage) throws IOException;

    /**
     * Finds all AI conversations for a user with optional filtering and pagination.
     *
     * @param user the user for whom to find conversations.
     * @param filters a Map of strings representing the filtering criteria.
     * @param queryFilter a Pageable object for pagination information.
     * @return a Page containing the AI conversations matching the filters.
     */
    Page<AIConversation> findAll(User user, Map<String, List<String>> filters, QueryFilter queryFilter);

    /**
     * Finds all messages for a specific AI conversation with optional filtering and pagination.
     *
     * @param user the user requesting the messages.
     * @param id the ID of the conversation for which to find messages.
     * @param filters a Map of strings representing the filtering criteria.
     * @param queryFilter a Pageable object for pagination information.
     * @return a Page containing the messages for the specified conversation.
     */
    Page<AIMessage> findAllMessages(User user, UUID id, Map<String, List<String>> filters, QueryFilter queryFilter);

    /**
     * Finds all AI conversations with optional filtering and pagination.
     *
     * @param filters  a Map of strings representing the filtering criteria.
     * @param queryFilter a Pageable object for pagination information.
     * @return a Page containing all AI conversations matching the filters.
     */
    Page<AIConversation> findAllConversations(Map<String, List<String>> filters, QueryFilter queryFilter);

    /**
     * Sends the encrypted configuration to the AI proxy.
     * <p>
     * This method accepts a byte array representing the encrypted configuration and sends it to the AI proxy for
     * further processing or application. The configuration is assumed to have been generated and encrypted by the
     * caller before being passed to this method.
     *
     * @param configuration a byte array containing the encrypted configuration data to be sent to the AI proxy.
     */
    void sendConfiguration(byte[] configuration);

    /**
     * Retrieves the descriptions of the configurations from the AI proxy.
     * <p>
     * This method communicates with the AI proxy to retrieve a list or summary of configuration descriptions that are
     * currently available. The descriptions provide insight into the configurations being used or processed by the
     * proxy. The result is returned as a string, typically in a JSON or plain text format.
     *
     * @return a string representing the configuration descriptions retrieved from the AI proxy.
     */
    String getConfigurationDescriptions();
}
