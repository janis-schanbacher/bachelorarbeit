# Automatische Anlagenanalyse / Analyseauswertung
TODO Janis:
- Motivation, Ausgangssituation, Zielsetzung, Abgrenzung, Methoden und Vorgehen des Exposé anschauen und gucken was (abgeändert) übernommen werden kann.
- Titelblatt
    - Titel
    ```
    (Heading) Exposé zur Bachelorarbeit
    (SubHeading) Analyse von Heizungs-Messdaten zur automatisierten Planung von Effizienzverbesserungen

    Janis Schanbacher
    ssxxxxxxx
    sxxxxxxx@htw-berlin.de
    Lehrstuhl
    HTW Berlin

    29. März 2021
    ```
- Abschnitte:
    - Methoden und Vorgehen
    - Erfordernisse und Randbedingungen
    - Erwartete Ergebnisse
    - Zeitplan mit Meilensteinen
    - Grob-Gliederung in erster Ausbaustufe
    - Quellenverzeichnis

    
## Motivation
Der Klimawandel bringt diverse Herausforderungen mit sich, insbesondere das das Erfordernis Emissionen einzusparen mit sich. Auf diese Herausforderung hat sich die Effiziente Wärme- und Stromlieferung GmbH (EWUS) spezialisiert.
Diesem Unternehmen liegen diverse Messdaten und teilweise mittels künstlicher Intelligenz berechnete Kennwerte vor. Da mittlerweile mehr als 1000 Anlagen betreut werden, ist im Bereich der Anlagenoptimierung eine Automatisierung von Arbeitsschritten, welche anderenfalls regelmäßig manuell durchgeführt werden müssen.
Die Arbeit beschäftigt sich mit der automatisierten Auswertung der Kennwerte und Analyseergebnisse um die Maßnahmenplanung zu vereinfachen. Insbesondere soll durch Generierung von Textbausteinen die Ticket- und Protokoll-Erstellung vereinfacht und schließlich in zukünftigen Weiterentwicklungen automatisiert werden.

TODO: open source komponente?

## Zielsetzung und Grenzen
### Ausgangssituation
Die EWUS betreibt mehr als 1000 Heizanlagen. Für diese liegen in den zwei firmeninternen Webanwendungen Energielenker und Eneffco Stammdaten, Messwerte und diverse Kennzahlen und Analyseergebnisse vor. 
Diese Daten werden automatisch durch vernetzte Messgeräte eingespeist und durch verschiedene Anwendungen generiert. Die Auswertung und daraus resultierende Maßnahmenplanung geschieht jedoch bisher größtenteils manuell. Aufgrund der steigenden Anzahl zu betreuender Anlagen und vieler repetitiver Auswertungsschritte, ist hier eine Automatisierung gewünscht. 
Da nicht alle der möglichen Analysen auf alle Anlagen anwendbar sind, sind entsprechende Konfigurationsmöglichkeiten erforderlich. Des Weiteren sollen die Auswertungsergebnisse zunächst manuell überprüft und gegebenenfalls bearbeitet werden, bevor sie in die zentrale Anwendung Energielenker geschrieben werden.

### Zielsetzung
In dieser Arbeit soll eine interaktive Webanwendung entwickelt werden mit welcher basierend auf den Daten der Webanwendungen Energielenker und Eneffco Textbausteine generiert werden. 
Die Anwendung soll im Wesentlichen aus einem Microservice-basiertem Backend und einem Frontend bestehen. Im Frontend sollen Benutzer in einer Admin-Oberfläche die Möglichkeit haben globale Einstellungen für die Auswertungen vorzunehmen. Insbesondere soll hier spezifiziert werden welche Auswertungen durchgeführt werden sollen, welche Datenpunkte dafür untersucht werden und, welche Textbausteine für welche Teilmengen der Wertebereiche der einzelnen Kennzahlen generiert werden sollen. 
In der Arbeits-Oberfläche der Webanwendung sollen Benutzer die Möglichkeit Anlagen auszuwählen und für diese Auswertungen auszulösen. Die Ergebnisse und eventuelle Fehler sollen dem Benutzer daraufhin als editierbare Felder angezeigt werden. Schließlich soll der Benutzer die Ergebnisse, also die generierten Textbausteine bestätigen können, sodass sie nach Energielenker geschrieben werden. 

TODO: signalisieren, wenn bereits wert in Feld steh?

TODO: folgenden Satz/Feature entfernen?
Des Weiteren soll dem Benutzer in bei der Ergebnis-Ansicht ermöglicht werden die zugehörigen Auswertungen der entsprechenden Anlage permanent zu deaktivieren. 

Damit längerfristig getestet werden kann welche Analysen für welche Anlagen zu sinnvollen Ergebnissen führen, soll in eine Datenbank geschrieben werden, welche Auswertungs-Ergebnisse akzeptiert, welche bearbeitet und welche gelöscht wurden.

Eine Analyse und Optimierung der Benutzbarkeit und User Experience ist ebenfalls geplant.
8
### Ausblick

Als Ausblick ist eine automatisierte Protokoll-Erstellung geplant, welche nicht im Rahmen der Bachelorarbeit umgesetzt wird. Dafür sollen Stammdaten aus Energielenker, Kennwerte aus Eneffco und die generierten Textbausteine in einem PDF zusammengefügt werden. Dieses Feature ist nicht Teil der Arbeit, da zuerst eine ausführliche Testphase unter Einbezug der Ingenieure erfolgen muss. 
Folgende Features sind ebenfalls für die Zukunft angedacht:
- Authentifizierung, damit die Anwendung auch außerhalb des Firmennetzwerks verwendet werden kann
- Prognose der Kosten und Einsparungen durch die ermittelten Maßnahmen
- Analyse der Entwicklungen, welche aus Anlagenumstellungen erfolgten und die Bereitstellung dieser an die Öffentlichkeit in anonymisierter Form.

