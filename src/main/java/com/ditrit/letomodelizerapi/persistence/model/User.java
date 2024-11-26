package com.ditrit.letomodelizerapi.persistence.model;

import io.github.zorin95670.predicate.FilterType;
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
 * User entity.
 */
@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
@Data
public class User extends AbstractEntity {

    /**
     * Internal id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usr_id")
    @FilterType(type = UUID.class)
    private UUID id;

    /**
     * Email of the user.
     */
    @Column(name = "email")
    @FilterType(type = String.class)
    private String email;

    /**
     * Login of the user.
     */
    @Column(name = "login")
    @FilterType(type = String.class)
    private String login;

    /**
     * Full name of the user.
     */
    @Column(name = "name")
    @FilterType(type = String.class)
    private String name;

    /**
     * URL link to the user's profile picture.
     */
    @Column(name = "picture")
    @FilterType(type = String.class)
    private String picture;

    /**
     * Set insertDate before persisting in repository.
     */
   @PrePersist
    public void prePersist() {
        this.setInsertDate(Timestamp.valueOf(LocalDateTime.now()));
    }

}
