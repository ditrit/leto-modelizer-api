# leto-modelizer-api

Backend for leto-modelizer to manage libraries and access rights.

## Features
 
### Conditional features

A conditional feature (abbreviated CF) is linked to an action that a user is allowed (or not) to perform.
It translates into a specific system role that can't be deleted nor modified by administrators, so it can only be activated or deactivated for users.
The CF are created according to this pattern: `CF_[action]`, where `action` is written in camelCase.

List of conditional features:

- `CF_createProject`: allows user to create a project in `leto-modelizer`.
- `CF_createProjectFromTemplate`: allows user to create a project from template in `leto-modelizer`.
- `CF_createDiagram`: allows user to create a diagram in `leto-modelizer`.
- `CF_createDiagramFromTemplate`: allows user to create a diagram from template in `leto-modelizer`.
- `CF_deleteDiagram`: allows user to delete a diagram in `leto-modelizer`.
- `CF_createComponent`: allows user to create a component in `leto-modelizer`.
- `CF_createComponentFromTemplate`: allows user to create a component from template in `leto-modelizer`.
- `CF_deleteComponent`: allows user to delete a component in `leto-modelizer`.
- `CF_useTextEditor`: allows user to use the text editor in `leto-modelizer`.

## Getting Started

### Requirements

- node - v18.14
- npm - v8.19.3

