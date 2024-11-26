package com.ditrit.letomodelizerapi.persistence.model;

import com.ditrit.letomodelizerapi.model.library.LibraryTemplateType;
import com.ditrit.letomodelizerapi.persistence.converter.LibraryTemplateTypeConverter;
import com.ditrit.letomodelizerapi.persistence.converter.StringListConverter;
import io.github.zorin95670.predicate.FilterType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.ColumnTransformer;

import java.util.List;
import java.util.UUID;

/**
 * Represents a view entity for user library templates in the database.
 * It includes fields for an internal ID, user ID, library template ID, library ID, documentation URL, name, type,
 * description, base path, plugins, icon, schemas, and files.
 */
@Entity
@Table(name = "users_library_templates_view")
@Data
public class UserLibraryTemplateView {

    /**
     * Internal id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ult_id")
    @FilterType(type = String.class)
    private String id;

    /**
     * The identifier of the user to whom this template of library is assigned.
     */
    @Column(name = "usr_id")
    @FilterType(type = UUID.class)
    private UUID userId;

    /**
     * Library template ID associated with this view.
     */
    @Column(name = "lit_id")
    @FilterType(type = UUID.class)
    private UUID libraryTemplateId;

    /**
     * Library ID associated with this library template.
     */
    @Column(name = "lib_id")
    @FilterType(type = UUID.class)
    private UUID libraryId;

    /**
     * Documentation URL for the library template.
     */
    @Column(name = "documentation_url")
    @FilterType(type = String.class)
    private String documentationUrl;

    /**
     * Name of the library template.
     */
    @Column(name = "name")
    @FilterType(type = String.class)
    private String name;

    /**
     * Type of the library template, managed by a custom converter.
     */
    @Column(name = "type", nullable = false)
    @Convert(converter = LibraryTemplateTypeConverter.class)
    @ColumnTransformer(write = "?::library_template_type")
    @FilterType(type = String.class)
    private LibraryTemplateType type;

    /**
     * Description of the library template.
     */
    @Column(name = "description")
    @FilterType(type = String.class)
    private String description;

    /**
     * Base path for the library template's files.
     */
    @Column(name = "base_path")
    private String basePath;

    /**
     * JSON string representing the plugins associated with the library template.
     */
    @Column(name = "plugins")
    @Convert(converter = StringListConverter.class)
    @FilterType(type = String.class)
    private List<String> plugins;

    /**
     * Icon associated with the library template.
     */
    @Column(name = "icon")
    private String icon;

    /**
     * List of schema files associated with the library template, managed by a custom converter.
     */
    @Convert(converter = StringListConverter.class)
    @Column(name = "schemas")
    private List<String> schemas;

    /**
     * List of file paths included in the library template, managed by a custom converter.
     */
    @Convert(converter = StringListConverter.class)
    @Column(name = "files")
    private List<String> files;
}
