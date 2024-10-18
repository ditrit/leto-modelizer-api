package com.ditrit.letomodelizerapi.config;

/**
 * All constants value.
 */
public final class Constants {
    /**
     * Maximum resource size.
     */
    public static final int MAXIMUM_RESOURCE_SIZE = 200;
    /**
     * Default resource size.
     */
    public static final int DEFAULT_RESOURCE_SIZE = 10;

    /**
     * The constant representing the role ID for the super administrator.
     */
    public static final String SUPER_ADMINISTRATOR_ROLE_NAME = "SUPER_ADMINISTRATOR";

    /**
     * Constant defining the default property name used to identify the user within the application.
     * This property, typically "login", is used as a key in various contexts such as session management,
     * authentication processes, and user identification within the application's data layer. It represents
     * the standard field through which users are uniquely identified across the application.
     */
    public static final String DEFAULT_USER_PROPERTY = "login";

    /**
     * The constant representing the default context property.
     */
    public static final String DEFAULT_CONTEXT_PROPERTY = "context";

    /**
     * The constant representing the default update date property.
     */
    public static final String DEFAULT_UPDATE_DATE_PROPERTY = "updateDate";

    /**
     * The constant representing the default message property.
     */
    public static final String DEFAULT_MESSAGE_PROPERTY = "message";

    /**
     * The constant representing the default plugin name property.
     */
    public static final String DEFAULT_PLUGIN_NAME_PROPERTY = "pluginName";

    /**
     * The constant representing the default AI secret value property.
     */
    public static final String DEFAULT_AI_SECRET_VALUE_PROPERTY = "value";

    /**
     * Private constructor.
     */
    private Constants() {
        throw new UnsupportedOperationException();
    }
}
