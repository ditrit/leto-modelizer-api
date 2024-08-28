package com.ditrit.letomodelizerapi.config;

import com.ditrit.letomodelizerapi.controller.CsrfController;
import com.ditrit.letomodelizerapi.controller.CurrentUserController;
import com.ditrit.letomodelizerapi.controller.GroupController;
import com.ditrit.letomodelizerapi.controller.HomeController;
import com.ditrit.letomodelizerapi.controller.AIController;
import com.ditrit.letomodelizerapi.controller.LibraryController;
import com.ditrit.letomodelizerapi.controller.PermissionController;
import com.ditrit.letomodelizerapi.controller.RoleController;
import com.ditrit.letomodelizerapi.controller.ScopeController;
import com.ditrit.letomodelizerapi.controller.UserController;
import com.ditrit.letomodelizerapi.controller.handler.ApiExceptionHandler;
import com.ditrit.letomodelizerapi.controller.handler.ConstraintViolationExceptionHandler;
import com.ditrit.letomodelizerapi.controller.handler.DataIntegrityViolationExceptionHandler;
import com.ditrit.letomodelizerapi.controller.handler.IllegalArgumentExceptionHandler;
import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Jersey configuration.
 */
@Configuration
public class JerseyConfig extends ResourceConfig {

    /**
     * Default constructor to initialize registered endpoints and filters.
     * @param aiHost the host value for ai request, injected from application properties.
     */
    @Autowired
    public JerseyConfig(@Value("${ai.host}") final String aiHost) {
        // Filter
        // Controller
        register(UserController.class);
        register(CurrentUserController.class);
        register(RoleController.class);
        register(GroupController.class);
        register(ScopeController.class);
        register(LibraryController.class);
        register(HomeController.class);
        register(CsrfController.class);
        register(PermissionController.class);

        if (StringUtils.isNotBlank(aiHost)) {
            register(AIController.class);
        }

        // Exception handler
        register(ApiExceptionHandler.class);
        register(ConstraintViolationExceptionHandler.class);
        register(DataIntegrityViolationExceptionHandler.class);
        register(IllegalArgumentExceptionHandler.class);
    }
}
