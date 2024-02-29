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
    @FilterType(type = FilterType.Type.UUID)
    private UUID id;

    /**
     * URL of the library.
     */
    @Column(name = "url")
    @FilterType(type = FilterType.Type.TEXT)
    private String url;

    /**
     * URL for the library's documentation.
     */
    @Column(name = "documentation_url")
    @FilterType(type = FilterType.Type.TEXT)
    private String documentationUrl;

    /**
     * Name of the library.
     */
    @Column(name = "name")
    @FilterType(type = FilterType.Type.TEXT)
    private String name;

    /**
     * Version of the library.
     */
    @Column(name = "version")
    @FilterType(type = FilterType.Type.TEXT)
    private String version;

    /**
     * Maintainer of the library.
     */
    @Column(name = "maintainer")
    @FilterType(type = FilterType.Type.TEXT)
    private String maintainer;

    /**
     * Description of the library.
     */
    @Column(name = "description")
    @FilterType(type = FilterType.Type.TEXT)
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
