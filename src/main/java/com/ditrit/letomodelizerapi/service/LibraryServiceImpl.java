package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlRecord;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.model.library.LibraryRecord;
import com.ditrit.letomodelizerapi.persistence.function.JsonNodeToLibraryFunction;
import com.ditrit.letomodelizerapi.persistence.function.JsonNodeToLibraryTemplateFunction;
import com.ditrit.letomodelizerapi.persistence.function.UserLibraryTemplateViewToLibraryTemplateFunction;
import com.ditrit.letomodelizerapi.persistence.function.UserLibraryViewToLibraryFunction;
import com.ditrit.letomodelizerapi.persistence.model.AccessControl;
import com.ditrit.letomodelizerapi.persistence.model.Library;
import com.ditrit.letomodelizerapi.persistence.model.LibraryTemplate;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.persistence.model.UserLibraryTemplateView;
import com.ditrit.letomodelizerapi.persistence.model.UserLibraryView;
import com.ditrit.letomodelizerapi.persistence.repository.LibraryRepository;
import com.ditrit.letomodelizerapi.persistence.repository.LibraryTemplateRepository;
import com.ditrit.letomodelizerapi.persistence.repository.UserLibraryTemplateViewRepository;
import com.ditrit.letomodelizerapi.persistence.repository.UserLibraryViewRepository;
import com.ditrit.letomodelizerapi.persistence.specification.SpecificationHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.erosb.jsonsKema.FormatValidationPolicy;
import com.github.erosb.jsonsKema.JsonParser;
import com.github.erosb.jsonsKema.JsonValue;
import com.github.erosb.jsonsKema.Schema;
import com.github.erosb.jsonsKema.SchemaLoader;
import com.github.erosb.jsonsKema.ValidationFailure;
import com.github.erosb.jsonsKema.Validator;
import com.github.erosb.jsonsKema.ValidatorConfig;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the PermissionService interface.
 *
 * <p>This class provides concrete implementations for the user management operations defined in PermissionService.
 * PermissionServiceImpl interacts with the underlying repository layer to perform these operations,
 * ensuring that business logic and data access are effectively managed.
 */
@Slf4j
@Service
@Transactional
public class LibraryServiceImpl implements LibraryService {

    /**
     * The LibraryRepository instance is injected by Spring's dependency injection mechanism.
     * This repository is used for performing database operations related to Library entities, such as querying,
     * saving and updating user data.
     */
    private LibraryRepository libraryRepository;

    /**
     * The LibraryTemplateRepository instance is injected by Spring's dependency injection mechanism.
     * This repository is used for performing database operations related to LibraryTemplate entities, such as querying,
     * saving and updating user data.
     */
    private LibraryTemplateRepository libraryTemplateRepository;

    /**
     * The UserLibraryViewRepository instance is injected by Spring's dependency injection mechanism.
     * This repository is used for performing database operations related to UserLibraryView entities, such as querying,
     * saving and updating user data.
     */
    private UserLibraryViewRepository userLibraryViewRepository;

    /**
     * The UserLibraryTemplateViewRepository instance is injected by Spring's dependency injection mechanism.
     * This repository is used for performing database operations related to UserLibraryTemplateView entities, such as
     * querying, saving and updating user data.
     */
    private UserLibraryTemplateViewRepository userLibraryTemplateViewRepository;

    /**
     * Service to manage access control.
     */
    private AccessControlService accessControlService;

    /**
     * Service to manage permission.
     */
    private PermissionService permissionService;

    /**
     * A list of hostnames that are whitelisted for library URLs.
     * This is used to validate the URLs of libraries against a predefined list of acceptable hosts,
     * ensuring that libraries are loaded from trusted sources.
     */
    private List<String> libraryWhitelistHosts;

    /**
     * A Validator instance used for validating library entities against a JSON schema.
     * This validator ensures that library data conforms to a specified schema, providing a way
     * to enforce data integrity and structure for library entities.
     */
    private Validator librarySchemaValidator;

