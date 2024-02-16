package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.library.LibraryRecord;
import com.ditrit.letomodelizerapi.persistence.model.Library;
import com.ditrit.letomodelizerapi.persistence.model.LibraryTemplate;
import com.ditrit.letomodelizerapi.persistence.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * The PermissionService interface defines the operations for managing permissions within the application.
 */
public interface LibraryService {

    /**
     * Creates a new library entity based on the provided LibraryRecord.
     * Associate user to the library role if created.
     *
     * @param libraryRecord the record containing the data to create a new library
     * @param login the login identifier of the user entity to associate with the AccessControl entity
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
     * @param id the ID of the library to update
     * @param url the new URL to set for the library
     * @throws JsonProcessingException if there is an error processing the updated library data
     */
    void update(Long id, String url) throws JsonProcessingException;

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
    Library findById(Long id);

    /**
     * Deletes a library entity by its ID.
     *
     * @param id the ID of the library to delete
     */
    void delete(Long id);

    /**
     * Finds all libraries that match the given filters and pagination settings.
     *
     * @param filters a map of filter criteria
     * @param pageable the pagination settings
     * @return a page of Library entities
     */
    Page<Library> findAll(Map<String, String> filters, Pageable pageable);

    /**
     * Finds all libraries accessible to a given user that match the provided filters and pagination settings.
     *
     * @param user the user for which to find accessible libraries
     * @param filters a map of filter criteria
     * @param pageable the pagination settings
     * @return a page of Library entities accessible to the given user
     */
    Page<Library> findAll(User user, Map<String, String> filters, Pageable pageable);

    /**
     * Finds all library templates that match the given filters and pagination settings.
     *
     * @param filters a map of filter criteria
     * @param pageable the pagination settings
     * @return a page of LibraryTemplate entities
     */
    Page<LibraryTemplate> findAllTemplates(Map<String, String> filters, Pageable pageable);

    /**
     * Finds all library templates accessible to a given user that match the provided filters and pagination settings.
     *
     * @param user the user for which to find accessible libraries
     * @param filters a map of filter criteria
     * @param pageable the pagination settings
     * @return a page of LibraryTemplate entities accessible to the given user
     */
    Page<LibraryTemplate> findAllTemplates(User user, Map<String, String> filters, Pageable pageable);
}
