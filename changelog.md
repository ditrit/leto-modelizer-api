# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html)

## [Unreleased]

### Added

* Add api endpoints:
  * For AI configuration actions on proxy:
    * `GET /api/ai/proxy/configuration`, to send configuration on the proxy.
    * `GET /api/ai/proxy/descriptions`, to get all configurations descriptions on the proxy.
  * For AI configurations:
    * `GET    /api/ai/configurations`, to get all AI configurations.
    * `POST   /api/ai/configurations`, to get create an AI configuration.
    * `PUT    /api/ai/configurations/`, to update multiple AI configurations.
    * `GET    /api/ai/configurations/[CONFIGURATION_ID]`, to get an AI configuration.
    * `PUT    /api/ai/configurations/[CONFIGURATION_ID]`, to update an AI configuration.
    * `DELETE /api/ai/configurations/[CONFIGURATION_ID]`, to delete an AI configuration.
  * For AI secrets:
    * `GET    /api/ai/secrets`, to get all AI secret keys.
    * `POST   /api/ai/secrets`, to create an AI secret.
    * `GET    /api/ai/secrets/[SECRET_ID]`, to get an AI secret.
    * `PUT    /api/ai/secrets/[SECRET_ID]`, to update an AI secret.
    * `DELETE /api/ai/secrets/[SECRET_ID]`, to delete an AI secret.
* Use library to filter database from query parameters.

### Removed

- Jersey by Spring.

### Fixed

- Workflow to generate sonar statistics.

## [1.0.0] - 2024/10/15

### Added

