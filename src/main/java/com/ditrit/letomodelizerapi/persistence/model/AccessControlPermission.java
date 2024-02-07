package com.ditrit.letomodelizerapi.persistence.model;

import com.ditrit.letomodelizerapi.persistence.specification.filter.FilterType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Entity representing permissions assigned to access controls, stored in the "access_controls_permissions" table.
 * This entity captures the associations between access controls and permissions, indicating which operations are
 * permissible for specific access control entries. It extends {@link AbstractEntity} to inherit common entity
 * attributes, including metadata like creation and modification timestamps.
 *
 * <p>Permissions are fundamental to enforcing security and access control within the system, defining what actions
 * are allowed or denied for users or roles. This entity directly links access controls with their respective
 * permissions through unique identifiers, facilitating efficient access control checks and permission management.</p>
 */
@Entity
@Table(name = "access_controls_permissions")
@EqualsAndHashCode(callSuper = true)
@Data
public class AccessControlPermission extends AbstractEntity {

    /**
     * Internal id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pac_id")
    @FilterType(type = FilterType.Type.NUMBER)
    private Long id;

    /**
     * The identifier of the access control to whom this permission is assigned.
     */
    @Column(name = "aco_id")
    @FilterType(type = FilterType.Type.NUMBER)
    private Long accessControlId;

    /**
     * The identifier of the access control to whom this permission is assigned.
     */
    @Column(name = "per_id")
    @FilterType(type = FilterType.Type.NUMBER)
    private Long permissionId;

    /**
     * Set insertDate before persisting in repository.
     */
    @PrePersist
    public void prePersist() {
        this.setInsertDate(Timestamp.valueOf(LocalDateTime.now()));
    }
}
