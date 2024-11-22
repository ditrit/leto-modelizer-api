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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ColumnTransformer;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Represents a library template entity in the database.
 * Includes fields for internal ID, library ID, documentation URL, name, type, description, base path, plugins, icon,
 * schemas, and files.
 */
@Entity
@Table(name = "library_templates")
@EqualsAndHashCode(callSuper = true)
@Data
public class LibraryTemplate extends AbstractEntity {

    /**
     * Internal id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lit_id")
    @FilterType(type = UUID.class)
    private UUID id;

    /**
     * Library ID this template belongs to.
     */
    @Column(name = "lib_id")
    @FilterType(type = UUID.class)
    private UUID libraryId;

    /**
     * URL for the template's documentation.
     */
    @Column(name = "documentation_url")
    @FilterType(type = String.class)
    private String documentationUrl;

    /**
     * Name of the template.
     */
    @Column(name = "name")
    @FilterType(type = String.class)
    private String name;

    /**
     * Type of the template, represented by a custom enum.
     */
    @Column(name = "type", nullable = false)
    @Convert(converter = LibraryTemplateTypeConverter.class)
    @ColumnTransformer(write = "?::library_template_type")
    @FilterType(type = String.class)
    private LibraryTemplateType type;

    /**
     * Description of the template.
     */
    @Column(name = "description")
    @FilterType(type = String.class)
    private String description;

    /**
     * Base path for the template's files.
     */
    @Column(name = "base_path")
    private String basePath;

    /**
     * JSON string representing the plugins associated with the template.
     */
    @Column(name = "plugins")
    @Convert(converter = StringListConverter.class)
    @FilterType(type = String.class)
    private List<String> plugins;

    /**
     * Icon for the template.
     */
    @Column(name = "icon")
    private String icon;

    /**
     * List of schema files associated with the template, stored as a JSON string.
     */
    @Convert(converter = StringListConverter.class)
    @Column(name = "schemas")
    private List<String> schemas;

    /**
     * List of file paths included in the template, stored as a JSON string.
     */
    @Convert(converter = StringListConverter.class)
    @Column(name = "files")
    private List<String> files;

    /**
     * Pre-persist operation to set the insert date to the current timestamp before saving.
     */
    @PrePersist
    public void prePersist() {
        this.setInsertDate(Timestamp.valueOf(LocalDateTime.now()));
    }
}