This server is based on [Parse Server](https://docs.parseplatform.org/parse-server/guide/).

For all information related to Parse Server, please refer to its documentation.

### Authentication

For this version authentication is only enable with public or enterprise GitHub.

### Installation

To get started with __leto-modelizer-api__ using Docker, follow these steps:

#### 1. Install Docker

If you don't have Docker installed on your system, you can download and install it from the official website for your operating system:

* [Docker Desktop for Windows](https://docs.docker.com/desktop/install/windows-install/)
* [Docker Desktop for macOS](https://docs.docker.com/desktop/install/mac-install/)
* [Docker Engine for Linux](https://docs.docker.com/desktop/install/linux-install/)

#### 2. Clone the repository

Open your terminal or command prompt and clone the project repository to your machine:

```bash
git clone git@github.com:ditrit/leto-modelizer-api.git
cd leto-modelizer-api
```

### Run server

#### 1. Build and Run with Docker Compose

Run the following command to build the Docker containers and start the server:

```shell
docker build --build-arg NODE_ENV=dev -t leto-modelizer-api .
 
docker-compose up -d
```

This command will build leto-modelizer-api set up the server with the specified configuration.

#### 2. Verify Installation

Once the containers are up and running, you can access your server at the defined
SERVER_URL (http://localhost:1337/api/health in this example).
You can now start using the Parse functionalities in your project.

#### 3. Clean Up

To stop and remove the Docker containers when you're done using the project, run:

```shell
docker-compose down
```

Now your project is up and running using Docker, providing a convenient and isolated environment for your backend.

### Configuration

| Variable                        | Required                                                    | Example                                               | Description                                                                                                                                         |
|---------------------------------|-------------------------------------------------------------|-------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------|
| DATABASE_URI                    | Yes                                                         | postgres://leto_admin:password@localhost:5432/leto_db | Represents a connection string used to connect to a database.                                                                                       |
| APP_ID                          | No, default: `leto-modelizer-api`                           | leto-modelizer-api                                    | A unique identifier for your application, used to distinguish it when interacting with the Parse backend.                                           |
| MASTER_KEY                      | Yes                                                         | mySuperSecretMasterKey123                             | A special access key that grants unrestricted access to the Parse backend, allowing administrative-level operations and data management.            |
| MASTER_KEY_IPS                  | No, default: [], no ip allowed                              | 0.0.0.0/0                                             | Restrict masterKey to be used by only these ips. 0.0.0.0/0 enables any ip, not recommended for production.                                          |
| OAUTH_APP_CLIENT_ID             | Yes                                                         | 0a000a000a0a0000a                                     | The ID of the OAuth App you created on your own GitHub/Enterprise GitHub dashboard.                                                                 |
| OAUTH_APP_CLIENT_SECRET         | Yes                                                         | 0a000a000a00aa0aaa0a0000a                             | A secret token generated on the OAuth App you created on your own GitHub/Enterprise GitHub dashboard.                                               |
| OAUTH_IDENTITY_REQUEST_ENDPOINT | No if GITHUB_OAUTH_TYPE=public, otherwise yes.              | https://github.com/login/oauth/authorize?client_id=id | The endpoint where the user authenticates himself to the provider.                                                                                  |
| GITHUB_OAUTH_TYPE               | No, only enterprise/public are accepted, default is public. | enterprise                                            | Defines whether the app will use an enterprise GitHub authentication or the public one, if set to 'enterprise', the enterprise API URL is required. |
| OAUTH_APP_ACCESS_TOKEN_URL      | Yes                                                         | https://github.com/login/oauth/access_token           | The URL where the user should get his access_token from, put the GitHub one for a classic GitHub authentication.                                    |
| OAUTH_APP_API_BASE_URL          | No                                                          | https://myenterprisehost/customghapi                  | If set, the API calls will be performed at this URL, else the GitHub API will be used.                                                              |
| SERVER_URL                      | No, default: `http://localhost:1337/api`                    | http://localhost:1337/api                             | That defines the URL where the server is hosted, enabling clients to connect and interact with the Parse backend.                                   |
| PARSE_MOUNT                     | No, default: `/api`                                         | /api                                                  | That specifies the mount path where the Parse middleware should be deployed in your Node.js application.                                            |
| PORT                            | No, default: `1337`                                         | 1337                                                  | The network port number on which a server process listens for incoming connections from clients.                                                    |
| NODE_ENV                        | Yes                                                         | prod                                                  | Indicate the name of your .env file you want to use.                                                                                                |

## Github configuration

In order for the API to work properly, you need your (enterprise) github to be properly configured to use leto-modelizer-api with leto-modelizer.

Here are the steps to follow to configure it:
- Log in to your github account.
- Go to: Settings > Developper Settings > OAuth apps
- Create a new OAuth app, if needed.
- Copy the `client ID` and use it for `OAUTH_APP_CLIENT_ID` field in leto-modelizer-api.
- Generate a `client secret` (or use the existing one), copy it and use it for `OAUTH_APP_CLIENT_SECRET` field in leto-modelizer-api.
- Fill the `Homepage URL` and the `Authorization callback URL` with your leto-modelizer URL, for instance `http://localhost:8080/` for dev settings.

### Build

Create a `[env_name].env` file in the root of the project directory to store your 
configuration settings. Add the following variables to the .env file:

```makefile
DATABASE_URI=postgres://leto_admin:password@localhost:5432/leto_db
APP_ID=my_app_id
MASTER_KEY=my_super_secret_master_key
MASTER_KEY_IPS=1.1.1.1/1,2.2.2.2/2,3.3.3.3/3
OAUTH_APP_CLIENT_ID=0a000a000a0a0000a
OAUTH_APP_CLIENT_SECRET=0a000a000a00aa0aaa0a0000a
OAUTH_IDENTITY_REQUEST_ENDPOINT=https://myenterprisehost/login/oauth/authorize?client_id=0a000a000a0a0000a
GITHUB_OAUTH_TYPE=enterprise
OAUTH_APP_ACCESS_TOKEN_URL=https://myenterprisehost/login/oauth/access_token
OAUTH_APP_API_BASE_URL=https://myenterprisehost/customghapi
SERVER_URL=http://localhost:1337/api
PARSE_MOUNT=/api
PORT=1337
```

See Configuration section for more details.

Then build your docker image.

```shell
docker build --build-arg NODE_ENV=[env_name] -t leto-modelizer-api .
```

Replace `my_app_id` and `my_super_secret_master_key` with appropriate values for your Parse Server setup.

## License

[Mozilla Public License 2.0](LICENSE)
