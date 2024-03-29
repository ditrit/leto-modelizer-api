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
     * Private constructor.
     */
    private Constants() {
        throw new UnsupportedOperationException();
    }
}
