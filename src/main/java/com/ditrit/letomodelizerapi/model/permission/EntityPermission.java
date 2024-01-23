package com.ditrit.letomodelizerapi.model.permission;

/**
 * Enumeration of various entity permissions used within the system.
 * Each constant represents a specific type of permission that can be assigned to a user.
 * These permissions typically define the level of access or control a user has over different
 * sections or functionalities of the application.
 */
public enum EntityPermission {
    /**
     * Represents administrative permissions, typically granting full access
     * of the administration application.
     */
    ADMIN,
    /**
     * Represents permissions related to managing projects.
     */
    PROJECT,
    /**
     * Represents permissions specific to project templates.
     */
    PROJECT_TEMPLATE,
    /**
     * Represents permissions associated with diagrams.
     */
    DIAGRAM,
    /**
     * Represents permissions for handling diagram templates.
     */
    DIAGRAM_TEMPLATE,
    /**
     * Represents permissions for managing components.
     */
    COMPONENT,
    /**
     * Represents permissions specific to component templates.
     */
    COMPONENT_TEMPLATE,
    /**
     * Represents permissions related to libraries within the system.
     */
    LIBRARY;
}
