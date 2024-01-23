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
    * `/api/user/me`, to get all its information.
    * `/api/user/me/permissions`, to get all its permissions.
    * `/api/user/me/picture`, to get its picture.
  * `/api/login`, to login.
  * `/api/redirect`, to redirect with token on leto-modelizer/leto-modelizer-admin.