### Abgrenzung
Der Schwerpunkt der Arbeit liegt auf der Entwicklung einer Fullstack Webanwendung, welche die Schnittstellen der Webanwendungen Energielenker und Eneffco verwendet, um die dort vorliegenden Daten anhand von vorgegebenen Bedingungen auszuwerten. Die Berechnung der benötigten Kennwerte, die Vorgabe der Auswertungs-Bedingungen und im allgemeinen Tätigkeiten, welche Fachwissen der Energietechnik erfordern, werden ausdrücklich vom Thema der Bachelorarbeit ausgeschlossen. 
Auch das Testen welche der Analysen für die verschiedenen Anlagen angemessen sind, wird von der Arbeit ausgeschlossen. Für dieses werden lediglich die Benutzerinteraktionen mit den Auswertungsergebnissen dokumentiert. Umgesetzte Funktionalitäten wie das Abrufen der Daten und die Stabilliät der Anwendung werden selbstverständlich getestet.
Auch das Testen Benutzbarkeit und User Experience ist Teil der Arbeit. 

## Methoden und Vorgehen
Zu Beginn wird eine Anforderungsanalyse durchgeführt.
Das Backend wird als Microserice-Basierte Anwendung mit Spring Cloud unter Verwendung des Test Driven Development (TODO) Vorgehensmodells umgesetzt. Das Frontend wird mit React, Redux und Ant Design umgesetzt. 
Zur Versionsverwaltung wird Git verwendet. Des Weiteren wird ein Kanban-Board in Trello zur Organisation der Aufgaben verwendet.

Vor Beginn der Implementierung werden die Sinnhaftigkeit der bisher angedachten Technologiewahl geprüft (und eventuell angepasst) und die Aufteilung der Anforderungen an das Backend auf die verschiedenen Microservices geplant.

Um gute Benutzbarkeit und User Experience zu gewährleisten, werden im Zuge der Entwicklung der Benutzeroberfläche Design-Prototypen erstellt und entsprechende Tests mit den zukünftigen Benutzern durchgeführt. 

Der wissenschaftliche Teil der Arbeit wird mit LaTeX geschrieben, während die Dokumentation in Form einer Markdown Readme verfasst wird. 

## Erwartete Ergebnisse
Das zentrale Ergebnis der Arbeit ist ein Prototyp der aus Backend und Frontend bestehenden oben beschriebenen Webanwendung. Die Generierung von Textbausteinen für Tickets und Protokolle ist die zentrale Funktionalität dieser Webanwendung.
Weitere Artefakte sind die zugehörigen Dokumentationen, Tests für das Backend, sowie Design-Prototypen und eine Auswertung der Benutzbarkeits- und User Experience Tests. 

Im schriftlichen Teil der Arbeit werden Vorgehen und die erzielten Ergebnisse beschrieben und reflektiert. Insbesondere wird dabei auf die Funktionsweise der Anwendung und die Kommunikation der einzelnen Komponenten eingegangen. 

## Zeitplan

---
|                | Arbeitstage                        | Arbeitswochen   | Ergebnis|
| -------------- |:----------------------------------:| :--------------:| ----|
| <b>Gesamtdauer</b> | 50  | 10 |  |
| Thematische Einarbeitung | 5  | 1 | Notizen |
| Praktische Umsetzung | 20  | 4 | Prototyp, Dokumentation, Tests, Design-Prototypen, Benutzbarkeits-Testergebnisse |
| <b>Schriftlicher Teil</b> ||| Bachelorarbeit |
| Thematische Aufbereitung | 5 | 1 ||
| Ausformulierung der Inhalte | 10 | 2 ||
| Überarbeitung und Abgabevorbereitung| 5 | 1 || 
| Puffer | 5 | 1 ||
---

## Vorläufige Grob-Gliederung
- Titelblatt
- Abstract
- Inhaltsverzeichnis
- Einführung
    - Hintergrund der Arbeit, Motivation
    - Problemstellung und Datengrundlage
    - Zielsetzung
- Grundlagen
    - Grundbegriffe
    - Entwicklungsframeworks, -Tools, und -Konzepte
        <!-- 
        - Agile Softwareentwicklung
        - Microservice--Architektur mit Spring Cloud
        - React und Redux 
        - 
        -->

- Analyse
    - Anforderungserhebung
    - Anforderungsanalyse
        - Funktionale Anforderungen
        - Nicht-Funktionale Anforderungen
- Entwurf
    - Architektur
        - Backend
        - Frontend
    - Gestaltung der Benutzeroberfläche
        - Design Prototypen
        - Optimierung der Gebrauchstauglichkeit
- Umsetzung
    - Implementierung
    - Dokumentation
    - Evaluation
        <!-- 
        - Erfüllung der Anforderungen
        - Funktionsweise des Prototypen
        - Erkenntnisse aus der Evaluation        
         -->
<!-- - Testing -->
<!-- Deployment -->
- Ergebnisse
    - Backend
        - Architektur und Kommunikation der Komponenten
        - Text-Generierung
        - Lesen und Schreiben der Daten
            - Energielenker API
            - Eneffco API
            - Datenbank
    - Frontend
    - Design-Prototypen
    - Dokumentation
- Ausblick
- Evaluation der Ergebnisse und Vorgehensweise
- Fazit
- Glossar
- Abbildungsverzeichnis
- Tabellenverzeichnis
- Quellenverzeichnis    
    
    
## Quellenverzeichnis