* Setup server.
* Setup authentication to GitHub OAuth2.
* Setup user permissions.
* Add api endpoints:
  * For current user:
    * `GET /api/user/me`, to get all its information.
    * `GET /api/user/me/permissions`, to get all its permissions.
    * `GET /api/user/me/picture`, to get its picture.
    * `GET /api/user/me/roles`, to get its roles.
    * `GET /api/user/me/groups`, to get its groups.
    * `GET /api/user/me/scopes`, to get its scopes.
    * `GET /api/users/me/ai/conversations`, to get its conversations.
  * For all users:
    * `GET    /api/users`, to get all users.
    * `GET    /api/users/{login}`, to get user by login.
    * `DELETE /api/users/{login}`, to delete user by login.
    * `GET    /api/users/{login}/roles`, to get all roles associated to the user.
    * `GET    /api/users/{login}/groups`, to get all groups associated to the user.
    * `GET    /api/users/{login}/scopes`, to get all scopes associated to the user.
    * `GET    /api/users/{login}/permissions`, to get all permissions associated to the user.
    * `GET    /api/users/{login}/picture`, to get picture associated to the user.
    * `GET    /api/users/{login}/ai/conversations`, to get all conversations associated to the user.
  * For all roles:
    * `GET    /api/roles`, to get all roles.
    * `POST   /api/roles`, to create a role.
    * `GET    /api/roles/[ROLE_ID]`, to get role by id.
    * `PUT    /api/roles/[ROLE_ID]`, to update a role.
    * `DELETE /api/roles/[ROLE_ID]`, to delete a role.
    * `GET    /api/roles/[ROLE_ID]/users`, to get all users associated to the role.
    * `POST   /api/roles/[ROLE_ID]/users`, to associate role and user.
    * `DELETE /api/roles/[ROLE_ID]/users/[USER_LOGIN]`, to dissociate role and user.
    * `GET    /api/roles/[ROLE_ID]/groups`, to get all groups associated to the role.
    * `POST   /api/roles/[ROLE_ID]/groups`, to associate role and group.
    * `DELETE /api/roles/[ROLE_ID]/groups/[GROUP_ID]`, to dissociate role and group.
    * `GET    /api/roles/[ROLE_ID]/scopes`, to get all scopes associated to the role.
    * `POST   /api/roles/[ROLE_ID]/scopes`, to associate role and scope.
    * `DELETE /api/roles/[ROLE_ID]/scopes/[SCOPE_ID]`, to dissociate role and scope.
    * `GET    /api/roles/[ROLE_ID]/permissions`, to get all permissions associated to the role.
    * `POST   /api/roles/[ROLE_ID]/permissions`, to associate role and permission.
    * `DELETE /api/roles/[ROLE_ID]/permissions/[PERMISSION_ID]`, to dissociate role and permission.
  * For all groups:
    * `GET    /api/groups`, to get all groups.
    * `POST   /api/groups`, to create a group.
    * `GET    /api/groups/[GROUP_ID]`, to get group by id.
    * `PUT    /api/groups/[GROUP_ID]`, to update a group.
    * `DELETE /api/groups/[GROUP_ID]`, to delete a group.
    * `GET    /api/groups/[GROUP_ID]/users`, to get all users associated to the group.
    * `POST   /api/groups/[GROUP_ID]/users`, to associate group and user.
    * `DELETE /api/groups/[GROUP_ID]/users/[USER_LOGIN]`, to dissociate group and user.
    * `GET    /api/groups/[GROUP_ID]/groups`, to get all groups associated to the group.
    * `POST   /api/groups/[GROUP_ID]/groups`, to associate group and group.
    * `DELETE /api/groups/[GROUP_ID]/groups/[GROUP_ID]`, to dissociate group and group.
    * `GET    /api/groups/[GROUP_ID]/roles`, to get all roles associated to the group.
    * `GET    /api/groups/[GROUP_ID]/permissions`, to get all permissions associated to the group.
  * For all scopes:
    * `GET    /api/scopes`, to get all groups.
    * `POST   /api/scopes`, to create a group.
    * `GET    /api/scopes/[SCOPE_ID]`, to get group by id.
    * `PUT    /api/scopes/[SCOPE_ID]`, to update a scope.
    * `DELETE /api/scopes/[SCOPE_ID]`, to delete a scope.
    * `GET    /api/scopes/[SCOPE_ID]/users`, to get all users associated to the scope.
    * `POST   /api/scopes/[SCOPE_ID]/users`, to associate scope and user.
    * `DELETE /api/scopes/[SCOPE_ID]/users/[USER_LOGIN]`, to dissociate scope and user.
    * `GET    /api/scopes/[SCOPE_ID]/groups`, to get all groups associated to the scope.
    * `POST   /api/scopes/[SCOPE_ID]/groups`, to associate group and scope.
    * `DELETE /api/scopes/[SCOPE_ID]/groups/[GROUP_ID]`, to dissociate group and scope.
    * `GET    /api/scopes/[SCOPE_ID]/roles`, to get all roles associated to the scope.
    * `GET    /api/scopes/[SCOPE_ID]/permissions`, to get all permissions associated to the scope.
  * For all libraries:
    * `GET    /api/libraries`, to get all libraries.
    * `POST   /api/libraries`, to create a libraries.
    * `POST   /api/libraries/validate`, to validate index of a library.
    * `GET    /api/libraries/[LIBRARY_ID]`, to get a library by id.
    * `PUT    /api/libraries/[LIBRARY_ID]`, to update a library.
    * `DELETE /api/libraries/[LIBRARY_ID]`, to delete a library.
    * `GET    /api/libraries/[LIBRARY_ID]/icon`, to get icon of library.
    * `GET    /api/libraries/[LIBRARY_ID]/templates`, to get all templates of library.
    * `GET    /api/libraries/templates`, to get all templates.
    * `GET    /api/libraries/templates/[TEMPLATE_ID]`, to get a template by id.
    * `GET    /api/libraries/templates/[TEMPLATE_ID]/icon`, to get icon of template.
    * `GET    /api/libraries/templates/[TEMPLATE_ID]/schemas/[INDEX]`, to get schema of template.
    * `GET    /api/libraries/templates/[TEMPLATE_ID]/files/[INDEX]`, to get file of template.
  * For AI:
    * `GET    /api/ai/generate`, to generate diagram with AI.
    * `GET    /api/ai/conversations`, to get all conversation.
    * `POST   /api/ai/conversations`, to create a conversation.
    * `GET    /api/ai/conversations/[CONVERSATION_ID]`, to get a conversation by id.
    * `DELETE /api/ai/conversations/[CONVERSATION_ID]`, to delete a conversation.
    * `GET    /api/ai/conversations/[CONVERSATION_ID]/messages`, to get all messages of a conversations.
    * `POST   /api/ai/conversations/[CONVERSATION_ID]/messages`, to send a message to AI.
  * `/api/login`, to login.
  * `/api/redirect`, to redirect with token on leto-modelizer/leto-modelizer-admin.

[1.0.0]: https://github.com/ditrit/leto-modelizer-api/blob/main/changelog.md#1.0.0

