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
import jakarta.validation.constraints.Min;
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
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;

import java.net.http.HttpResponse;
import java.util.HashMap;
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
     * The maximum age for caching resource library content.
     * This variable is intended to store the duration (in seconds) that resource library content should be considered
     * fresh and can be cached by clients or intermediary proxies. After this period elapses, clients are expected to
     * re-validate the cached content with the origin server to ensure it is still up-to-date. This value is typically
     * configured through application settings or environment variables to allow for easy adjustment based on the
     * application's caching strategy and performance requirements.
     */
    private String resourceCacheMaxAge;

    /**
     * Constructor for LibraryController.
     * Initializes the controller with the necessary services for managing libraries, including user management,
     * permission validation, and library operations. It also sets the configuration for the maximum age of library file
     * caching based on the application's properties. This setup ensures efficient handling of library-related
     * operations such as accessing, creating, updating, and deleting library resources, while also optimizing
     * performance through caching strategies for library files.
     *
     * @param userService the service responsible for managing user details and operations.
     * @param userPermissionService the service for checking and validating user permissions.
     * @param libraryService the service dedicated to managing library operations such as CRUD actions and queries.
     * @param resourceCacheMaxAge the configured maximum age for caching library files, specified in application
     *                            properties, indicating how long client browsers and intermediate caches should
     *                            consider library files fresh before revalidating them. This helps in reducing
     *                            unnecessary server requests and improving user experience by leveraging browser cache.
     */
    @Autowired
    public LibraryController(final UserService userService,
                             final UserPermissionService userPermissionService,
                             final LibraryService libraryService,
                             final @Value("${library.files.cache.max.age}") String resourceCacheMaxAge) {
        this.userService = userService;
        this.userPermissionService = userPermissionService;
        this.libraryService = libraryService;
        this.resourceCacheMaxAge = resourceCacheMaxAge;
    }


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
     * Handles the GET request to retrieve the icon for a library identified by its ID.
     * Validates the user's permission to access the library before attempting to retrieve the icon.
     * This method fetches the library's icon as a byte array and determines the content type from the
     * response headers. If the content type is not specified, "application/octet-stream" is used as a default.
     *
     * @param request the HttpServletRequest containing the session information to identify the user.
     * @param id the unique identifier of the library whose icon is being retrieved, validated for non-null value.
     * @return a Response object containing the icon as a byte array and the appropriate content type.
     */
    @GET
    @Path("/{id}/icon")
    public Response getLibraryIcon(final @Context HttpServletRequest request,
                                   final @PathParam("id") @Valid @NotNull Long id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkLibraryPermission(user, ActionPermission.ACCESS, id);

        log.info("Received GET request to get library icon with id {}", id);

        HttpResponse<byte[]> response = libraryService.getIcon(id);
        String contentType = response.headers()
                .firstValue(HttpHeaders.CONTENT_TYPE)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return Response.ok(response.body(), contentType).cacheControl(getCacheControl(resourceCacheMaxAge)).build();
    }

    /**
     * Handles the GET request to retrieve templates associated with a specific library identified by its ID.
     * Validates the user's permission to access the library before querying for templates. This method applies
     * additional filters provided through the URI and the QueryFilter bean to refine the search for templates.
     * It ensures that only templates associated with the specified library ID are fetched.
     *
     * @param request the HttpServletRequest containing the session information to identify the user.
     * @param uriInfo the UriInfo containing query parameters for additional filtering.
     * @param queryFilter a QueryFilter bean containing pagination settings.
     * @param id the unique identifier of the library whose templates are being retrieved, validated for non-null value.
     * @return a Response object containing a page of LibraryTemplate entities associated with the library and matching
     * the specified filters and pagination settings.
     */
    @GET
    @Path("/{id}/templates")
    public Response getLibraryTemplates(final @Context HttpServletRequest request,
                                        final @Context UriInfo uriInfo,
                                        final @BeanParam @Valid QueryFilter queryFilter,
                                        final @PathParam("id") @Valid @NotNull Long id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        userPermissionService.checkLibraryPermission(user, ActionPermission.ACCESS, id);
        Map<String, String> filters = new HashMap<>(this.getFilters(uriInfo));
        filters.put("libraryId", id.toString());

        log.info("Received GET request to get templates of library with id {} with the following filters: {}", id,
                filters);
        Page<LibraryTemplate> resources = libraryService.findAllTemplates(filters, queryFilter.getPagination());

        return Response.status(this.getStatus(resources)).entity(resources).build();
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

    /**
     * Handles the GET request to retrieve a specific library template by its ID.
     * Fetches the library template first to determine the associated library ID, then checks the user's permission
     * to access the library. If the user has the necessary permission, the method returns the requested library
     * template.
     *
     * @param request the HttpServletRequest containing the session information to identify the user.
     * @param id the unique identifier of the library template being retrieved, validated for non-null value.
     * @return a Response object containing the LibraryTemplate entity if found and accessible by the user.
     */
    @GET
    @Path("/templates/{id}")
    public Response getTemplatesById(final @Context HttpServletRequest request,
                                     final @PathParam("id") @Valid @NotNull Long id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        LibraryTemplate libraryTemplate = libraryService.getTemplateById(id);
        userPermissionService.checkLibraryPermission(user, ActionPermission.ACCESS, libraryTemplate.getLibraryId());

        log.info("Received GET request to get templates with id {}", id);

        return Response.ok(libraryTemplate).build();
    }

    /**
     * Handles the GET request to retrieve the icon for a specific library template identified by its ID.
     * Validates the user's permission to access the associated library before attempting to retrieve the template's
     * icon.
     * This method fetches the template's icon as a byte array and determines the content type from the response
     * headers.
     * If the content type is not specified, "application/octet-stream" is used as a default.
     *
     * @param request the HttpServletRequest containing the session information to identify the user.
     * @param id the unique identifier of the library template whose icon is being retrieved, validated for non-null
     *           value.
     * @return a Response object containing the icon as a byte array and the appropriate content type.
     */
    @GET
    @Path("/templates/{id}/icon")
    public Response getTemplateIcon(final @Context HttpServletRequest request,
                                    final @PathParam("id") @Valid @NotNull Long id) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        LibraryTemplate libraryTemplate = libraryService.getTemplateById(id);
        userPermissionService.checkLibraryPermission(user, ActionPermission.ACCESS, libraryTemplate.getLibraryId());

        log.info("Received GET request to get template icon with id {}", id);

        HttpResponse<byte[]> response = libraryService.getTemplateIcon(libraryTemplate);
        String contentType = response.headers()
                .firstValue(HttpHeaders.CONTENT_TYPE)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        return Response.ok(response.body(), contentType).cacheControl(getCacheControl(resourceCacheMaxAge)).build();
    }

    /**
     * Handles the GET request to retrieve a specific schema from a library template by its ID and the schema's index.
     * Validates the user's permission to access the associated library before attempting to retrieve the template's
     * schema.
     * This method fetches the specified schema as a byte array based on its index within the library template and
     * determines the content type from the response headers. If the content type is not specified,
     * "application/octet-stream" is used as a default.
     *
     * @param request the HttpServletRequest containing the session information to identify the user.
     * @param id the unique identifier of the library template from which the schema is being retrieved, validated for
     *           non-null value.
     * @param index the index of the schema within the template, validated to be non-null and minimum 0.
     * @return a Response object containing the schema as a byte array and the appropriate content type.
     */
    @GET
    @Path("/templates/{id}/schemas/{index}")
    public Response getTemplateSchema(final @Context HttpServletRequest request,
                                      final @PathParam("id") @Valid @NotNull Long id,
                                      final @PathParam("index") @Valid @NotNull @Min(0) Long index) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        LibraryTemplate libraryTemplate = libraryService.getTemplateById(id);
        userPermissionService.checkLibraryPermission(user, ActionPermission.ACCESS, libraryTemplate.getLibraryId());

        log.info("Received GET request to get template schema with id {} and index", id, index);

        HttpResponse<byte[]> response = libraryService.getTemplateSchema(libraryTemplate, index);
        String contentType = response.headers()
                .firstValue(HttpHeaders.CONTENT_TYPE)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);
        String fileName = libraryService.getFileName(true, libraryTemplate, index);

        return Response.ok(response.body(), contentType)
                .header("Content-Disposition", String.format("filename=\"%s\"", fileName))
                .cacheControl(getCacheControl(resourceCacheMaxAge))
                .build();
    }

    /**
     * Handles the GET request to retrieve a specific file from a library template by its ID and the file's index.
     * Validates the user's permission to access the associated library before attempting to retrieve the template's
     * file.
     * This method fetches the specified file as a byte array based on its index within the library template and
     * determines the content type from the response headers. If the content type is not specified,
     * "application/octet-stream" is used as a default.
     *
     * @param request the HttpServletRequest containing the session information to identify the user.
     * @param id the unique identifier of the library template from which the file is being retrieved, validated for
     *           non-null value.
     * @param index the index of the file within the template, validated to be non-null and minimum 0.
     * @return a Response object containing the file as a byte array and the appropriate content type.
     */
    @GET
    @Path("/templates/{id}/files/{index}")
    public Response getTemplateFile(final @Context HttpServletRequest request,
                                      final @PathParam("id") @Valid @NotNull Long id,
                                      final @PathParam("index") @Valid @NotNull @Min(0) Long index) {
        HttpSession session = request.getSession();
        User user = userService.getFromSession(session);
        LibraryTemplate libraryTemplate = libraryService.getTemplateById(id);
        userPermissionService.checkLibraryPermission(user, ActionPermission.ACCESS, libraryTemplate.getLibraryId());

        log.info("Received GET request to get template file with id {} and index", id, index);

        HttpResponse<byte[]> response = libraryService.getTemplateFile(libraryTemplate, index);
        String contentType = response.headers()
                .firstValue(HttpHeaders.CONTENT_TYPE)
                .orElse(MediaType.APPLICATION_OCTET_STREAM);
        String fileName = libraryService.getFileName(false, libraryTemplate, index);

        return Response.ok(response.body(), contentType)
                .header("Content-Disposition", String.format("filename=\"%s\"", fileName))
                .cacheControl(getCacheControl(resourceCacheMaxAge))
                .build();
    }
}
