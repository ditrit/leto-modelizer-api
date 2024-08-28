package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.model.ai.AIRequestRecord;
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

    @Override
    public String sendRequest(final AIRequestRecord aiRequest) {
        String url = String.format("%s%s/", aiHost, aiRequest.type());

        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("pluginName", aiRequest.plugin());
        json.put("description", aiRequest.description());

        String body = json.toString();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
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
                throw new ApiException(ErrorType.WRONG_VALUE, "url", url);
            }

            return response.body();
        } catch (URISyntaxException | IOException e) {
            throw new ApiException(ErrorType.WRONG_VALUE, "url", url);
        } catch (InterruptedException e) {
            log.warn("InterruptedException during requesting ai with {} and ", url, body, e);
            Thread.currentThread().interrupt();
            throw new ApiException(ErrorType.INTERNAL_ERROR, "url", url);
        }
    }
}
