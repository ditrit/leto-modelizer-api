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
  * For all users:
    * `GET /api/users`, to get all users.
    * `GET /api/users/{login}`, to get user by login.
    * `GET /api/users/{login}/roles`, to get all roles associated to the users.
  * For all roles:
    * `GET    /api/roles`, to get all roles.
    * `POST   /api/roles`, to create a role.
    * `GET    /api/roles/[ROLE_ID]`, to get role by id.
    * `PUT    /api/roles/[ROLE_ID]`, to update a role.
    * `DELETE /api/roles/[ROLE_ID]`, to delete a role.
    * `GET    /api/roles/[ROLE_ID]/users`, to get all users associated to the role.
    * `POST   /api/roles/[ROLE_ID]/users`, to associate role and user.
    * `DELETE /api/roles/[ROLE_ID]/users/[USER_LOGIN]`, to dissociate role and user.
  * `/api/login`, to login.
  * `/api/redirect`, to redirect with token on leto-modelizer/leto-modelizer-admin.
