# Setting Service

Setting Service is a Spring Boot configuration service for managing application settings and feature access rules. It is built around a feature configuration model where each feature can be globally controlled and can also have user-specific blocking rules based on configurable user attributes.

The service is intended for use cases such as login controls, biometric login controls, MPIN access, notification access, and other feature toggles where access can be decided centrally from database-driven configuration.

## Main Capabilities

- Manage application-level configuration records.
- Manage feature definitions with display names, descriptions, global status, and localized messages.
- Manage user attribute definitions used by rule evaluation.
- Manage feature rules connected to features.
- Manage rule conditions with one or more allowed or blocked values.
- Evaluate user access and return feature availability as true or false values.
- Return globally blocked features without requiring a username.
- Return feature access in a graph-style response grouped by parent feature and child rules.
- Store rule condition values as a set of strings in a CLOB-backed JSON format.
- Generate OpenAPI YAML into the Maven target folder.
- Generate Kubernetes or OpenShift manifests using Eclipse JKube.
- Run locally against Oracle using Docker.

## Technology Stack

- Java 21
- Spring Boot 3.5.x
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring Actuator
- Oracle JDBC
- H2 for local/test profile support
- Lombok
- MapStruct
- Springdoc OpenAPI
- Eclipse JKube for Kubernetes and OpenShift YAML generation

## Package Structure

The main package is `com.nkhan.configuration`.

- `common`: shared constants, response models, response builders, converters, utility classes, and exception handling.
- `config`: application configuration beans.
- `controller`: REST controllers grouped by feature area.
- `mapper`: MapStruct mappers between entities and API models.
- `model`: JPA entities.
- `repository`: Spring Data repositories.
- `service`: service contracts.
- `service.imp`: service implementations.
- `src/main/jkube`: JKube resource fragments used during Kubernetes and OpenShift manifest generation.
- `src/main/resources/sql`: Oracle SQL scripts for feature configuration tables.
- `docker`: local Oracle Docker setup and initialization scripts.
- `postman`: Postman collection for the feature configuration APIs.

## Domain Model

### Feature

Represents a configurable business capability, such as login, biometric login, MPIN login, notifications, or push notifications. A feature can be globally controlled and can carry localized user-facing messages.

### User Attribute

Represents a dynamic attribute that can be used during rule evaluation. Examples include user id, role, branch, channel, or an amount field. Each attribute defines where the value comes from, such as a claim, request header, or request body.

### Feature Rule

Represents a rule attached to a feature. Rules are used to decide whether a user should be blocked from a feature. Rules support enabled status, start and end dates, priority, and deny response metadata.

### Rule Condition

Represents a condition attached to a feature rule. Multiple conditions under one rule are treated as a combined rule definition. Condition values are stored as a set of strings and persisted in JSON form.

### Application Config

Represents general application configuration values that can be created, updated, soft-deleted, permanently deleted, and fetched by type or code.

## Feature Access Evaluation

The feature access APIs are designed to answer whether a user can access each configured feature.

The intended behavior is:

- If a feature is globally blocked, the feature is not available to anyone.
- If a feature is not globally blocked, active rules are evaluated.
- If an enabled blocking rule matches the supplied user value, access is denied for that feature or child rule.
- If no blocking rule matches, access is allowed.
- The access map response returns every configured feature as a key with a true or false value.
- The graph response groups feature access into parent features and nested rule values.

## REST API Areas

The service context path is `/setting-service` and the API base path is `/api/v1`.

### Features

Base path: `/setting-service/api/v1/features`

Supported operations include:

- List all features.
- Get a feature by feature id.
- Create a feature.
- Update a feature.
- Delete a feature.
- Return blocked features for a username.
- Return globally blocked features.
- Return all features as key/value access flags for a username.
- Return feature access as a graph for a username.

### Feature Rules

Base path: `/setting-service/api/v1/feature-rules`

Supported operations include:

- List all feature rules.
- Get a feature rule by rule id.
- Create a feature rule.
- Update a feature rule.
- Delete a feature rule.

### Rule Conditions

Base path: `/setting-service/api/v1/rule-conditions`

Supported operations include:

