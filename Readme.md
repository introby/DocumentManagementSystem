# To launch the application (Command line):

1) open Terminal
2) go to the project folder
3) run app with command:
* for UNIX:
    ```sh
    ./mvnw spring-boot:run -DPOSTGRES_HOST=localhost -DPOSTGRES_PORT=5432 -DPOSTGRES_DB=dms -DPOSTGRES_DRIVER=org.postgresql.Driver -DPOSTGRES_PASSWORD=postgres -DPOSTGRES_USERNAME=postgres -DskipTests=true
    ```
* for Windows:
    ```sh
    mvnw spring-boot:run -DPOSTGRES_HOST=localhost -DPOSTGRES_PORT=5432 -DPOSTGRES_DB=dms -DPOSTGRES_DRIVER=org.postgresql.Driver -DPOSTGRES_PASSWORD=postgres -DPOSTGRES_USERNAME=postgres -DskipTests=true
    ```

# To launch the application (IDEA):

1) go to Edit Configurations - Before launch - add first element
    ```sh
    clean install
    ```
2) set environment variables. File | Settings | Build, Execution, Deployment | Build Tools | Maven | Runner
   ```sh
   POSTGRES_HOST=localhost;POSTGRES_PORT=5432;POSTGRES_DB=dms;POSTGRES_DRIVER=org.postgresql.Driver;POSTGRES_PASSWORD=postgres;POSTGRES_USERNAME=postgres
   ```
