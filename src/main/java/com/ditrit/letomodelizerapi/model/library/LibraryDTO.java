package com.ditrit.letomodelizerapi.model.library;

import lombok.Data;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) for Library.
 * This class is used for transferring Library data between different layers of the application,
 * typically between services and controllers. It is designed to encapsulate the data attributes of a Library
 * entity in a form that is easy to serialize and deserialize when sending responses or requests.
 */
@Data
public class LibraryDTO {

    /**
     * Internal id.
     */
    private UUID id;

    /**
     * URL of the library.
     */
    private String url;

    /**
     * URL for the library's documentation.
     */
    private String documentationUrl;

    /**
     * Name of the library.
     */
    private String name;

    /**
     * Version of the library.
     */
    private String version;

    /**
     * Maintainer of the library.
     */
    private String maintainer;

    /**
     * Description of the library.
     */
    private String description;

    /**
     * Icon of the library.
     */
    private String icon;
}
