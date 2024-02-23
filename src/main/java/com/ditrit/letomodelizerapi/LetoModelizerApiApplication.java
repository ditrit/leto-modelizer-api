package com.ditrit.letomodelizerapi;

import com.ditrit.letomodelizerapi.config.Constants;
import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import com.ditrit.letomodelizerapi.service.AccessControlService;
import com.ditrit.letomodelizerapi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * Default application runnable.
 */
@Slf4j
@SpringBootApplication
public class LetoModelizerApiApplication {

    /**
     * Run application.
     * @param args Default application arguments.
     */
    public static void main(final String[] args) {
        SpringApplication.run(LetoModelizerApiApplication.class, args);
    }

    /**
     * Initializes the super administrator account based on application properties, specifically the login provided
     * via environment configuration. This method ensures the application is equipped with a super administrator
     * upon startup, facilitating high-level administrative access and control.
     *
     * The process involves checking for a super administrator login defined in the environment. If present and valid,
     * a new administrator user is created with this login, and the user is associated with the super administrator role
     * using the provided services. This setup is crucial for maintaining secure and effective management of the
     * application.
     *
     * @param environment the Spring Environment used to access application properties.
     * @param userService the UserService to create and manage user entities.
     * @param accessControlService the AccessControlService to manage user roles and permissions.
     * @return true if the super administrator was successfully initialized, false if the login was not specified or
     * was blank.
     */
    @Bean
    public boolean initSuperAdministrator(final Environment environment,
                                          final UserService userService,
                                          final AccessControlService accessControlService) {
        String login = environment.getProperty("SUPER_ADMINISTRATOR_LOGIN");

        if (StringUtils.isBlank(login)) {
            return false;
        }

        log.info("Init SUPER_ADMINISTRATOR with login {}", login);
        userService.createAdmin(login);
        accessControlService.associateUser(AccessControlType.ROLE, Constants.SUPER_ADMINISTRATOR_ROLE_ID, login);
        log.info("User with login {} is associated to the role SUPER_ADMINISTRATOR", login);

        return true;
    }
}
