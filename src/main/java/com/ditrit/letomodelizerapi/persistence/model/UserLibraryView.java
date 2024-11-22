package com.ditrit.letomodelizerapi.persistence.model;

import io.github.zorin95670.predicate.FilterType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

/**
 * Represents a view entity for user libraries in the database.
 * It includes fields for an internal ID, user ID, library ID and all library fields.
 */
@Entity
@Table(name = "users_libraries_view")
@Data
public class UserLibraryView {

    /**
     * Internal id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "uli_id")
    @FilterType(type = String.class)
    private String id;

    /**
     * The identifier of the user to whom this template of library is assigned.
     */
    @Column(name = "usr_id")
    @FilterType(type = UUID.class)
    private UUID userId;

    /**
     * Library ID associated with this library template.
     */
    @Column(name = "lib_id")
    @FilterType(type = UUID.class)
    private UUID libraryId;

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
}
