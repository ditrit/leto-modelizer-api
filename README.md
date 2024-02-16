# leto-modelizer-api

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=ditrit_leto-modelizer-api&metric=alert_status)](https://sonarcloud.io/summary/overall?id=ditrit_leto-modelizer-api)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=ditrit_leto-modelizer-api&metric=reliability_rating)](https://sonarcloud.io/summary/overall?id=ditrit_leto-modelizer-api)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=ditrit_leto-modelizer-api&metric=sqale_rating)](https://sonarcloud.io/summary/overall?id=ditrit_leto-modelizer-api)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=ditrit_leto-modelizer-api&metric=security_rating)](https://sonarcloud.io/summary/overall?id=ditrit_leto-modelizer-api)

[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=ditrit_leto-modelizer-api&metric=code_smells)](https://sonarcloud.io/summary/overall?id=ditrit_leto-modelizer-api)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=ditrit_leto-modelizer-api&metric=bugs)](https://sonarcloud.io/summary/overall?id=ditrit_leto-modelizer-api)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=ditrit_leto-modelizer-api&metric=vulnerabilities)](https://sonarcloud.io/summary/overall?id=ditrit_leto-modelizer-api)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=ditrit_leto-modelizer-api&metric=sqale_index)](https://sonarcloud.io/summary/overall?id=ditrit_leto-modelizer-api)

[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=ditrit_leto-modelizer-api&metric=ncloc)](https://sonarcloud.io/summary/overall?id=ditrit_leto-modelizer-api)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=ditrit_leto-modelizer-api&metric=coverage)](https://sonarcloud.io/summary/overall?id=ditrit_leto-modelizer-api)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=ditrit_leto-modelizer-api&metric=duplicated_lines_density)](https://sonarcloud.io/summary/overall?id=ditrit_leto-modelizer-api)

[![](https://dcbadge.vercel.app/api/server/zkKfj9gj2C?style=flat&theme=default-inverted)](https://discord.gg/zkKfj9gj2C)

Backend for leto-modelizer to manage libraries and access rights.

## Features

### Manage permissions

A permission is linked to an action that a user is allowed (or not) to perform.
It translates into a specific system role that can't be deleted nor modified by administrators, so it can only be
activated or deactivated for users, groups and roles.

List of permissions:

- `admin`: allows user to access and use `leto-modelizer-admin`.
- `create_project`: allows user to create a project in `leto-modelizer`.
- `create_project_from_template`: allows user to create a project from template in `leto-modelizer`.
- `create_diagram`: allows user to create a diagram in `leto-modelizer`.
- `create_diagram_from_template`: allows user to create a diagram from template in `leto-modelizer`.
- `delete_diagram`: allows user to delete a diagram in `leto-modelizer`.
- `create_component`: allows user to create a component in `leto-modelizer`.
- `create_component_from_template`: allows user to create a component from template in `leto-modelizer`.
- `delete_component`: allows user to delete a component in `leto-modelizer`.
- `use_text_editor`: allows user to use the text editor in `leto-modelizer`.

## Getting Started

### Requirements

- java - v21
- gradle - v8.5
- postgres - v16

This server is based on [Spring boot](https://spring.io/projects/spring-boot/)
and [Jersey](https://eclipse-ee4j.github.io/jersey/).

For all information related to them, please refer to documentation.

### Installation

To get started with __leto-modelizer-api__ using Docker, follow these steps:

#### 1. Install Docker

If you don't have Docker installed on your system, you can download and install it from the official website for your
operating system:

* [Docker Desktop for Windows](https://docs.docker.com/desktop/install/windows-install/)
* [Docker Desktop for macOS](https://docs.docker.com/desktop/install/mac-install/)
* [Docker Engine for Linux](https://docs.docker.com/desktop/install/linux-install/)

#### 2. Clone the repository

Open your terminal or command prompt and clone the project repository to your machine:

```bash
git clone git@github.com:ditrit/leto-modelizer-api.git
cd leto-modelizer-api
```

### Generate certificate for HTTPS

To generate certificate, run this command at the root of your project folder:

```shell
keytool -genkey -alias myKeyAlias -keyalg RSA -keysize 2048 -keystore src/main/resources/keystore.jks -validity 3650
```

### Run server

#### 1. Build and Run with Docker Compose

Run the following command to build the Docker containers and start the server:

```shell
docker build -t leto-modelizer-api .
 
docker-compose up -d
```

This command will build leto-modelizer-api set up the server with the specified configuration.

#### 2. Verify Installation

Once the containers are up and running, you can access your server at the defined
SERVER_URL (https://localhost:8443/api/actuator/health in this example).

#### 3. Clean Up

To stop and remove the Docker containers when you're done using the project, run:

```shell
docker-compose down
```

#### 4. Launch the dashboard (optional)

In order to directly interact with the DB or the API, you can use the
dashboard [Leto-Modelizer-Admin](https://github.com/ditrit/leto-modelizer-admin).

### OAuth Authentication Setup

To configure OAuth authentication for Leto-modelizer, you need to set up an OAuth application with specific details.
Follow the steps below to ensure proper configuration:

#### Step-by-Step Guide

1. Create an OAuth App:

* Visit the [GitHub tutorial](https://docs.github.com/en/apps/oauth-apps/building-oauth-apps/creating-an-oauth-app) on
  creating an OAuth app for detailed instructions.
* Navigate to the OAuth apps settings in your GitHub account.

2. Configure Your OAuth App:

* When setting up your OAuth app, use the following configuration details:
    * Application Name: `Leto-modelizer-api`
      This is the name that will be displayed to users during the OAuth flow.
    * Homepage URL: `https://localhost:8443/`
      Replace `https://localhost:8443/` with the URL of your hosted application if it's not running locally.
    * Authorization Callback URL: `https://localhost:8443/api/`
      Similarly, update this URL if your application is hosted at a different address.

#### Important Notes

* The URLs provided are for a local development environment. Ensure you replace them with your actual application URLs
  in a production setup.
* The Authorization Callback URL should point to the specific API endpoint in your application that handles OAuth
  callbacks.

By following these guidelines, you will set up OAuth authentication correctly for the Leto-modelizer application,
enabling secure and streamlined user authentication.

### Configuration

| Variable                            | Required                                                   | Description                                                                                                                                 |
|-------------------------------------|------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------|
| DATABASE_HOST                       | No, default: `localhost`                                   | A configuration parameter that specifies the hostname or IP address of the server where the database is hosted.                             |
| DATABASE_PORT                       | No, default: `5432`                                        | A configuration parameter that defines the port number on which the database server is listening for connections.                           |
| DATABASE_NAME                       | No, default: `leto_db`                                     | A configuration parameter that specifies the name of the specific database to be accessed on the server.                                    |
| DATABASE_USER                       | No, default: `leto_admin`                                  | A configuration parameter indicating the username used to authenticate with the database server.                                            |
| DATABASE_PASSWORD                   | No, default: `password`                                    | A configuration parameter that specifies the password for authenticating the designated user with the database server.                      |
| SSL_KEY_STORE                       | No, default: `classpath:keystore.jks`                      | A configuration parameter specifying the file path to the keystore containing SSL/TLS certificates and keys for secure connections.         |
| SSL_KEY_STORE_PASSWORD              | No, default: `password`                                    | A configuration parameter that defines the password for accessing the SSL key store file.                                                   |
| SSL_KEY_PASSWORD                    | No, default: `password`                                    | A configuration parameter that specifies the password for the individual SSL key within the key store.                                      |
| GITHUB_CLIENT_ID                    | Yes                                                        | A configuration parameter used to specify the client ID for OAuth2 authentication with GitHub.                                              |
| GITHUB_CLIENT_SECRET                | Yes                                                        | A configuration parameter for setting the client secret in OAuth2 authentication with GitHub.                                               |
| GITHUB_ENTERPRISE_AUTHORIZATION_URL | No, default: `https://github.com/login/oauth/authorize`    | A configuration parameter that specifies the URL for the authorization endpoint in GitHub's OAuth2 service.                                 |
| GITHUB_ENTERPRISE_TOKEN_URL         | No, default: `https://github.com/login/oauth/access_token` | A configuration parameter for defining the URL to obtain tokens from GitHub in OAuth2 authentication.                                       |
| GITHUB_ENTERPRISE_USER_INFO_URL     | No, default: `https://api.github.com/user`                 | A configuration parameter used to set the URL for retrieving user information from GitHub in OAuth2 authentication.                         |
| LETO_MODELIZER_URL                  | No, default: `http://localhost:8080/`                      | A configuration parameter to set the redirection URL on valid authentication for Leto-modelizer.                                            |
| LETO_ADMIN_URL                      | No, default: `http://localhost:9000/`                      | A configuration parameter to set the redirection URL on valid authentication for Leto-modelizer-admin.                                      |
| LIBRARY_HOST_WHITELIST              | No, default: ``                                            | A configuration parameter that defines a comma-separated list of trusted hostnames or IP addresses, allowing the host for library download. | 

> Notes: `GITHUB_ENTERPRISE_*` variables are only required on self-hosted GitHub.

### Build

Create a `[env_name].env` file in the root of the project directory to store your
configuration settings. Add the following variables to the .env file:

```makefile
# Postgres configuration
POSTGRES_DB=leto_db
POSTGRES_USER=leto_admin
POSTGRES_PASSWORD=password

# Api configuration
DATABASE_HOST=localhost
DATABASE_PORT=5432
DATABASE_NAME=leto_db
DATABASE_USER=leto_admin
DATABASE_PASSWORD=password
SSL_KEY_STORE=classpath:keystore.jks
SSL_KEY_STORE_PASSWORD=password
SSL_KEY_PASSWORD=password
GITHUB_CLIENT_ID=YOUR_CLIENT_ID
GITHUB_CLIENT_SECRET=YOUR_CLIENT_SECRET
LETO_MODELIZER_URL=http://localhost:8080/
LETO_ADMIN_URL=http://localhost:9000/
```

See Configuration section for more details.

Then run your docker compose with this command:

```shell
docker-compose --env-file [env_name].env up
```

## License

[Mozilla Public License 2.0](LICENSE)
