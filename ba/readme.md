# Webanwendung zur automatisierten Ermittlung von Optimierungs-Möglichkeiten in Hei-zungsanlagen

## Run the application:
To run the application, start all Spring Boot Services (in separate terminals) and the React application
- Run discovery Server
    ```
    cd discoveryServer
    ./gradlew bootRun
    ```
- Run Energielenker Service
    ```
    cd energielenkerService
    ./gradlew bootRun
    ```
- Run Eneffco Service
    ```
    cd eneffcoService
    ./gradlew bootRun
    ```
- Run Analysis Service
    ```
    cd analysisService
    ./gradlew bootRun
    ```

## Analysis Service
- '/analysis'