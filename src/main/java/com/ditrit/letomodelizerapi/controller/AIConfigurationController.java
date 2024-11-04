package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.model.BeanMapper;
import com.ditrit.letomodelizerapi.model.ai.AIConfigurationDTO;
import com.ditrit.letomodelizerapi.model.ai.AIConfigurationRecord;
import com.ditrit.letomodelizerapi.model.ai.UpdateMultipleAIConfigurationRecord;
import com.ditrit.letomodelizerapi.model.permission.ActionPermission;
import com.ditrit.letomodelizerapi.model.permission.EntityPermission;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.AIConfigurationService;
import com.ditrit.letomodelizerapi.service.AISecretService;
import com.ditrit.letomodelizerapi.service.AIService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST Controller for managing ai and configurations.
 * Provides endpoints for CRUD operations on roles, including listing, retrieving, creating, updating, and deleting
 * roles.
 * Only accessible by users with administrative permissions.
 */
@Slf4j
@Path("/ai/configurations")
@Produces(MediaType.APPLICATION_JSON)
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AIConfigurationController implements DefaultController {

    /**
     * Service to manage AI configuration.
     */
    private final AIConfigurationService aiConfigurationService;

    /**
     * Service to manage AI secrets.
     */
    private final AISecretService aiSecretService;

    /**
     * Service to manage AI configuration.
     */
    private final AIService aiService;

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
     * @return a Response object containing the requested page of AIConfigurationDTO objects representing the
     * configurations. The status of the response can vary based on the outcome of the request.
     */
    @GET
    public Response getAllConfigurations(final @Context HttpServletRequest request,
                                  final @Context UriInfo uriInfo,
                                  final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkPermission(user, "id", EntityPermission.AI_CONFIGURATION, ActionPermission.ACCESS);

        Map<String, String> filters = new HashMap<>(this.getFilters(uriInfo));

        log.info("[{}] Received GET request to get configurations with the following filters: {}", user.getLogin(),
                filters);

        var resources = aiConfigurationService.findAll(filters, queryFilter.getPagination())
                .map(new BeanMapper<>(AIConfigurationDTO.class));

        return Response.status(this.getStatus(resources)).entity(resources).build();
    }

    /**
     * Get configuration by id.
     *
     * @param request     the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id          the ID of the configuration to retrieve. Must be a valid and non-null UUID value.
     * @return a Response object containing theAIConfigurationDTO object representing the configuration.
     * The status of the response can vary based on the outcome of the request.
     */
    @GET
    @Path("/{id}")
    public Response getConfigurationById(final @Context HttpServletRequest request,
                                  final @PathParam("id") @Valid @NotNull UUID id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkPermission(user, "id", EntityPermission.AI_CONFIGURATION, ActionPermission.ACCESS);

        log.info("[{}] Received GET request to get configuration {}", user.getLogin(), id);

        var aiConfiguration = aiConfigurationService.findById(id);

        return Response.status(HttpStatus.OK.value())
                .entity(new BeanMapper<>(AIConfigurationDTO.class).apply(aiConfiguration))
                .build();
    }

    /**
     * Create a configuration.
     *
     * <p>This method handles a POST request to create a configuration.
     * It validates the user's session and ensures the user has administrative privileges before proceeding with the
     * association.
     *
     * @param request the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param aiConfigurationRecord the record containing the details of the configuration to be created,
     *                                  validated for correctness.
     * @return a Response object indicating the outcome of the configuration creation. A successful operation returns
     * a status of CREATED.
     */
    @POST
    public Response createConfiguration(final @Context HttpServletRequest request,
                                 final @Valid AIConfigurationRecord aiConfigurationRecord) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkPermission(user, null, EntityPermission.AI_CONFIGURATION, ActionPermission.CREATE);

        log.info("[{}] Received POST request to create configuration with key {}", user.getLogin(),
                aiConfigurationRecord.key());
        var aiConfiguration = aiConfigurationService.create(aiConfigurationRecord);

        var configuration = aiSecretService.generateConfiguration();

        aiService.sendConfiguration(configuration);

        return Response.status(HttpStatus.CREATED.value())
                .entity(new BeanMapper<>(AIConfigurationDTO.class).apply(aiConfiguration))
                .build();
    }

    /**
     * Update multiple configurations.
     *
     * <p>This method handles a PUT request to update multiple configurations.
     * It validates the user's session and ensures the user has administrative privileges before proceeding with the
     * association.
     *
     * @param request the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param aiConfigurationRecords the record containing list of configurations to be updated,
     *                               validated for correctness.
     * @return a Response object indicating the outcome of configurations update. A successful operation returns
     * a status of OK.
     */
    @PUT
    public Response updateConfiguration(final @Context HttpServletRequest request,
                                        final @Valid List<UpdateMultipleAIConfigurationRecord> aiConfigurationRecords) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkPermission(user, "id", EntityPermission.AI_CONFIGURATION, ActionPermission.UPDATE);

        log.info("[{}] Received PUT request to update configurations {}", user.getLogin(),
                aiConfigurationRecords.stream()
                        .map(UpdateMultipleAIConfigurationRecord::id)
                        .map(UUID::toString)
                        .collect(Collectors.joining(",")));

        List<AIConfigurationDTO> configurations = new ArrayList<>();

        aiConfigurationRecords.forEach(aiConfigurationRecord -> {

            var aiConfiguration = aiConfigurationService.update(aiConfigurationRecord.id(), new AIConfigurationRecord(
                    aiConfigurationRecord.handler(),
                    aiConfigurationRecord.key(),
                    aiConfigurationRecord.value()
                    ));

            configurations.add(new BeanMapper<>(AIConfigurationDTO.class).apply(aiConfiguration));
        });

        var configuration = aiSecretService.generateConfiguration();

        aiService.sendConfiguration(configuration);

        return Response.status(HttpStatus.OK.value())
                .entity(configurations)
                .build();
    }

    /**
     * Update a configuration.
     *
     * <p>This method handles a PUT request to update a configuration.
     * It validates the user's session and ensures the user has administrative privileges before proceeding with the
     * association.
     *
     * @param request the HttpServletRequest from which to obtain the HttpSession for user validation.
     * @param id      the ID of the configuration . Must be a valid and non-null UUID value.
     * @param aiConfigurationRecord the record containing the details of the configuration to be updated,
     *                                  validated for correctness.
     * @return a Response object indicating the outcome of the configuration update. A successful operation returns
     * a status of OK.
     */
    @PUT
    @Path("/{id}")
    public Response updateConfiguration(final @Context HttpServletRequest request,
                                 final @PathParam("id") @Valid @NotNull UUID id,
                                 final @Valid AIConfigurationRecord aiConfigurationRecord) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkPermission(user, "id", EntityPermission.AI_CONFIGURATION, ActionPermission.UPDATE);

        log.info("[{}] Received PUT request to update configuration {}", user.getLogin(), id.toString());

        var aiConfiguration = aiConfigurationService.update(id, aiConfigurationRecord);
        var configuration = aiSecretService.generateConfiguration();

        aiService.sendConfiguration(configuration);

        return Response.status(HttpStatus.OK.value())
                .entity(new BeanMapper<>(AIConfigurationDTO.class).apply(aiConfiguration))
                .build();
    }

    /**
     * Delete a configuration.
     *
     * <p>This method facilitates the handling of a DELETE request to delete a configuration identified by its
     * respective ID.
     * The operation is secured, requiring validation of the user's session and administrative privileges.
     *
     * @param request the HttpServletRequest used to validate the user's session.
     * @param id      the ID of the configuration . Must be a valid and non-null UUID value.
     * @return a Response object with a status indicating the outcome of the deletion operation. A successful operation
     * returns a status of NO_CONTENT.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteConfiguration(final @Context HttpServletRequest request,
                                 final @PathParam("id") @Valid @NotNull UUID id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkPermission(user, "id", EntityPermission.AI_CONFIGURATION, ActionPermission.DELETE);

        log.info("[{}] Received DELETE request to delete configuration {}", user.getLogin(), id);
        var aiConfiguration = aiConfigurationService.findById(id);

        aiConfiguration.setValue(null);

        var configuration = aiSecretService.generateConfiguration();

        aiService.sendConfiguration(configuration);

        aiConfigurationService.delete(id);

        return Response.noContent().build();
    }
}
