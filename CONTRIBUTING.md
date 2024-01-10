# leto-modelizer-api

## Default IDE

By default, we use [IntelliJ IDEA](https://www.jetbrains.com/idea/).

### Plugins to install

To help you in your development, you can install these plugins:

1. `CheckStyle-IDEA`

To configure it, go to `Settings > Tools > Checkstyle`, then set:

- `Checkstyle version` to `10.12.5` 
- `Scan scope` to `Only java source(but not tests)`
- Add a new configuration file with:
  - `Description`: `Leto`.
  - `File`: select `checkstyle.xml` in project root folder.
  - `Use a local Checkstyle file` checked.
  - `Store relative to project location` checked.

## Helpful configuration

### Managing auto-import

To configure it, got to `Settings > Editor > General > Auto Import`, then check `Optimize imports on the fly`.

## Run application in dev mode

Setup database with docker:

```shell
docker run -p 5432:5432 --rm -ti --name postgres -e POSTGRES_USER=leto_admin -e POSTGRES_PASSWORD=password -e POSTGRES_DB=leto_db postgres:16
```

Once database is setup you can run :

```shell
./gradlew bootrun
```

> :warning: **You have to generate keystore.jks before!**, see [README](https://github.com/ditrit/leto-modelizer-api/blob/main/README.md#Generate-certificate-for-HTTPS).

## Run application tests

To run all the application tests (unit and integration), use this command:

```shell
./gradlew test -i
```

To run only the application unit tests, use this command:

```shell
./gradlew unitTest -i
```

To run all the application integration tests, use this command:

```shell
./gradlew integrationTest -i
```
## Checkstyle

Before pushing your branch and open/synchronize a pull-request, you have to verify the checkstyle of your application. Here is the command to do so:

```shell
./gradlew checkstyleMain
```

Reports can be found in:
- `build/reports/checkstyle/main.xml`

## Dependencies update

You can check update of your code dependencies by running this command:

```shell
./gradlew dependencyUpdates
```


## Generate test coverage report

You can generate test coverage report by running this command:

```shell
./gradlew jacacoTestReport
```

Reports can be found in:
- `build/JacocoHtml/index.html`
- `build/reports/jacoco/test/jacocoTestReport.xml`

## How to release

We use [Semantic Versioning](https://semver.org/spec/v2.0.0.html) as guideline for the version management.

Steps to release:
- Create a new branch labeled `release/vX.Y.Z` from the latest `main`.
- Improve the version number in `package.json`, `package-lock.json` and `changelog.md`.
- Verify the content of the `changelog.md`.
- Commit the modifications with the label `Release version X.Y.Z`.
- Create a pull request on github for this branch into `main`.
- Once the pull request validated and merged, tag the `main` branch with `vX.Y.Z`.
- After the tag is pushed, make the release on the tag in GitHub.

## Git: Default branch

The default branch is `main`. Direct commit on it is forbidden. The only way to update the application is through pull request.

Release tags are only done on the `main` branch.

## Git: Branch naming policy

`[BRANCH_TYPE]/[BRANCH_NAME]`

* `BRANCH_TYPE` is a prefix to describe the purpose of the branch. Accepted prefixes are:
  * `feature`, used for feature development
  * `bugfix`, used for bug fix
  * `improvement`, used for refacto
  * `library`, used for updating library
  * `prerelease`, used for preparing the branch for the release
  * `release`, used for releasing project
  * `hotfix`, used for applying a hotfix on main
* `BRANCH_NAME` is managed by this regex: `[a-z0-9._-]` (`_` is used as space character).
