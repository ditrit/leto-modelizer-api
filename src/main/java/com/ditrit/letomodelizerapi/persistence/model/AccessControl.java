package com.ditrit.letomodelizerapi.persistence.model;

import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import com.ditrit.letomodelizerapi.persistence.converter.AccessControlTypeConverter;
import io.github.zorin95670.predicate.FilterType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import java.util.UUID;

/**
 * Represents an access control entity in the database, which can be a ROLE, GROUP, or SCOPE.
 */
@Entity
@Table(name = "access_controls")
@EqualsAndHashCode(callSuper = true)
@Data
public class AccessControl extends AbstractEntity {

    /**
     * Internal id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aco_id")
    @FilterType(type = UUID.class)
    private UUID id;

    /**
     * The name of the access control.
     */
    @Column(name = "name")
    @FilterType(type = String.class)
    private String name;

    /**
     * The type of access control, which determines whether it's a ROLE, GROUP, or SCOPE.
     * This is defined by the AccessControlType enum.
     */
    @Column(name = "type", nullable = false)
    @Convert(converter = AccessControlTypeConverter.class)
    @ColumnTransformer(write = "?::access_control_type")
    @FilterType(type = AccessControlType.class)
    private AccessControlType type;

    /**
     * Set insertDate before persisting in repository.
     */
    @PrePersist
    public void prePersist() {
        this.setInsertDate(Timestamp.valueOf(LocalDateTime.now()));
    }
}
