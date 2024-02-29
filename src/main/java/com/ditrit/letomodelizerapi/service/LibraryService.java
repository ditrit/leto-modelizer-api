package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.library.LibraryRecord;
import com.ditrit.letomodelizerapi.persistence.model.Library;
import com.ditrit.letomodelizerapi.persistence.model.LibraryTemplate;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.net.http.HttpResponse;
import java.util.Map;
import java.util.UUID;

/**
 * The PermissionService interface defines the operations for managing permissions within the application.
 */
public interface LibraryService {

    /**
     * Creates a new library entity based on the provided LibraryRecord.
     * Associate user to the library role if created.
     *
     * @param libraryRecord the record containing the data to create a new library
     * @param login         the login identifier of the user entity to associate with the AccessControl entity
     * @return the created Library entity
     * @throws JsonProcessingException if there is an error processing the library record
     */
    Library create(LibraryRecord libraryRecord, String login) throws JsonProcessingException;

    /**
     * Updates the URL of an existing library entity identified by its ID.
     * This method allows for the modification of a library's URL attribute, facilitating updates to
     * where the library's resources are located. It ensures that the library entity remains up-to-date
     * with any changes in the resource location.
     *
     * @param id  the ID of the library to update
     * @param url the new URL to set for the library
     * @throws JsonProcessingException if there is an error processing the updated library data
     */
    void update(UUID id, String url) throws JsonProcessingException;

    /**
     * Validates the content and structure of a library located at the specified URL.
     * This method performs a series of checks against the library's content to ensure it meets predefined standards
     * and specifications. This might include validating the JSON structure, ensuring required fields are present,
     * and checking for the integrity and accessibility of linked resources. The validation process is essential
     * for maintaining the quality and reliability of libraries within the application.
     *
     * @param url the URL of the library to be validated. The URL should point directly to a library descriptor file,
     *            typically named "index.json", that contains metadata and other relevant information about the library.
     */
    void validateLibrary(String url);

    /**
     * Validates a given JSON string to ensure it meets the library data structure requirements.
     *
     * @param json the JSON string to validate
     */
    void validate(String json);

    /**
     * Finds a library entity by its ID.
     *
     * @param id the ID of the library to find
     * @return the found Library entity, or null if not found
     */
    Library findById(UUID id);

    /**
     * Deletes a library entity by its ID.
     *
     * @param id the ID of the library to delete
     */
    void delete(UUID id);

    /**
     * Finds all libraries that match the given filters and pagination settings.
     *
     * @param filters  a map of filter criteria
     * @param pageable the pagination settings
     * @return a page of Library entities
     */
    Page<Library> findAll(Map<String, String> filters, Pageable pageable);

    /**
     * Finds all libraries accessible to a given user that match the provided filters and pagination settings.
     *
     * @param user     the user for which to find accessible libraries
     * @param filters  a map of filter criteria
     * @param pageable the pagination settings
     * @return a page of Library entities accessible to the given user
     */
    Page<Library> findAll(User user, Map<String, String> filters, Pageable pageable);

    /**
     * Finds all library templates that match the given filters and pagination settings.
     *
     * @param filters  a map of filter criteria
     * @param pageable the pagination settings
     * @return a page of LibraryTemplate entities
     */
    Page<LibraryTemplate> findAllTemplates(Map<String, String> filters, Pageable pageable);

    /**
     * Finds all library templates accessible to a given user that match the provided filters and pagination settings.
     *
     * @param user     the user for which to find accessible libraries
     * @param filters  a map of filter criteria
     * @param pageable the pagination settings
     * @return a page of LibraryTemplate entities accessible to the given user
     */
    Page<LibraryTemplate> findAllTemplates(User user, Map<String, String> filters, Pageable pageable);

    /**
     * Retrieves a library template by its ID.
     * This method is responsible for fetching a LibraryTemplate entity from the database or other storage
     * based on the provided ID. It is typically used to access the details of a specific library template.
     *
     * @param id the ID of the library template to retrieve
     * @return the LibraryTemplate entity if found, or null otherwise
     */
    LibraryTemplate getTemplateById(UUID id);

    /**
     * Retrieves the icon for a library identified by its ID.
     * This method fetches the icon associated with a library, returning the content as a byte array wrapped
     * in an HttpResponse. It's used for serving library icon files directly to clients.
     *
     * @param id the ID of the library whose icon is being retrieved
     * @return an HttpResponse containing the byte array of the icon
     */
    HttpResponse<byte[]> getIcon(UUID id);

    /**
     * Retrieves the icon for a specific library template.
     * Similar to getIcon but specifically targets the icons of library templates. It uses the LibraryTemplate
     * entity directly, facilitating access to template-specific icons.
     *
     * @param libraryTemplate the LibraryTemplate entity whose icon is being retrieved
     * @return an HttpResponse containing the byte array of the template icon
     */
    HttpResponse<byte[]> getTemplateIcon(LibraryTemplate libraryTemplate);

    /**
     * Retrieves a specific schema from a library template based on its index.
     * This method is used to fetch individual schema files from a library template, identified by a zero-based
     * index, and return the content as a byte array wrapped in an HttpResponse. It allows for direct access
     * to the schema files of a library template.
     *
     * @param libraryTemplate the LibraryTemplate entity from which to retrieve the schema
     * @param index           the index of the schema within the library template
     * @return an HttpResponse containing the byte array of the schema
     */
    HttpResponse<byte[]> getTemplateSchema(LibraryTemplate libraryTemplate, Long index);

    /**
     * Retrieves a specific file from a library template based on its index.
     * Functions similarly to getTemplateSchema but is used for fetching arbitrary files from a library template.
     * The method provides access to individual files within a library template, identified by a zero-based index,
     * and returns the content as a byte array wrapped in an HttpResponse.
     *
     * @param libraryTemplate the LibraryTemplate entity from which to retrieve the file
     * @param index           the index of the file within the library template
     * @return an HttpResponse containing the byte array of the file
     */
    HttpResponse<byte[]> getTemplateFile(LibraryTemplate libraryTemplate, Long index);

    /**
     * Generates a file name for either a schema or a general file based on the provided library template and index.
     * This method determines the appropriate file name by inspecting the library template's schema or file list,
     * depending on the value of the isSchema flag, and then retrieving the name at the specified index.
     * It's designed to support operations that require dynamically accessing file or schema names within a library
     * template, such as serving file downloads or content delivery.
     *
     * @param isSchema a boolean flag indicating whether the file name to be generated is for a schema (true) or a
     *                 general file (false)
     * @param libraryTemplate the LibraryTemplate object from which to retrieve the file or schema name
     * @param index the index within the appropriate list (schemas or files) from which to retrieve the name
     * @return the file name at the specified index from either the schema or file list of the library template
     */
    String getFileName(boolean isSchema, LibraryTemplate libraryTemplate, Long index);
}
