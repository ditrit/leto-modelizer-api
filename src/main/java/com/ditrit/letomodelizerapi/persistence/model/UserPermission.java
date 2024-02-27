package com.ditrit.letomodelizerapi.persistence.model;

import com.ditrit.letomodelizerapi.persistence.specification.filter.FilterType;
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
 * Represents a user permission entity in the system.
 */
@Entity
@Table(name = "users_permissions_view")
@EqualsAndHashCode(callSuper = true)
@Data
public class UserPermission extends AbstractEntity {

    /**
     * Internal id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usp_id")
    @FilterType(type = FilterType.Type.TEXT)
    private String id;

    /**
     * The identifier of the user to whom this permission is assigned.
     */
    @Column(name = "usr_id")
    @FilterType(type = FilterType.Type.UUID)
    private UUID userId;


    /**
     * The identifier of permission.
     */
    @Column(name = "per_id")
    @FilterType(type = FilterType.Type.UUID)
    private UUID permissionId;

    /**
     * The entity associated with this permission.
     */
    @Column(name = "entity")
    @FilterType(type = FilterType.Type.TEXT)
    private String entity;

    /**
     * The action associated with this permission.
     */
    @Column(name = "action")
    @FilterType(type = FilterType.Type.TEXT)
    private String action;

    /**
     * The identifier of the library associated with this permission.
     */
    @Column(name = "lib_id")
    @FilterType(type = FilterType.Type.UUID)
    private UUID libraryId;
}