    /**
     * Constructor for LibraryServiceImpl.
     * Initializes the service with required repositories and services for library and library template management,
     * permission checking, and access control. It also configures a list of whitelisted hosts for libraries based on
     * a comma-separated string input. Additionally, it calls loadSchemaValidator to set up JSON schema validation
     * for library entities.
     *
     * @param libraryRepository the repository for accessing and manipulating library entities
     * @param libraryTemplateRepository the repository for accessing and manipulating library template entities
     * @param accessControlService the service for managing access control and permissions
     * @param permissionService the service for checking permissions
     * @param userLibraryViewRepository the repository for accessing and manipulating library view entities
     * @param userLibraryTemplateViewRepository the repository for accessing and manipulating library template view
     *                                          entities
     * @param libraryWhitelistHosts a comma-separated string of hosts that are whitelisted for library URLs
     */
    @Autowired
    public LibraryServiceImpl(final LibraryRepository libraryRepository,
                              final LibraryTemplateRepository libraryTemplateRepository,
                              final AccessControlService accessControlService,
                              final PermissionService permissionService,
                              final UserLibraryViewRepository userLibraryViewRepository,
                              final UserLibraryTemplateViewRepository userLibraryTemplateViewRepository,
                              @Value("${library.host.whitelist}") final String libraryWhitelistHosts) {
        this.libraryRepository = libraryRepository;
        this.libraryTemplateRepository = libraryTemplateRepository;
        this.accessControlService = accessControlService;
        this.permissionService = permissionService;
        this.userLibraryViewRepository = userLibraryViewRepository;
        this.userLibraryTemplateViewRepository = userLibraryTemplateViewRepository;
        this.libraryWhitelistHosts = Arrays.stream(libraryWhitelistHosts.split(",")).map(String::trim).toList();

        loadSchemaValidator();
    }

    /**
     * Loads the JSON schema validator for library entities from a JSON file.
     * This method reads the library schema definition from a file, parses it to a JsonValue,
     * then constructs and configures a Schema instance for validation. It sets the librarySchemaValidator
     * attribute of the class for future validation operations.
     *
     * Throws ApiException with an appropriate error message and type if there is an issue loading or parsing
     * the schema file, such as an IOException.
     */
    public void loadSchemaValidator() {
        InputStream inputStream = getClass().getResourceAsStream("/library-schema.json");

        try {
            JsonValue json = new JsonParser(new String(inputStream.readAllBytes(), StandardCharsets.UTF_8)).parse();
            Schema schema = new SchemaLoader(json).load();
            this.librarySchemaValidator = Validator.create(schema, new ValidatorConfig(FormatValidationPolicy.ALWAYS));
        } catch (IOException e) {
            log.error("Error when retrieving library-schema.json", e);
            throw new ApiException(e, ErrorType.INTERNAL_ERROR, "library-schema.json", "Error when retrieving.");
        }
    }

    @Override
    public Library create(final LibraryRecord libraryRecord, final String login) throws JsonProcessingException {
        if (libraryWhitelistHosts.stream().noneMatch(host -> libraryRecord.url().startsWith(host))) {
            throw new ApiException(ErrorType.UNAUTHORIZED_LIBRARY_URL, "url", libraryRecord.url());
        }

        AccessControl role = null;
        if (StringUtils.isNotBlank(libraryRecord.role())) {
            role = accessControlService.create(AccessControlType.ROLE, new AccessControlRecord(libraryRecord.role()));
            accessControlService.associateUser(AccessControlType.ROLE, role.getId(), login);
        }

        String libraryUrl = libraryRecord.url().replace("index.json", "");

        if (libraryRepository.existsByUrl(libraryUrl)) {
            throw new ApiException(ErrorType.ENTITY_ALREADY_EXISTS, "url", libraryUrl);
        }

        Library library = save(libraryUrl, null);

        permissionService.createLibraryPermissions(library, role);

        return library;
    }

    @Override
    public void update(final Long id, final String url) throws JsonProcessingException {
        findById(id);
        if (libraryWhitelistHosts.stream().noneMatch(url::startsWith)) {
            throw new ApiException(ErrorType.UNAUTHORIZED_LIBRARY_URL, "url", url);
        }

        String libraryUrl = url.replace("index.json", "");

        if (libraryRepository.existsByUrlAndIdIsNot(libraryUrl, id)) {
            throw new ApiException(ErrorType.ENTITY_ALREADY_EXISTS, "url", libraryUrl);
        }

        libraryTemplateRepository.deleteByLibraryId(id);
        save(libraryUrl, id);
    }

