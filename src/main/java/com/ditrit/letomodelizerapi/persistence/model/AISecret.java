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
 * Represents an AI secret in the system.
 */
@Entity
@Table(name = "ai_secrets")
@EqualsAndHashCode(callSuper = true)
@Data
public class AISecret extends AbstractEntity {

    /**
     * Internal id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ais_id")
    @FilterType(type = FilterType.Type.UUID)
    private UUID id;

    /**
     * The secret key given by the user, must be unique.
     */
    @Column(name = "key")
    @FilterType(type = FilterType.Type.TEXT)
    private String key;

    /**
     * The encoded value of the secret.
     */
    @Column(name = "value")
    private byte[] value;

    /**
     * Set insertDate before persisting in repository.
     */
    @PrePersist
    public void prePersist() {
        this.setInsertDate(Timestamp.valueOf(LocalDateTime.now()));
    }
}
