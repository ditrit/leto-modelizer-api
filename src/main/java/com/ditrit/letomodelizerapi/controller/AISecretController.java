package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.model.BeanMapper;
import com.ditrit.letomodelizerapi.model.ai.AISecretDTO;
import com.ditrit.letomodelizerapi.model.ai.AISecretRecord;
import com.ditrit.letomodelizerapi.model.permission.ActionPermission;
import com.ditrit.letomodelizerapi.model.permission.EntityPermission;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.AISecretService;
import com.ditrit.letomodelizerapi.service.UserPermissionService;
import com.ditrit.letomodelizerapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * REST Controller for managing ai and secrets.
 * Provides endpoints for CRUD operations on roles, including listing, retrieving, creating, updating, and deleting
 * roles.
 * Only accessible by users with administrative permissions.
 */
@Slf4j
@Path("/ai/secrets")
@Produces(MediaType.APPLICATION_JSON)
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AISecretController implements DefaultController {

    /**
     * Service to manage ai secret.
     */
    private final AISecretService aiSecretService;

    /**
     * Service to manage user.
     */
    private final UserService userService;
    /**
     * Service to manage user permissions.
     */
    private final UserPermissionService userPermissionService;

    /**
     * Retrieves the scopes of a specified role.
     *
     * <p>This method processes a GET request to obtain scopes associated with a given role ID. It filters the scopes
     * based on the provided query parameters and pagination settings.
     *
     * @param request     the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param uriInfo     UriInfo context to extract query parameters for filtering results.
     * @param queryFilter bean parameter encapsulating filtering and pagination criteria.
     * @return a Response object containing the requested page of AISecretDTO objects representing the
     * secrets. The status of the response can vary based on the outcome of the request.
     */
    @GET
    public Response getAllSecrets(final @Context HttpServletRequest request,
                                  final @Context UriInfo uriInfo,
                                  final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkPermission(user, "id", EntityPermission.AI_SECRET, ActionPermission.ACCESS);

        Map<String, String> filters = new HashMap<>(this.getFilters(uriInfo));

        log.info("[{}] Received GET request to get secrets with the following filters: {}", user.getLogin(), filters);

        var resources = aiSecretService.findAll(filters, queryFilter.getPagination())
                .map(new BeanMapper<>(AISecretDTO.class));

        return Response.status(this.getStatus(resources)).entity(resources).build();
    }

    /**
     * Get secret by id.
     *
     * @param request     the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id          the ID of the secret to retrieve. Must be a valid and non-null UUID value.
     * @return a Response object containing theAISecretDTO object representing the secret.
     * The status of the response can vary based on the outcome of the request.
     */
    @GET
    @Path("/{id}")
    public Response getSecretById(final @Context HttpServletRequest request,
                                  final @PathParam("id") @Valid @NotNull UUID id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkPermission(user, "id", EntityPermission.AI_SECRET, ActionPermission.ACCESS);

        log.info("[{}] Received GET request to get secret {}", user.getLogin(), id);

        var aiSecret = aiSecretService.findById(id);

        return Response.status(HttpStatus.OK.value())
                .entity(new BeanMapper<>(AISecretDTO.class).apply(aiSecret))
                .build();
    }

    /**
     * Create a secret.
     *
     * <p>This method handles a POST request to create a secret.
     * It validates the user's session and ensures the user has administrative privileges before proceeding with the
     * association.
     *
     * @param request the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param aiSecretRecord the record containing the details of the secret to be created,
     *                                  validated for correctness.
     * @return a Response object indicating the outcome of the secret creation. A successful operation returns
     * a status of CREATED.
     */
    @POST
    public Response createSecret(final @Context HttpServletRequest request,
                                 final @Valid AISecretRecord aiSecretRecord) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkPermission(user, null, EntityPermission.AI_SECRET, ActionPermission.CREATE);

        log.info("[{}] Received POST request to create secret with key {}", user.getLogin(),
                aiSecretRecord.key());
        var aiSecret = aiSecretService.create(aiSecretRecord);

        return Response.status(HttpStatus.CREATED.value())
                .entity(new BeanMapper<>(AISecretDTO.class).apply(aiSecret))
                .build();
    }

    /**
     * Update a secret.
     *
     * <p>This method handles a PUT request to update a secret.
     * It validates the user's session and ensures the user has administrative privileges before proceeding with the
     * association.
     *
     * @param request the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id      the ID of the secret . Must be a valid and non-null UUID value.
     * @param aiSecretRecord the record containing the details of the secret to be updated,
     *                                  validated for correctness.
     * @return a Response object indicating the outcome of the secret update. A successful operation returns
     * a status of OK.
     */
    @PUT
    @Path("/{id}")
    public Response updateSecret(final @Context HttpServletRequest request,
                                 final @PathParam("id") @Valid @NotNull UUID id,
                                 final @Valid AISecretRecord aiSecretRecord) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkPermission(user, "id", EntityPermission.AI_SECRET, ActionPermission.UPDATE);

        log.info("[{}] Received PUT request to update secret {}", user.getLogin(), id.toString());
        var aiSecret = aiSecretService.update(id, aiSecretRecord);

        return Response.status(HttpStatus.OK.value())
                .entity(new BeanMapper<>(AISecretDTO.class).apply(aiSecret))
                .build();
    }

    /**
     * Delete a secret.
     *
     * <p>This method facilitates the handling of a DELETE request to delete a secret identified by its respective ID.
     * The operation is secured, requiring validation of the user's session and administrative privileges.
     *
     * @param request the HttpServletRequest used to validate the user's session.
     * @param id      the ID of the secret . Must be a valid and non-null UUID value.
     * @return a Response object with a status indicating the outcome of the deletion operation. A successful operation
     * returns a status of NO_CONTENT.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteSecret(final @Context HttpServletRequest request,
                                 final @PathParam("id") @Valid @NotNull UUID id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkPermission(user, "id", EntityPermission.AI_SECRET, ActionPermission.DELETE);

        log.info("[{}] Received DELETE request to delete secret {}", user.getLogin(), id);
        aiSecretService.delete(id);

        return Response.noContent().build();
    }
}
