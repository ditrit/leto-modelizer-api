package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlDirectDTO;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlRecord;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import com.ditrit.letomodelizerapi.persistence.model.AccessControl;
import com.ditrit.letomodelizerapi.persistence.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * Service interface for managing AccessControl entities.
 * This interface defines methods for operations like finding, creating, updating, and deleting AccessControl entities.
 */
public interface AccessControlService {

    /**
     * Finds and returns a page of AccessControl entities of a specific type, filtered by provided criteria.
     *
     * @param type     the AccessControlType to filter the AccessControl entities
     * @param filters  a Map of strings representing the filtering criteria
     * @param pageable a Pageable object for pagination information
     * @return a Page of AccessControl entities matching the specified type and filters
     */
    Page<AccessControl> findAll(AccessControlType type, Map<String, String> filters, Pageable pageable);

    /**
     * Finds and returns a paginated list of AccessControl entities of a specific type associated with a given User,
     * filtered by provided criteria. This method allows for retrieving AccessControl entities linked to a specific
     * user.
     *
     * @param type     the AccessControlType to filter the AccessControl entities
     * @param user     the User object associated with the AccessControl entities
     * @param filters  a Map of strings representing the filtering criteria
     * @param pageable a Pageable object for pagination information
     * @return a Page of AccessControl entities matching the specified type, user, and filters
     */
    Page<AccessControl> findAll(AccessControlType type, User user, Map<String, String> filters, Pageable pageable);

    /**
     * Finds and returns a paginated list of Users associated with a specific AccessControl entity, filtered by
     * provided criteria.
     * This method can be used to find users who have a certain type of access control, as identified by the access
     * control ID.
     *
     * @param id       the ID of the AccessControl entity
     * @param filters  a Map of strings representing the filtering criteria
     * @param pageable a Pageable object for pagination information
     * @return a Page of Users associated with the specified AccessControl entity
     */
    Page<User> findAllUsers(Long id, Map<String, String> filters, Pageable pageable);

    /**
     * Retrieves all sub-access controls associated with a specific parent access control ID.
     *
     * This method is designed to find and return all sub-access controls for a given parent access control, identified
     * by its ID. It applies a set of filtering criteria and pagination to efficiently manage and return large datasets.
     * The method is essential for hierarchical access control systems where access controls may have multiple levels
     * of granularity or inheritance.
     *
     * @param id the ID of the parent access control for which sub-access controls are being searched. This ID uniquely
     *           identifies an access control entity within the system.
     * @param type the specific type of access control entities to retrieve, which helps in narrowing down the search to
     *             relevant sub-categories or classifications of access controls.
     * @param filters a map containing key-value pairs used for filtering the search results. These filters can apply to
     *                various attributes of the access controls, such as names, statuses, or custom properties, to
     *                refine the results further.
     * @param pageable an object that encapsulates pagination and sorting criteria. This parameter allows the method to
     *                 return a subset of the results in a manageable, paginated format, facilitating easier data
     *                 handling and user interface rendering.
     * @return a {@code Page<AccessControlDirectDTO>} object containing the sub-access controls associated with the
     * specified parent ID. This paginated object includes the subset of access controls that match the search criteria,
     * along with pagination details such as total results, current page number, and total pages available.
     */
    Page<AccessControlDirectDTO> findAllAccessControls(Long id,
                                                       AccessControlType type,
                                                       Map<String, String> filters,
                                                       Pageable pageable);

    /**
     * Finds and returns an AccessControl entity of a specific type by its ID.
     *
     * @param type the AccessControlType of the AccessControl entity
     * @param id   the ID of the AccessControl entity
     * @return the found AccessControl entity, or null if no entity is found with the given ID
     */
    AccessControl findById(AccessControlType type, Long id);

    /**
     * Creates a new AccessControl entity of a specified type.
     *
     * @param type                the AccessControlType of the new AccessControl entity
     * @param accessControlRecord an AccessControlRecord object containing the data for the new entity
     * @return the newly created AccessControl entity
     */
    AccessControl create(AccessControlType type, AccessControlRecord accessControlRecord);

    /**
     * Updates an existing AccessControl entity of a specified type and ID.
     *
     * @param type                the AccessControlType of the AccessControl entity to update
     * @param id                  the ID of the AccessControl entity to update
     * @param accessControlRecord an AccessControlRecord object containing the updated data
     * @return the updated AccessControl entity
     */
    AccessControl update(AccessControlType type, Long id, AccessControlRecord accessControlRecord);

    /**
     * Deletes an AccessControl entity of a specified type by its ID.
     *
     * @param type the AccessControlType of the AccessControl entity to delete
     * @param id   the ID of the AccessControl entity to delete
     */
    void delete(AccessControlType type, Long id);

    /**
     * Associates a role of a specific type with a parent access control entity, creating a hierarchical relationship.
     * This method is used for structuring access controls within a hierarchy, specifying parent-child relationships.
     *
     * @param parentType the AccessControlType of the parent AccessControl entity
     * @param id         the ID of the parent AccessControl entity
     * @param type       the AccessControlType of the child AccessControl entity to associate
     * @param roleId     the ID of the child AccessControl entity to associate
     */
    void associate(AccessControlType parentType, Long id, AccessControlType type, Long roleId);

    /**
     * Dissociates a role of a specific type from a parent access control entity, removing the hierarchical
     * relationship.
     * This method is used to manage and update the structure of access controls within the system, allowing for
     * the modification of parent-child relationships.
     *
     * @param parentType the AccessControlType of the parent AccessControl entity
     * @param id         the ID of the parent AccessControl entity
     * @param type       the AccessControlType of the child AccessControl entity to dissociate
     * @param roleId     the ID of the child AccessControl entity to dissociate
     */
    void dissociate(AccessControlType parentType, Long id, AccessControlType type, Long roleId);

    /**
     * Associates a specific AccessControl entity with a user entity identified by a login string.
     * This method is typically used to link access control settings with a specific user.
     *
     * @param type   the AccessControlType of the AccessControl entity to be associated
     * @param id     the ID of the AccessControl entity to be associated
     * @param login  the login identifier of the user entity to associate with the AccessControl entity
     */
    void associateUser(AccessControlType type, Long id, String login);

    /**
     * Dissociates a specific AccessControl entity from a user entity identified by a login string.
     * This method is used to remove a previously established link between access control settings and a specific user.
     *
     * @param type   the AccessControlType of the AccessControl entity to be dissociated
     * @param id     the ID of the AccessControl entity to be dissociated
     * @param login  the login identifier of the user entity to dissociate from the AccessControl entity
     */
    void dissociateUser(AccessControlType type, Long id, String login);
}
