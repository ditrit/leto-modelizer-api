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
 * Represents an AI configuration in the system.
 */
@Entity
@Table(name = "ai_configurations")
@EqualsAndHashCode(callSuper = true)
@Data
public class AIConfiguration extends AbstractEntity {

    /**
     * Internal id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "acf_id")
    @FilterType(type = FilterType.Type.UUID)
    private UUID id;

    /**
     * The configuration handler.
     */
    @Column(name = "handler")
    @FilterType(type = FilterType.Type.TEXT)
    private String handler;

    /**
     * The configuration key.
     */
    @Column(name = "key")
    @FilterType(type = FilterType.Type.TEXT)
    private String key;

    /**
     * The value of the configuration.
     */
    @Column(name = "value")
    @FilterType(type = FilterType.Type.TEXT)
    private String value;

    /**
     * Set insertDate before persisting in repository.
     */
    @PrePersist
    public void prePersist() {
        this.setInsertDate(Timestamp.valueOf(LocalDateTime.now()));
    }
}
