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
import org.hibernate.annotations.ColumnTransformer;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Represents a permission entity in the system.
 */
@Entity
@Table(name = "permissions")
@EqualsAndHashCode(callSuper = true)
@Data
public class Permission extends AbstractEntity {

    /**
     * Internal id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "per_id")
    @FilterType(type = FilterType.Type.NUMBER)
    private Long id;

    /**
     * The entity associated with this permission.
     */
    @Column(name = "entity")
    @ColumnTransformer(write = "?::entity_type")
    @FilterType(type = FilterType.Type.TEXT)
    private String entity;

    /**
     * The action associated with this permission.
     */
    @Column(name = "action")
    @ColumnTransformer(write = "?::action_type")
    @FilterType(type = FilterType.Type.TEXT)
    private String action;

    /**
     * The identifier of the library associated with this permission.
     */
    @Column(name = "lib_id")
    @FilterType(type = FilterType.Type.NUMBER)
    private Long libraryId;

    /**
     * Set insertDate before persisting in repository.
     */
    @PrePersist
    public void prePersist() {
        this.setInsertDate(Timestamp.valueOf(LocalDateTime.now()));
    }
}
