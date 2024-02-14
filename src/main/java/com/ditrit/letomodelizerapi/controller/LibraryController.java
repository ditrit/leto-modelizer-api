package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.controller.model.QueryFilter;
import com.ditrit.letomodelizerapi.model.BeanMapper;
import com.ditrit.letomodelizerapi.model.library.LibraryDTO;
import com.ditrit.letomodelizerapi.model.library.LibraryRecord;
import com.ditrit.letomodelizerapi.model.permission.ActionPermission;
import com.ditrit.letomodelizerapi.model.permission.EntityPermission;
import com.ditrit.letomodelizerapi.persistence.model.LibraryTemplate;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.service.LibraryService;
import com.ditrit.letomodelizerapi.service.UserPermissionService;
import com.ditrit.letomodelizerapi.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * REST Controller for managing libraries.
 * Provides endpoints for CRUD operations on libraries, including listing, retrieving, creating, updating, and deleting
 * roles.
 * Only accessible by users with administrative permissions.
 */
@Slf4j
@Path("/libraries")
@Produces(MediaType.APPLICATION_JSON)
@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LibraryController implements DefaultController {

    /**
     * Service to manage user.
     */
    private UserService userService;

    /**
     * Service to manage user permissions.
     */
    private UserPermissionService userPermissionService;

    /**
     * Service to manage library.
     */
    private LibraryService libraryService;

    /**
     * Handles the GET request to retrieve all libraries applying filters and pagination.
     * Access is controlled based on the user's permissions. Users with access permission can view all libraries,
     * while others can only view libraries based on specific criteria.
     *
     * @param request the HttpServletRequest containing the session information to identify the user.
     * @param uriInfo the UriInfo to extract query parameters for filters.
     * @param queryFilter the query parameters encapsulated in a QueryFilter object for pagination and filtering.
     * @return a Response containing the list of libraries with status code and pagination details.
     */
    @GET
    public Response findAll(final @Context HttpServletRequest request,
                            final @Context UriInfo uriInfo,
                            final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        Map<String, String> filters = this.getFilters(uriInfo);

        log.info("Received GET request to get libraries with the following filters: {}", filters);
        Page<LibraryDTO> resources;

        if (userPermissionService.hasPermission(user, EntityPermission.LIBRARY, ActionPermission.ACCESS)) {
            resources = libraryService.findAll(filters, queryFilter.getPagination())
                    .map(new BeanMapper<>(LibraryDTO.class));
        } else {
            resources = libraryService.findAll(user, filters, queryFilter.getPagination())
                    .map(new BeanMapper<>(LibraryDTO.class));
        }

        return Response.status(this.getStatus(resources)).entity(resources).build();
    }

    /**
     * Handles the GET request to retrieve a single library by its ID.
     * Validates user's permission to access the specific library before providing the information.
     * If the user has the necessary access permission, the library details are returned.
     *
     * @param request the HttpServletRequest containing the session information to identify the user.
     * @param id the unique identifier of the library to be retrieved, validated for non-null value.
     * @return a Response object containing the library details if accessible, with an OK status.
     */
    @GET
    @Path("/{id}")
    public Response getLibraryById(final @Context HttpServletRequest request,
                                   final @PathParam("id") @Valid @NotNull Long id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkLibraryPermission(user, ActionPermission.ACCESS, id);

        log.info("Received GET request to get library with id {}", id);
        LibraryDTO library = new BeanMapper<>(LibraryDTO.class).apply(libraryService.findById(id));

        return Response.ok(library).build();
    }

    /**
     * Handles the POST request to create a new library.
     * Validates the user's permission to create libraries before proceeding with the creation.
     * Accepts a LibraryRecord object as input, containing the details of the library to be created.
     * If the user has the necessary create permission, the library is created and a response with the created
     * library's details is returned.
     *
     * @param request the HttpServletRequest containing the session information to identify the user.
     * @param libraryRecord the record containing the details of the library to be created, validated for correctness.
     * @return a Response object with the status set to CREATED and the newly created library as the entity.
     * @throws JsonProcessingException if there is an error processing the input library record.
     */
    @POST
    public Response createLibrary(final @Context HttpServletRequest request,
                                  final @Valid LibraryRecord libraryRecord) throws JsonProcessingException {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkLibraryPermission(user, ActionPermission.CREATE, null);

        log.info("Received POST request to create a library with {}", libraryRecord);
        LibraryDTO library = new BeanMapper<>(LibraryDTO.class)
                .apply(libraryService.create(libraryRecord, user.getLogin()));

        return Response.status(HttpStatus.CREATED.value()).entity(library).build();
    }

    /**
     * Handles the PUT request to update the URL of an existing library identified by its ID.
     * Validates the user's permission to update the library before proceeding with the update operation.
     * The URL must match a specific pattern, indicating it should end with "/index.json".
     *
     * @param request the HttpServletRequest containing the session information to identify the user.
     * @param id the unique identifier of the library to be updated, validated for non-null value.
     * @param url the new URL for the library, validated against a pattern to ensure it ends with "/index.json".
     * @return a Response object with a status indicating that no content exists (successful update) after the
     * operation.
     * @throws JsonProcessingException if there is an error processing the new URL for the library.
     */
    @PUT
    @Path("/{id}")
    public Response updateLibrary(final @Context HttpServletRequest request,
                                  final @PathParam("id") @Valid @NotNull Long id,
                                  final @Valid @Pattern(regexp = ".+/index\\.json$") String url)
            throws JsonProcessingException {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkLibraryPermission(user, ActionPermission.UPDATE, id);

        log.info("Received PUT request to update a library {} with {}", id, url);
        libraryService.update(id, url);

        return Response.noContent().build();
    }

    /**
     * Handles the DELETE request to remove a library identified by its ID.
     * Checks the user's permission to delete the specified library before proceeding with the deletion.
     * If the user has the necessary delete permission, the library is removed from the system.
     *
     * @param request the HttpServletRequest containing the session information to identify the user.
     * @param id the unique identifier of the library to be deleted, validated for non-null value.
     * @return a Response object with a status indicating that no content exists (successful deletion) after the
     * operation.
     */
    @DELETE
    @Path("/{id}")
    public Response deleteLibrary(final @Context HttpServletRequest request,
                                  final @PathParam("id") @Valid @NotNull Long id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkLibraryPermission(user, ActionPermission.DELETE, id);

        log.info("Received DELETE request to delete library with id {}", id);
        libraryService.delete(id);

        return Response.noContent().build();
    }


    /**
     * Handles the GET request to retrieve all library templates applying filters and pagination.
     * Access is controlled based on the user's permissions. Users with access permission can view all library
     * templates, while others can only view library templates based on specific criteria.
     *
     * @param request the HttpServletRequest containing the session information to identify the user.
     * @param uriInfo the UriInfo to extract query parameters for filters.
     * @param queryFilter the query parameters encapsulated in a QueryFilter object for pagination and filtering.
     * @return a Response containing the list of library templates with status code and pagination details.
     */
    @GET
    @Path("/templates")
    public Response findAllTemplates(final @Context HttpServletRequest request,
                            final @Context UriInfo uriInfo,
                            final @BeanParam @Valid QueryFilter queryFilter) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        Map<String, String> filters = this.getFilters(uriInfo);

        log.info("Received GET request to get library templates with the following filters: {}", filters);
        Page<LibraryTemplate> resources;

        if (userPermissionService.hasPermission(user, EntityPermission.LIBRARY, ActionPermission.ACCESS)) {
            resources = libraryService.findAllTemplates(filters, queryFilter.getPagination());
        } else {
            resources = libraryService.findAllTemplates(user, filters, queryFilter.getPagination());
        }

        return Response.status(this.getStatus(resources)).entity(resources).build();
    }
}
