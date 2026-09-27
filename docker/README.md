# Local Oracle Docker Setup

Start Oracle:

```bash
ORACLE_PASSWORD=<sys-password> APP_USER_PASSWORD=<app-password> docker compose up -d
```

Oracle connection details from your host machine:

```text
Host: localhost
Port: 1521
Service name: FREEPDB1
Username: CONFIG_SERVICE
Password: value of APP_USER_PASSWORD
JDBC URL: jdbc:oracle:thin:@//localhost:1521/FREEPDB1
```

Use the connection details above from your local DB viewer, for example IntelliJ Database Tools, DBeaver, SQL Developer, or DataGrip.

Run the Spring service against local Oracle:

```bash
CONFIG_URL=jdbc:oracle:thin:@//localhost:1521/FREEPDB1 \
CONFIG_USERNAME=CONFIG_SERVICE \
DB_CONFIG_PASSWORD=<app-password> \
mvn spring-boot:run
```

The feature configuration DDL runs automatically when the Oracle volume is created. If you need to rerun initialization from scratch:

```bash
docker compose down -v
ORACLE_PASSWORD=<sys-password> APP_USER_PASSWORD=<app-password> docker compose up -d
```

Important: the init script creates objects in the `CONFIG_SERVICE` schema. Docker only runs files in `docker/oracle/init` when the Oracle data volume is created for the first time.
