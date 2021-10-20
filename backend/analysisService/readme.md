# Analysis Service

Spring Boot Web Service for performing heating facility analyses. The necessary data is retrieved from the facility service. Analyses configurations are retrieved from the central database.

## Run the application:
- Create a configuration file `/src/main/resources/dbConfig.properties` based on `/src/main/resources/dbConfig.properties.sample`
- Run the application
    ```
    ./gradlew bootRun
    ```
