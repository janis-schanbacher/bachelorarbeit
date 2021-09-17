# Analysis Service

## Features
- Energielenker Daten bzgl. möglicher Anlagenoptimierungen auswerten
    - Out:
        - Results as text fragments for tickets and protocols
        - Send Results to Ticketservice
    - In:
        - User Input: Anlagen, Anlagenkonfigurationen (welche Analysen durchgeführt werden sollen)
            - JSON Array: Analgencodes from Frontend
            - Configurations from Database
        - Data from Energielenker- and Eneffco-Microservice


## TODO folgendes integrieren von Ticket Service
- RESTful Ticket Service
    - PATCH:
        - Status: Open, Accepted, Modified (and accepted), Rejected
            - Save Timestamp on change
        - Textbausteine
        - Vorige Textbausteine (current EL value)
    - GET:
        - Tabelle für Liste von Anlagencodes
        - Tabelle für Anlagencode
    - Post:
        - Tabelle für liste von Anlagencodes
        - Tabelle für Analgencode

- Connected to DB
