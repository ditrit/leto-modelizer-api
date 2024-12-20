package com.ditrit.letomodelizerapi.persistence.model;

import io.github.zorin95670.predicate.FilterType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

/**
 * Entity representing a SQL view for aggregating permissions across a hierarchy of roles within the access control
 * system.
 * Mapped to "access_controls_permissions_view", this entity facilitates the retrieval of all permissions assigned to a
 * specific role, including those inherited from parent roles. It serves as a structured representation of the complex
 * relationships between roles and permissions, supporting efficient permission checks and queries within the system.
 */
@Entity
@Table(name = "access_controls_permissions_view")
@EqualsAndHashCode(callSuper = true)
@Data
public class AccessControlPermissionView extends AbstractEntity {

    /**
     * Internal id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "acp_id")
    @FilterType(type = String.class)
    private String id;

    /**
     * The identifier of the access control to whom this permission is assigned.
     */
    @Column(name = "aco_id")
    @FilterType(type = UUID.class)
    private UUID accessControlId;

    /**
     * The identifier of the access control to whom this permission is assigned.
     */
    @Column(name = "per_id")
    @FilterType(type = UUID.class)
    private UUID permissionId;

    /**
     * The entity associated with this permission.
     */
    @Column(name = "entity")
    @FilterType(type = String.class)
    private String entity;

    /**
     * The action associated with this permission.
     */
    @Column(name = "action")
    @FilterType(type = String.class)
    private String action;

    /**
     * The identifier of the library associated with this permission.
     */
    @Column(name = "lib_id")
    @FilterType(type = UUID.class)
    private UUID libraryId;

    /**
     * Boolean flag indicating whether the permission is directly assigned to the role or inherited from a parent role
     * in the hierarchy. Direct permissions are explicitly defined for the role, while indirect permissions are
     * inherited through the role's position in the role hierarchy, offering a nuanced view of permission origins.
     */
    @Column(name = "direct")
    @FilterType(type = Boolean.class)
    private Boolean isDirect;
}
