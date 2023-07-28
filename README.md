# leto-modelizer-api

Backend for leto-modelizer to manage libraries and access rights.

## Features
 
To complete.

## Getting Started

### Requirements

- node - v18.14
- npm - v8.19.3

This server is based on [Parse Server](https://docs.parseplatform.org/parse-server/guide/).

For all information related to Parse Server, please refer to its documentation.

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

| Variable     | Required                                 | Example                                               | Description                                                                                                                              |
|--------------|------------------------------------------|-------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------|
| DATABASE_URI | Yes                                      | postgres://leto_admin:password@localhost:5432/leto_db | Represents a connection string used to connect to a database.                                                                            |
| APP_ID       | No, default: `leto-modelizer-api`        | leto-modelizer-api                                    | A unique identifier for your application, used to distinguish it when interacting with the Parse backend.                                |
| MASTER_KEY   | Yes                                      | mySuperSecretMasterKey123                             | A special access key that grants unrestricted access to the Parse backend, allowing administrative-level operations and data management. |
| SERVER_URL   | No, default: `http://localhost:1337/api` | http://localhost:1337/api                             | That defines the URL where the server is hosted, enabling clients to connect and interact with the Parse backend.                        |
| PARSE_MOUNT  | No, default: `/api`                      | /api                                                  | That specifies the mount path where the Parse middleware should be deployed in your Node.js application.                                 |
| PORT         | No, default: `1337`                      | 1337                                                  | The network port number on which a server process listens for incoming connections from clients.                                         |
| NODE_ENV     | Yes                                      | prod                                                  | Indicate the name of your .env file you want to use.                                                                                     |

### Build

Create a `[env_name].env` file in the root of the project directory to store your 
configuration settings. Add the following variables to the .env file:

```makefile
DATABASE_URI=postgres://leto_admin:password@localhost:5432/leto_db
APP_ID=my_app_id
MASTER_KEY=my_super_secret_master_key
SERVER_URL=http://localhost:1337/api
PARSE_MOUNT=/api
PORT=1337
```

See Configuration section for more details.

Then build your docker image.

```shell
docker build --build-arg NODE_ENV=[env_name] -t leto-modelizer-api .
```

Replace my_app_id and my_super_secret_master_key with appropriate values for your Parse Server setup.

## License

[Mozilla Public License 2.0](LICENSE)
