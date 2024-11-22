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
 * Represents a library entity in the database.
 * Includes fields for ID, URL, documentation URL, name, version, maintainer, description, and icon.
 * Annotations are used to define how each field is mapped to the database table 'libraries'.
 */
@Entity
@Table(name = "libraries")
@EqualsAndHashCode(callSuper = true)
@Data
public class Library extends AbstractEntity {

    /**
     * Internal id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lib_id")
    @FilterType(type = UUID.class)
    private UUID id;

    /**
     * URL of the library.
     */
    @Column(name = "url")
    @FilterType(type = String.class)
    private String url;

    /**
     * URL for the library's documentation.
     */
    @Column(name = "documentation_url")
    @FilterType(type = String.class)
    private String documentationUrl;

    /**
     * Name of the library.
     */
    @Column(name = "name")
    @FilterType(type = String.class)
    private String name;

    /**
     * Version of the library.
     */
    @Column(name = "version")
    @FilterType(type = String.class)
    private String version;

    /**
     * Maintainer of the library.
     */
    @Column(name = "maintainer")
    @FilterType(type = String.class)
    private String maintainer;

    /**
     * Description of the library.
     */
    @Column(name = "description")
    @FilterType(type = String.class)
    private String description;

    /**
     * Icon for the library.
     */
    @Column(name = "icon")
    private String icon;

    /**
     * Pre-persist operation to set the insert date to the current timestamp before saving.
     */
    @PrePersist
    public void prePersist() {
        this.setInsertDate(Timestamp.valueOf(LocalDateTime.now()));
    }
}
