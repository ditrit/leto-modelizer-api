package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.model.ia.IARequestRecord;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.IAService;
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
 * Controller to manage ia endpoint.
 */
@Path("/ia")
@Produces(MediaType.APPLICATION_JSON)
@Controller
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class IAController {

    /**
     * Service to manage user.
     */
    private UserService userService;

    /**
     * Service to manage ia request.
     */
    private IAService iaService;

    /**
     * Handles a POST request to initiate an interaction with an Intelligent Assistant (IA) based on the provided
     * request details.
     * This endpoint accepts an IA request record, which includes the parameters necessary for the IA interaction.
     * The method retrieves the user from the session, logs the request details, and forwards the request to the IA
     * service. It then constructs and returns a response containing the IA's output.
     *
     * <p>The method uses the IA service to process the request by the user, generating a JSON response that is returned
     * to the client.
     * This process allows for dynamic interactions with the IA, facilitating use cases such as querying for
     * information, executing commands, or initiating workflows within the application.
     *
     * @param request the HttpServletRequest, used to access the user's session.
     * @param iaRequestRecord the request details for the IA, validated to ensure it meets the expected format.
     * @return a Response object containing the IA's response in JSON format, with a status of OK (200).
     */

    @POST
    public Response requestIA(final @Context HttpServletRequest request,
                                final @Valid IARequestRecord iaRequestRecord) throws InterruptedException {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);

        log.info("[{}] Received POST request to request ia with {}", user.getLogin(), iaRequestRecord);

        String json = iaService.sendRequest(user, iaRequestRecord);

        return Response.status(HttpStatus.OK.value())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .entity(json)
                .build();
    }
}
