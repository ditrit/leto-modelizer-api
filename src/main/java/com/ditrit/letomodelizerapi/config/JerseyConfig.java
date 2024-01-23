package com.ditrit.letomodelizerapi.config;

import com.ditrit.letomodelizerapi.controller.CurrentUserController;
import com.ditrit.letomodelizerapi.controller.HomeController;
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
        register(CurrentUserController.class);
        register(HomeController.class);

        // Exception handler
        register(ApiExceptionHandler.class);
        register(ConstraintViolationExceptionHandler.class);
    }
}