- List all rule conditions.
- Get a rule condition by condition id.
- Create a rule condition.
- Update a rule condition.
- Update only the set of condition values.
- Delete a rule condition.

### User Attributes

Base path: `/setting-service/api/v1/user-attributes`

Supported operations include:

- List all user attributes.
- Get a user attribute by key.
- Create a user attribute.
- Update a user attribute.
- Delete a user attribute.

### Application Config

Base path: `/setting-service/api/v1/application-cfg`

Supported operations include:

- List application configuration records.
- Create an application configuration record.
- Update an application configuration record.
- Soft-delete an application configuration record.
- Permanently delete an application configuration record.
- Fetch configuration by feature type.
- Fetch configuration by feature type and code.

## Database

The service is configured for Oracle in the local profile. The local Docker setup uses Oracle Free and initializes the feature configuration schema from the SQL scripts under `docker/oracle/init`.

The main SQL script creates:

- `FEATURE`
- `USER_ATTRIBUTE`
- `FEATURE_RULE`
- `RULE_CONDITION`
- supporting sequences
- supporting indexes
- update timestamp triggers where required

The endpoint group foreign key was intentionally skipped and can be added later when the endpoint group table is available.

## Local Oracle Setup

Docker Compose is included for running only Oracle locally. The Oracle passwords are intentionally not committed. Provide them through environment variables when starting Docker Compose.

Use the details in `docker/README.md` for the local Oracle connection values and startup notes.

## Application Profiles

The project currently includes profile-specific property files for:

- `local`: local Oracle database usage.
- `h2`: lightweight runtime used for OpenAPI generation and local verification.
- `test`: test execution support.

For local Oracle, provide the database password through the `DB_CONFIG_PASSWORD` environment variable or the `db.config.password` property.

## OpenAPI and Swagger

Springdoc is configured to expose OpenAPI documentation at runtime. The Maven build can also generate `swagger.yml` into the `target` folder during the verification lifecycle.

The generated file is useful for API sharing, gateway review, and Postman or client generation workflows.

## Postman Collection

A Postman collection is included under the `postman` directory. It contains requests for the feature configuration APIs and uses collection variables such as base URL, username, feature id, rule id, and condition id.

## Docker Image

A Dockerfile is included for building a runtime image from the packaged Spring Boot jar. Build the jar first, then build the image from the project root.

The container listens on port `8080`. Runtime configuration such as Spring profile, Oracle JDBC URL, username, and password should be supplied through environment variables.

The `.dockerignore` file keeps source files, Git metadata, local documentation, Postman files, and Maven build internals out of the Docker build context while keeping the packaged jar available for the image build.

## Kubernetes and OpenShift

The project supports both Kubernetes and OpenShift manifest generation through Maven profiles.

- The `kubernetes` profile generates Kubernetes resources.
- The `openshift` profile generates OpenShift resources.
- JKube uses the image registry, namespace, artifact id, and project version to build the image reference.
- `src/main/jkube/deployment.yml` injects the active Spring profile into the generated container environment.
- `src/main/jkube/router.yml` customizes the OpenShift Route with host, path, timeout, service target, and edge TLS termination.

The generated manifests are written under `target/classes/META-INF/jkube`.

## Build and Verification

The project has been verified with Maven package builds for both Kubernetes and OpenShift profiles. The build compiles the application, packages the Spring Boot jar, and validates the generated JKube resources.

For documentation generation, the Maven verification lifecycle starts the application with the H2 profile, reads the OpenAPI YAML, writes `target/swagger.yml`, and then stops the application.

## Security Notes

- Database passwords are not committed to the repository.
- Local Oracle passwords must be provided through environment variables.
- Build output and generated artifacts are ignored by Git.
- The Postman collection uses variables instead of fixed environment-specific secrets.

## Repository

GitHub repository: `git@github.com:Nasruddinkhan/setting-service.git`

## Current Status

The service includes CRUD APIs for feature configuration, user attributes, feature rules, rule conditions, and application configuration. It also includes user feature-access evaluation endpoints, Oracle SQL scripts, Postman collection support, local Oracle Docker setup, OpenAPI generation, and Kubernetes/OpenShift manifest generation.
