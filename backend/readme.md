# Webanwendung zur automatisierten Ermittlung von Optimierungs-Möglichkeiten in Heizungsanlagen (Backend)

## Run the application:
To run the application, start all Spring Boot Services (in separate terminals) and the React application. Note that configuration files for each service hab to be created based on `[service]/src/main/resources/dbConfig.properties.sample`
- Run facility-service
    ```
    cd facilityService
    ./gradlew bootRun
    ```
- Run Analysis Service
    ```
    cd analysisService
    ./gradlew bootRun
    ```
