# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html)

## [Unreleased]

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
  * For all users:
    * `GET    /api/users`, to get all users.
    * `GET    /api/users/{login}`, to get user by login.
    * `DELETE /api/users/{login}`, to delete user by login.
    * `GET    /api/users/{login}/roles`, to get all roles associated to the user.
    * `GET    /api/users/{login}/groups`, to get all groups associated to the user.
    * `GET    /api/users/{login}/picture`, to get picture associated to the user.
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
  * `/api/login`, to login.
  * `/api/redirect`, to redirect with token on leto-modelizer/leto-modelizer-admin.