    /**
     * Saves a library entity with the provided URL, potentially updating an existing library if an ID is provided.
     * This method downloads the library data from the specified URL, validates the JSON structure, and then
     * creates or updates a library entity based on the JSON data. It also processes any associated library templates
     * included in the library's JSON data, saving them to the repository.
     *
     * @param url the URL from which to download the library data, appended with "index.json" to target the specific
     *            JSON file
     * @param id the optional ID of the library to update; if null, a new library entity is created
     * @return the saved or updated Library entity
     * @throws JsonProcessingException if there is an error reading the JSON data into a JsonNode
     * Note: JsonProcessingException is mentioned in the method signature for clarity, but as noted, it cannot actually
     * be thrown due to prior validation of the JSON data.
     */
    Library save(final String url, final Long id) throws JsonProcessingException {
        String libraryValue = downloadLibrary(String.format("%sindex.json", url));
        validate(libraryValue);

        // JsonProcessingException can't be thrown because we already validate json before.
        JsonNode libraryJson = new ObjectMapper().readTree(libraryValue);
        Library library = new JsonNodeToLibraryFunction().apply(libraryJson);
        library.setUrl(url);

        if (id != null) {
            library.setId(id);
        }

        library = libraryRepository.save(library);
        final Long libraryId = library.getId();

        List<LibraryTemplate> templates = new ArrayList<>();
        libraryJson.get("templates").forEach(templateJson -> {
            LibraryTemplate template = new JsonNodeToLibraryTemplateFunction().apply(templateJson);
            template.setLibraryId(libraryId);

            templates.add(template);
        });

        libraryTemplateRepository.saveAll(templates);

        return library;
    }

    @Override
    public void validate(final String json) {
        ValidationFailure failure = librarySchemaValidator.validate(new JsonParser(json).parse());

        if (failure != null) {
            throw new ApiException(ErrorType.WRONG_LIBRARY_VALUE, failure.getKeyword().name(), failure.getMessage());
        }
    }

    /**
     * Downloads the content of a library from the specified URL.
     * This method sends an HTTP GET request to the provided URL and returns the response body as a string.
     * If the URL is not found (404 status code), it throws an ApiException indicating a wrong value error for the URL.
     * Any issues with the URI syntax or interruptions during the request handling result in throwing an ApiException
     * with an appropriate error type and message.
     *
     * @param url the URL from which to download the library content
     * @return the content of the library as a string
     * @throws ApiException if the URL is not found, if there's a problem with the URL's syntax,
     * or if an IO or interruption error occurs during the request
     */
    public String downloadLibrary(final String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient
                    .newBuilder()
                    .build()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == HttpStatus.NOT_FOUND.value()) {
                throw new ApiException(ErrorType.WRONG_VALUE, "url", url);
            }

            return response.body();
        } catch (URISyntaxException | IOException e) {
            throw new ApiException(ErrorType.WRONG_VALUE, "url", url);
        } catch (InterruptedException e) {
            log.warn("InterruptedException during library download with {}", url, e);
            Thread.currentThread().interrupt();
            throw new ApiException(ErrorType.INTERNAL_ERROR, "url", url);
        }
    }

    @Override
    public Library findById(final Long id) {
        return libraryRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorType.ENTITY_NOT_FOUND, "id", id.toString()));
    }

    @Override
    public void delete(final Long id) {
        Library library = this.findById(id);

        libraryRepository.delete(library);
    }

    @Override
    public Page<Library> findAll(final Map<String, String> filters, final Pageable pageable) {
        return libraryRepository.findAll(
                new SpecificationHelper<>(Library.class, filters),
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize())
        );
    }

    @Override
    public Page<Library> findAll(final User user, final Map<String, String> filters, final Pageable pageable) {
        return userLibraryViewRepository.findAllByUserId(
                user.getId(),
                new SpecificationHelper<>(UserLibraryView.class, filters),
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize())
        ).map(new UserLibraryViewToLibraryFunction());
    }

    @Override
    public Page<LibraryTemplate> findAllTemplates(final Map<String, String> filters, final Pageable pageable) {
        return libraryTemplateRepository.findAll(
                new SpecificationHelper<>(LibraryTemplate.class, filters),
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize())
        );
    }

    @Override
    public Page<LibraryTemplate> findAllTemplates(final User user,
                                                  final Map<String, String> filters,
                                                  final Pageable pageable) {
        return userLibraryTemplateViewRepository.findAllByUserId(
                user.getId(),
                new SpecificationHelper<>(UserLibraryTemplateView.class, filters),
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize())
        ).map(new UserLibraryTemplateViewToLibraryTemplateFunction());
    }
}
