Die Anforderungen werden zuerst mittels User Stories erhoben. Zur Entwicklung des Systems werden die Anforderungen in einem Trello-Board genauer spezifiziert.


## Agile Requirements
- Agile requirements are set in user stories when they are needed.
  - Stakeholders should actively participate in the requirements gathering process. 
  - User stories should be constantly refined and prioritized.
  - Agile requirement example:
    - As a carmaker, I expect our vehicles to comply with ISO 26262 so that our customers will be safe driving them.



<!-- Introduction to SRS (System Requriements Specification) is very important. -->
## Purpose
<!-- Purpose of your product. -->
Intended Audience and Intended Use

## Product Scope

- Describe the software being specified.
- include benefits, objectives, and goals. 
  - This should relate to overall business goals, especially if teams outside of development will have access to the SRS.

## Definitions and Acronyms
- SE: Software Entwicklung
- ESZ: Einsparzähler
## risk definition
 

## Anforderungenserhebung und -Analyse im Rahmen Agiler SE
Im Rahmen des Agilen Vorhens werden die Anforderungen in einem Backlock eines Trello Boards festgehalten, und während der Entwicklung verfeinert und priorisiert. 

Dies geschieht für funktionale Requirements (TODO: Ref funktional vs nonfunktional evt) in Form von Backlog-Einträgen, welche jew. eine User Story enthalten. 
User Stories werden einheitlich nach folgendem Schema definiert: 
    Als `<Role>` möchte ich `<Ziel/Wunsch>`, damit `<Nutzen>`.`

Die Backlog-Einträge der User Stories werden ggf. um Akzeptanzkriterien ergänzt. Für eine Abschätzung des Aufwands werden für jede User Story sog. Story-Points geschätzt. Story-Points sind ein Abstraktes Maß, welches die Komplexität und somit den Gesamtaufwand einer User Story schätzt (https://www.atlassian.com/agile/project-management/estimation).

Nicht-funktionale Anforderungen in diesem Projekt wie üblich (https://masteringbusinessanalysis.com/lightning-cast-non-functional-requirements-in-agile/) je nach Komplexität festgehalten als:
    - Backlog-Item (User Story oder Technical enabler) 
      - Für sehr komplexe Anforderungen
    - Akzeptanzkriterien einer User Story
      - Für leicht verständliche nicht-funktionale Anforderungen, die sich auf eine oder wenige User Stories gelten. 
      - Bsp. User Story: Als Nutzer möchte ich ich Textbausteine für ESZ Tickets und Protokolle generieren lassen... Akezeptanzkrieterien: Textbausteine werden ausschließlich nachdem diese akzeptiert wurden nach Energielenker geschrieben.

    - Definition of Done
      - Für leicht verständliche nicht-funktionale Anforderungen, die für alle User Stories gelten.

Die funktionalen Anforderungen wurden in Abstimmung mit Tobias Pauthner, dem Teamleiter (TODO Rolle) der Abteilung erhoben. Aus den Besprechungs-Notizen wurden daraufhin User Stories, Akzeptanzkriterien und eine Definition of Done spezifiziert. Schließlich wurden diese Herrn Pauthner zur Kontrolle (TODO better wording) vorgelegt und entsprechend angepasst. 

### Backlog-Eimträge
#### Funktionale Anforderungen
<!-- Erstellung von Textbausteinen für ESZ Tickets und Protokolle basierend auf Heinzungs-Kennwerten  -->
- Als Nutzer (TODO: wording) möchte ich Textbausteine für ESZ Tickets und Protokolle generieren lassen, damit ich eine manuelle Auswertung und Erstellung der Textbausteine vermeiden kann.
  - Akzeptanzkriterien: Textbausteine werden ausschließlich nachdem diese akzeptiert wurden nach Energielenker geschrieben.
<!-- Konfiguration, welche Analysen für welche Anlagen durchgeführt werden ist über Admin-Weboberfläche möglich.  -->
- Als Nutzer (TODO: wording) möchte ich über eine Konfigurations-Oberfläche (TODO: wording) definieren welche Analysen für welche Anlagen ausgwertet werden, damit ausschließlich verwendbare Textbausteine erstellt werden. 
<!-- Auswahl der Anlagen (einzeln & gruppenweise) über Arbeit-Weboberfläche. -->
- Als Nutzer (TODO: wording) möchte ich Anlagen für die Auswertung sowohl einzeln, als auch gruppenweise in der Arbeits-Oberfläche auswählen, damit die Auswahl mit geringem Zeitaufwand geschehen kann. 
<!-- Anzeige der Analyseergebnisse, sowie eventuell bereits vorhandene Werte  -->
- Als Nutzer (TODO: wording) möchte ich sowohl die generierten Textbausteine, als auch evt. bereits vorhandene Werte aus Energielenker einsehen, damit ich diese miteinander Vergleichen kann. 
<!-- Die generierten Textbausteine können in der Arbeits-Weboberfläche akzeptiert, angepasst und verworfen werden. -->
- Als Nutzer (TODO: wording) möchte ich die generierten Textbausteine akzeptieren, anpassen, und verwerfen können, bevor Sie nach Energielenker geschrieben werden, damit keine falschen Tickets oder Protokolle erstellt werden. 
<!-- Anzeige bisheriger Werte der Energielenker-Felder in welchen die Ergebnisse nach bBstätigung gespeichert werden in Arbeits-Weboberfläche -->
- Als Nutzer (TODO: wording) möchte ich , damit . TODO: löschen oder umformulieren/verstehen
<!-- Logging der Interaktionen (akzeptieren, anpassen, verwerfen) mit den Textbausteinen -->
- Als Nutzer (TODO: wording) möchte ich einsehen können wie mit den Textbausteinen interagiert wurde (Akzeptieren, Anpassen, Verwerfen), damit diese Informationen zur Optimierung der Auswertungs-Konfigurationen und für zukünftige weitere Automatisierung verwendet werden können. 
<!-- Logging der ausgeführten Analysen -->
- Als Nutzer (TODO: wording) möchte ich einsehen können welche Auswertungen für welche Anlagen ausgeführt wurden, damit diese Informationenen für eine Überwachung der Auswertungen verwendet werden können.
<!-- Signalisierung von Fehlern der einzelnen Analysen in Ergebnisansicht -->
- Als Nutzer (TODO: wording) möchte ich in der Ergebnisansicht auf Fehler bei den Auswertungen hingewiesen werden, damit diese Information bei der Entscheidung über die Interaktion mit den texttbausteinen und möglichen Anpassungen der Datenbasis (Energielenker) oder Auswertungs-Konfigurationen einbezogen werden können. 
<!-- Verlinkung der Konfigurations-Seite einer Anlage in Ergebnisansicht -->
- Als Nutzer (TODO: wording) möchte ich in der Ergebnisansicht zu den Konfigurations-Seiten den einzelnen Anlagenen einfach navigieren können, damit die mit wenig Aufwand möglich ist. 

#### Nicht-Funktionale Anforderungen
TODO
- Als `<Rolle>` möchte ich `<Ziel/Wunsch>`, damit `<Nutzen>`.
  
### Definition of Done (TODO)
- Gebrauchstauglichkeit der Weboberfläche
- Effiziente Datenbeschaffung von Energielenker

<!-- TODO: folgende checken uynd evt hinzufügen: availability, maintainability, performance, reliability, scalability, security, and usability -->

#### non-functional Requirements in Agile development
- The most common ways of doing this are: (https://masteringbusinessanalysis.com/lightning-cast-non-functional-requirements-in-agile/)
  - with an explicit backlog item (User Story or technical enabler)
    - Bsp: As a shopper, I want the website to be available 98% of the time I try to access it so that I can make my purchase and don’t need to find somewhere else to purchase the product.
  - as Acceptance Criteria (of a user story)
    - for most complex
    - Bsp: Userstory: As a Financial Analyst, I want to see the monthly transactions for my customers so that I can advise them on their financial health.
      - Acceptance Criteria: - System displays all transactions meeting the search parameters within 10 seconds of receiving the request.
  - as part of the team’s Definition of Done.
    - für allgemein verständliche Anforderungen  
    - Think of the Definition of Done as a consistent set of Acceptance Criteria that applies to all backlog items.  It’s a comprehensive checklist indicating what “Done” looks like both in terms of functionality and non-functional quality attributes.
    - These might be requirements around accessibility, performance, security, or usability.

## Agile Projektplanung mit User Stories
- As a `<Role>` I want `goal>` so that `<benefit>`. Acceptance Criteria: ...

https://m.heise.de/developer/artikel/Use-Case-2-0-Agile-Projektplanung-mit-Use-Case-Slices-2535642.html?seite=all


## Quellen
https://www.perforce.com/blog/alm/how-write-software-requirements-specification-srs-document
    - In Agile development, you create user stories instead of traditional requirements. So, an Agile requirements document gathers user stories essential for a release. 
      - It still covers the same elements — purpose, features, release criteria, timeline
      -  in a task board or interactive document, rather than in a static document. --> Trello