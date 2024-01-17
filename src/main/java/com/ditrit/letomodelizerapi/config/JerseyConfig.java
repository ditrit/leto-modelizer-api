package com.ditrit.letomodelizerapi.config;

import com.ditrit.letomodelizerapi.controller.UserController;
import com.ditrit.letomodelizerapi.controller.handler.ApiExceptionHandler;
import com.ditrit.letomodelizerapi.controller.handler.ConstraintViolationExceptionHandler;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

/**
 * Jersey configuration.
 */
@Configuration
public class JerseyConfig extends ResourceConfig {

    /**
     * Default constructor to initialize registered endpoints and filters.
     */
    public JerseyConfig() {
        // Filter
        // Controller
        register(UserController.class);

        // Exception handler
        register(ApiExceptionHandler.class);
        register(ConstraintViolationExceptionHandler.class);
    }
}
