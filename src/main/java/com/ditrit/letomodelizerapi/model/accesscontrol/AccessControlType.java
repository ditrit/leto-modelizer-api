package com.ditrit.letomodelizerapi.model.accesscontrol;

/**
 * Enum representing the types of access controls in a system.
 * This enum is used to categorize different aspects of access control,
 * defining how they interact with users, groups, and roles.
 *
 * <p>Each access control type has specific linkage rules:</p>
 * <ul>
 *     <li>ROLE - Represents a role, which is exclusively linked to permissions.
 *         Roles are typically used to group sets of permissions and can be assigned to users or groups to grant those
 *         permissions.</li>
 *     <li>GROUP - Represents a group, which is a collection of users.
 *         Groups can be linked to scopes and roles, facilitating the assignment of access controls based on group
 *         membership.</li>
 *     <li>SCOPE - Represents a scope, often used to define the extent or range of access control (e.g., module-level,
 *         application-level).
 *         Scopes can be linked to groups, roles, or individual users to specify the context in which access controls
 *         are applicable.</li>
 * </ul>
 *
 * <p>Note: While scopes and groups can be linked to various elements, permissions are uniquely linked to roles,
 * defining the access capabilities of those roles.</p>
 *
 * <p>This enum is mapped to a corresponding PostgreSQL enum type in the database.</p>
 */
public enum AccessControlType {
    /**
     * Represents a role. Roles are exclusively linked to permissions and are used to group sets of permissions.
     * These roles can then be assigned to users or groups.
     */
    ROLE,

    /**
     * Represents a group, a collection of users. Groups can be linked to scopes and roles, enabling the assignment
     * of access controls based on group membership.
     */
    GROUP,

    /**
     * Represents a scope, defining the extent or range of access control. Scopes can be linked to groups, roles, or
     * individual users, specifying the context in which access controls are applicable.
     */
    SCOPE;
}
