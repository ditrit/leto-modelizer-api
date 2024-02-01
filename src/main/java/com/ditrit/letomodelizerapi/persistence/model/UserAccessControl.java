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
 * Represents a user access control entity in the system.
 */
@Entity
@Table(name = "users_access_controls")
@EqualsAndHashCode(callSuper = true)
@Data
public class UserAccessControl extends AbstractEntity {

    /**
     * Internal id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uac_id")
    @FilterType(type = FilterType.Type.NUMBER)
    private Long id;

    /**
     * The identifier of the user.
     */
    @Column(name = "usr_id")
    @FilterType(type = FilterType.Type.NUMBER)
    private Long userId;


    /**
     * The identifier of the access control.
     */
    @Column(name = "aco_id")
    @FilterType(type = FilterType.Type.NUMBER)
    private Long accessControlId;

    /**
     * Set insertDate before persisting in repository.
     */
    @PrePersist
    public void prePersist() {
        this.setInsertDate(Timestamp.valueOf(LocalDateTime.now()));
    }
}
