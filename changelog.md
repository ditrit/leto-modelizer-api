# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html)

## [Unreleased]

### Added

* Setup server.
* User entity definition.
* Group entity definition.
* Enable GitHub and enterprise GitHub authentication.
* Role entity definition.

### Fixed

* Fix use of masterKey by enabling it for allowed IPs.
* Fix delete the associated relation after delete Role or User, [see issue](https://github.com/ditrit/leto-modelizer-api/issues/19).
