package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.ai.AICreateFileRecord;
import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Service implementation for interacting with an Artificial Intelligence (AI).
 * This class provides concrete implementations of the methods defined in the IAService interface,
 * facilitating communication with an IA system for processing and responding to user requests.
 */
@Slf4j
@Service
public class AIServiceImpl implements AIService {

    /**
     * the host address of the Intelligent Assistant system, injected from application properties.
     */
    private final String aiHost;

    /**
     * Constructor for AIServiceImpl.
     * Initializes the service with the host address of the Artificial Intelligence (AI) system. This address is used to
     * configure the connection and communication with the AI, enabling the service to send requests and receive
     * responses from the AI.
     * The host address is injected from the application's configuration properties, allowing for flexible deployment
     * and configuration of the AI service endpoint.
     *
     * @param aiHost the host address of the Artificial Intelligence system, injected from application properties.
     */
    @Autowired
    public AIServiceImpl(@Value("${ai.host}") final String aiHost) {
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

    @Override
    public String createFile(final AICreateFileRecord createFileRecord) {
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("pluginName", createFileRecord.plugin());
        json.put("description", createFileRecord.description());

        return sendRequest(createFileRecord.type(), json.toString());
    }
}
