package com.ditrit.letomodelizerapi.persistence.model;

import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import com.ditrit.letomodelizerapi.persistence.specification.filter.FilterType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

/**
 * Represents a user access control entity in the system.
 */
@Entity
@Table(name = "users_access_controls_view")
@Data
public class UserAccessControlView {

    /**
     * Internal id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usa_id")
    @FilterType(type = FilterType.Type.TEXT)
    private String id;

    /**
     * The identifier of the user to whom this access control is assigned.
     */
    @Column(name = "usr_id")
    @FilterType(type = FilterType.Type.UUID)
    private UUID userId;

    /**
     * Email of the user.
     */
    @Column(name = "email")
    @FilterType(type = FilterType.Type.TEXT)
    private String email;

    /**
     * Login of the user.
     */
    @Column(name = "login")
    @FilterType(type = FilterType.Type.TEXT)
    private String login;

    /**
     * Full name of the user.
     */
    @Column(name = "user_name")
    @FilterType(type = FilterType.Type.TEXT)
    private String userName;

    /**
     * The identifier of the access control.
     */
    @Column(name = "aco_id")
    @FilterType(type = FilterType.Type.UUID)
    private UUID accessControlId;

    /**
     * The name of the access control.
     */
    @Column(name = "access_control_name")
    @FilterType(type = FilterType.Type.TEXT)
    private String name;

    /**
     * The type of access control, which determines whether it's a ROLE, GROUP, or SCOPE.
     * This is defined by the AccessControlType enum.
     */
    @Column(name = "type", nullable = false)
    @FilterType(type = FilterType.Type.TEXT)
    private AccessControlType type;

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
