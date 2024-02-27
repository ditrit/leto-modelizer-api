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
import java.util.UUID;

/**
 * Entity representing the access control hierarchy stored in the "access_controls_tree" table.
 * This entity extends AbstractEntity and includes fields representing the hierarchical structure
 * of access controls. It is used to maintain a tree structure for access control entries, where
 * each entry can have a parent and a current access control identifier.
 */
@Entity
@Table(name = "access_controls_tree")
@EqualsAndHashCode(callSuper = true)
@Data
public class AccessControlTree extends AbstractEntity {

    /**
     * Internal id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "act_id")
    @FilterType(type = FilterType.Type.UUID)
    private UUID id;

    /**
     * Identifier of the parent access control in the hierarchy.
     * This field represents the parent node in the access control tree structure.
     */
    @Column(name = "parent")
    @FilterType(type = FilterType.Type.UUID)
    private UUID parent;

    /**
     * Identifier of the current access control.
     * This field represents the current node in the access control tree structure.
     */
    @Column(name = "current")
    @FilterType(type = FilterType.Type.UUID)
    private UUID current;

    /**
     * Set insertDate before persisting in repository.
     */
    @PrePersist
    public void prePersist() {
        this.setInsertDate(Timestamp.valueOf(LocalDateTime.now()));
    }
}
