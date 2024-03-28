package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.model.ia.IARequestRecord;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
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
 * Service implementation for interacting with an Intelligent Assistant (IA).
 * This class provides concrete implementations of the methods defined in the IAService interface,
 * facilitating communication with an IA system for processing and responding to user requests.
 */
@Slf4j
@Service
public class IAServiceImpl implements IAService {

    /**
     * the host address of the Intelligent Assistant system, injected from application properties.
     */
    private String iaHost;

    /**
     * Constructor for IAServiceImpl.
     * Initializes the service with the host address of the Intelligent Assistant (IA) system. This address is used to
     * configure the connection and communication with the IA, enabling the service to send requests and receive
     * responses from the IA.
     * The host address is injected from the application's configuration properties, allowing for flexible deployment
     * and configuration of the IA service endpoint.
     *
     * @param iaHost the host address of the Intelligent Assistant system, injected from application properties.
     */
    public IAServiceImpl(@Value("${ia.host}") final String iaHost) {
        this.iaHost = iaHost;
    }

    @Override
    public String sendRequest(final User user, final IARequestRecord iaRequest) {
        String url = String.format("%s%s", iaHost, iaRequest.type());

        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("userId", user.getId().toString());
        json.put("plugin", iaRequest.plugin());
        json.put("description", iaRequest.description());

        String body = json.toString();
        log.info(body);
        log.info("passe");

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (!HttpStatus.valueOf(response.statusCode()).is2xxSuccessful()) {
                throw new ApiException(ErrorType.WRONG_VALUE, "url", url);
            }

            return response.body();
        } catch (URISyntaxException | IOException e) {
            throw new ApiException(ErrorType.WRONG_VALUE, "url", url);
        } catch (InterruptedException e) {
            log.warn("InterruptedException during requesting ia with {} and ", url, body, e);
            Thread.currentThread().interrupt();
            throw new ApiException(ErrorType.INTERNAL_ERROR, "url", url);
        }
    }
}
