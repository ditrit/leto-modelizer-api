package com.ditrit.letomodelizerapi.persistence.model;

import com.ditrit.letomodelizerapi.model.permission.ActionPermission;
import com.ditrit.letomodelizerapi.model.permission.EntityPermission;
import com.ditrit.letomodelizerapi.persistence.converter.ActionPermissionConverter;
import com.ditrit.letomodelizerapi.persistence.converter.EntityPermissionConverter;
import io.github.zorin95670.predicate.FilterType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ColumnTransformer;

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
    @FilterType(type = String.class)
    private String id;

    /**
     * The identifier of the user to whom this permission is assigned.
     */
    @Column(name = "usr_id")
    @FilterType(type = UUID.class)
    private UUID userId;


    /**
     * The identifier of permission.
     */
    @Column(name = "per_id")
    @FilterType(type = UUID.class)
    private UUID permissionId;

    /**
     * The entity associated with this permission.
     */
    @Column(name = "entity")
    @Convert(converter = EntityPermissionConverter.class)
    @ColumnTransformer(write = "?::entity_type")
    @FilterType(type = EntityPermission.class)
    private EntityPermission entity;

    /**
     * The action associated with this permission.
     */
    @Column(name = "action")
    @Convert(converter = ActionPermissionConverter.class)
    @ColumnTransformer(write = "?::action_type")
    @FilterType(type = ActionPermission.class)
    private ActionPermission action;

    /**
     * The identifier of the library associated with this permission.
     */
    @Column(name = "lib_id")
    @FilterType(type = UUID.class)
    private UUID libraryId;
}
