package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.ia.IARequestRecord;
import com.ditrit.letomodelizerapi.persistence.model.User;

/**
 * Service implementation for interacting with an Intelligent Assistant (IA).
 * This class provides concrete implementation for sending requests to an IA, based on user input and specific
 * request details encapsulated in an IARequestRecord.
 */
public interface IAService {

    /**
     * Sends a request to the Intelligent Assistant (IA) based on the provided user and request record.
     * This method constructs the request, sends it to the IA system, and returns the IA's response as a String.
     *
     * @param user the user making the request, used for context or authentication purposes if needed.
     * @param iaRequest the IARequestRecord containing details about the request to be sent to the IA.
     * @return the response from the IA as a String.
     */
    String sendRequest(User user, IARequestRecord iaRequest);
}
