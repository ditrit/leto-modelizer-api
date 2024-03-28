package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.model.ai.AIRequestRecord;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.AIService;
import com.ditrit.letomodelizerapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

/**
 * Controller to manage ai endpoint.
 */
@Path("/ai")
@Produces(MediaType.APPLICATION_JSON)
@Controller
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AIController {

    /**
     * Service to manage user.
     */
    private UserService userService;

    /**
     * Service to manage ai request.
     */
    private AIService aiService;

    /**
     * Handles a POST request to initiate an interaction with an Artificial Intelligence (AI) based on the provided
     * request details.
     * This endpoint accepts an AI request record, which includes the parameters necessary for the AI interaction.
     * The method retrieves the user from the session, logs the request details, and forwards the request to the AI
     * service. It then constructs and returns a response containing the AI's output.
     *
     * <p>The method uses the AI service to process the request by the user, generating a JSON response that is returned
     * to the client.
     * This process allows for dynamic interactions with the AI, facilitating use cases such as querying for
     * information, executing commands, or initiating workflows within the application.
     *
     * @param request the HttpServletRequest, used to access the user's session.
     * @param aiRequestRecord the request details for the AI, validated to ensure it meets the expected format.
     * @return a Response object containing the AI's response in JSON format, with a status of OK (200).
     */

    @POST
    public Response requestAI(final @Context HttpServletRequest request,
                                final @Valid AIRequestRecord aiRequestRecord) throws InterruptedException {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);

        log.info("[{}] Received POST request to request AI with {}", user.getLogin(), aiRequestRecord);

        String json = aiService.sendRequest(aiRequestRecord);

        return Response.status(HttpStatus.CREATED.value())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .entity(json)
                .build();
    }
}
