package com.ditrit.letomodelizerapi.model.permission;

/**
 * Enumeration of action-based permissions within the system.
 * This enum defines various types of actions that can be performed by users,
 * reflecting different levels of permissions associated with these actions.
 */
public enum ActionPermission {
    /**
     * Permission to access or view a particular resource.
     * This typically involves reading or viewing operations without allowing modifications.
     */
    ACCESS,

    /**
     * Permission to create a new resource in the system.
     * This involves operations that add new entities or data.
     */
    CREATE,

    /**
     * Permission to delete an existing resource from the system.
     * This involves operations that remove entities or data.
     */
    DELETE,

    /**
     * Permission to update an existing resource from the system.
     * This involves operations that remove entities or data.
     */
    UPDATE;
}
