package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.ai.AIRequestRecord;

/**
 * Service implementation for interacting with an Artificial Intelligence (AI).
 * This class provides concrete implementation for sending requests to an AI, based on user input and specific
 * request details encapsulated in an AIRequestRecord.
 */
public interface AIService {

    /**
     * Sends a request to the Artificial Intelligence (AI) based on the provided user and request record.
     * This method constructs the request, sends it to the AI system, and returns the AI response as a String.
     *
     * @param aiRequest the AIRequestRecord containing details about the request to be sent to the AI.
     * @return the response from the AI as a String.
     */
    String sendRequest(AIRequestRecord aiRequest);
}
