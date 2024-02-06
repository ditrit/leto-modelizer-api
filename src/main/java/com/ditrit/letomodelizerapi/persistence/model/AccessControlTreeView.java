package com.ditrit.letomodelizerapi.persistence.model;

import com.ditrit.letomodelizerapi.persistence.specification.filter.FilterType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Entity representing a view of the access control tree, stored in the "access_controls_tree_view" view.
 * This entity extends AbstractEntity and includes fields for representing a hierarchical view of access controls.
 * It is used to provide a structured representation of access control entries, including both current and parent
 * access control information.
 */
@Entity
@Table(name = "access_controls_tree_view")
@Data
public class AccessControlTreeView {

    /**
     * Internal id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aca_id")
    @FilterType(type = FilterType.Type.TEXT)
    private String id;

    /**
     * Identifier of the current access control.
     * This field represents the current node in the access control tree structure.
     */
    @Column(name = "aco_id")
    @FilterType(type = FilterType.Type.NUMBER)
    private Long accessControlId;

    /**
     * Type of the current access control.
     */
    @Column(name = "type")
    @FilterType(type = FilterType.Type.TEXT)
    private String accessControlType;

    /**
     * Name of the current access control.
     */
    @Column(name = "name")
    @FilterType(type = FilterType.Type.TEXT)
    private String accessControlName;

    /**
     * Identifier of the parent access control in the tree structure.
     */
    @Column(name = "parent")
    @FilterType(type = FilterType.Type.NUMBER)
    private Long parentAccessControlId;

    /**
     * Name of the parent access control.
     */
    @Column(name = "parent_name")
    @FilterType(type = FilterType.Type.TEXT)
    private String parentAccessControlName;

    /**
     * Type of the parent access control.
     */
    @Column(name = "parent_type")
    @FilterType(type = FilterType.Type.TEXT)
    private String parentAccessControlType;

    /**
     * Indicates whether the access control relationship is direct.
     * This field distinguishes between direct and indirect relationships in the access control tree.
     * A direct relationship implies that the current access control is immediately subordinate to the parent,
     * without any intermediate access controls in between. This is useful for understanding the immediate
     * hierarchical structure and for operations that depend on direct lineage.
     */
    @Column(name = "direct")
    @FilterType(type = FilterType.Type.BOOLEAN)
    private Boolean isDirect;
}
