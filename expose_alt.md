Ein Exposé ist eine Beschreibung Ihres Vorhabens und umfasst ca. 3-5 DIN A4 Seiten.Gliederungsbestandteile sind beispielhaft wie folgt aufgeführt:
- Titel des Vorhabens
- Motivation
- (vorläufige) Problemstellung und Zielsetzung4
- Methoden und Vorgehen
- Erfordernisse und Randbedingungen
- Erwartete Ergebnisse/Artefakte (z.B. Prototyp, Code-Dokumentation, ...)
- Zeitplan mit Meilensteinen
- Grob-Gliederung in erster Ausbaustufe
- Quellenverzeichnim

# Exposé zur Bachelorarbeit
## (SubHeading) Analyse von Heizungs-Messdaten zur automatisierten Planung von Effizienzverbesserungen
# Analyse und Ticket-Erstellung basierend auf Messdaten
## Motivation
Der Klimawandel bringt diverse Herausforderungen mit sich, insbesondere das die Erfordernis Emissionen einzusparen mit sich. Auf diese Herausforderung hat sich die Effiziente Wärme- und Stromlieferung GmbH (EWUS) spezialisiert. Diesem Unternehmen liegen diverse Messdaten und Kennwerte von Heizungs-Anlagen vor. Die Daten werden derzeit zu großen Teilen manuell analysiert, um mögliche Verbesserungen und die Wirksamkeit bereits vorgenommener Einstellungen zu ermittetln. 
Die Arbeit beschäftigt sich mit der automatisierten Analyse und Auswertung dieser Messdaten und Kennwerte. Es sollen automatisch Tickets für Anlagenanpassungen und Visualisierungen der Kennwerte generiert werden. Des weiteren sollen im Rahmen der Arbeit anonymisierte Protokolle der Messwerte und Kennzahlen generiert werden, welche der Öffentlichkeit zur Verfügung gestellt werden. 

<!-- ## (Vorläufige) Problemstellung und Zielsetzung -->
## Zielsetzung und Grenzen
### Ausgangssituation
In den zwei firmeninternen Webanwendungen Energielenker und Eneffco liegen diverse Einsparzählerdaten und Kennzahlen vor. Für diese wurden bereits diverse kleine Anwendungen entwickelt um weitere Kennzahlen zu ermitteln und die Auswertung teilweise zu automatisieren oder vereinfachen. 
Insbesondere bei der Betrachtung der Nutzungsgrade und Vorlauftemperaturen zur Planung von Anlagenanpassungen werden jedoch einige automatisierbare Arbeitsschritte manuell durchgeführt.  

TODO: was macht die KI API? Kennwerte oder schlägt sie umstellungen vor, wie z.b. stelle Nachtabsenkum um auf 0-4 Uhr? 
- soll angeben wann wahrscheinlich gerade eine Nachtabsenkung aktiviert ist basierend auf den Messwerten. 
- hier fehlt noch bisschen die interpretation. Zb statt nachtabsenkung mai bis juni 20%. 
    - unter 30% keine Nachtabsenkung
    - zw 30-60% messdaten lassen keine einschätzung zu
    - über 60% nachtabsenkung aktiv

Des weiteren wäre für die Ingenieure der EWUS GmbH eine Analysegraphik der historischen Daten sehr hilfreich. Aus dieser soll hervorgehen, welche Veränderungen der Kewnnwerte (? TODO) bewirkt wurden, was dabei die Einflussgrößen waren, ob Einstellungen der Anlage geändert wurden und welche Resultate sich daraus entwickelten. 

TODO: Open Source komponente erwähnen?
- Anforderung von Förderer sog. Open source Komponente zu haben, also daten der öffentlichkeit zur verfügung zu stellen und wissenschaftlich mit den Daten arbeiten. 
- Wenn erfüllt höhere Förderbeträge
- im rahmen eines vom bmbi geförderten projektes. in dem projekt werden daten zu wissenschaftlichen zwekcen zur verfügung gestellt. Also positive sache, dass man anonymisierte 


### Zielsetzung

Ziel der Bachelorarbeit wird es sein eine Anwendung zu entwickeln, welche regelmäßig die Einsparzähler Messwerte und -Kennzahlen analysiert. Dabei sollen anhand von Grenzwerten und anderen Bedingungen, welche durch die EWUS GmbH bereitgestellt werden, Tickets für Anlagenanpassungen erstellt werden. Diese generierten Tickets sollen zunächst, bevor sie in Energielenker angelegt werden, in einer separaten Webanwendung dargestellt werden. Über diese Webanwendung sollen die Vorschläge akzeptiert, bearbeitet und gelöscht werden können.

Darüber hinaus sollen im Rahmen der Bachelorarbeit Visualisierungen ausgewählter Kennwerte generiert werden. Insbesondere sollen Nutzungsgrade, maximale-, mittlere- und minimale Vorlauftemperaturen (...? TODO) von Anlagen zu verschiedener Zeitfenster miteinander verglichen werden um die Auswirkungen von veränderten Einstellungen zu visualisieren. Die Wahl der Zeitfenster soll dabei inerhalb dynamisch anpassbarer Zeitfenstter derart geschehen, dass in den Ausgewählten Zeitfenstern eine ähnliche mittlere Temperatur vorliegt. 

TODO: wie soll die auswahl derAnlagen geschehen, für die die visualisierungs-tabelle erstellt wird?

Um die die Entwicklungen verschiedener dynamisch auswählbarer Anlagen zueinander in Bezug zu setzen soll eine geeignete Darstellungsform ermittelt und implementiert werden. 

TODO: Ermittlung der Darstellungsform als Teil der arbeit oder vorgegeben? (z.b. Darstellung von Miniaturen der Diagramme dynamisch auswählbarer Anlagen in einer Tabelle.)

TODO: Häufig gleiche Diagramme mehrfach ansehen? falls ja, evt. zwischenspeichern in Datenbank sinnvoll.


### Abgrenzung
Der Schwerpunkt der Arbeit liegt auf der Entwicklung einer Fullstack Anwendung 


Es handelt sich um eine Anwendung welche auf bereits existierenden / bzw. vom Unternehmen bereitgestellten Messwerten und Kennwerten arbeitet. 
Folgendes ist nicht teil der Arbeit:
- Komplexe Berechnungen der Kennwerte
- Tätigkeiten, die umfassende Ingenieurstechnische Einarbeitung erfordern

- KI API macht analyse, ich mache interpretation der analysierten Daten. 


## Methoden und Vorgehen
- Anforderungsanalyse
- Spring Cloud Backend oder nur Spring Boot?  
    - Microservice basierte anwendung sinnvoll? 
        - Authentifizierungs-Service?
            - 
        - Energielenker-Service
            - Daten holen/schreiben
            - Tickets schreiben

        - Eneffco-Service
        - DB-Service
        - Ticket-Service
            - vorschläge speichern, lesen, ändern, löschen
            - 
- ReactJS Frontend
    - Visualizations:
        - https://www.dunebook.com/react-chart-libraries/

## Erfordernisse und Randbedingungen
## Erwartete Ergebnise

TODO: Scopes definieren
- Datenbeschaffung
- Visualisierung
- Inteligente Zeitraum-wahl
- Anpassbare Dauer
- Cluster-bildung

## Zeitplan mit Meilensteinen
## Grob-Gliederung in erster AUsbaustufe
## Quellenverzeichnis

nur das was nur 1 woche alt ist neu berechnen und alles davor berechnen


Objekt-Ids
- eltern sind gespeichert
- um kinder zu bekommen db tabelle energielenker_sortiert

Automatische Statuserkennung - Monitoring


- skizzieren (draw.io..)
- features
    - wie cluster bilden
    - backlinks zu eneffco

- ziele formulieren (use cases fuer die einzelnen features)

Mögliche Ziele:
- detailierte Auswerteung
    - Frage: was unterschied zu Eneffco:
        - Fehlende funktionen, zb Zeitintervall nach temperatur
        - Dashboard-funktion, also übersicht mit verschiedenen anlagen. 

- Zeitraumwahl
    - start des zeitraums zb der zeitpunkt wo anlage geändert wurde. 
    - dauer 
    - voriger zeitraum kann zb auch aus gleichen jahr sein im frühling, wenn man grad im herbst ist

- zuerst auswahl der Anlage
    - dann auswahl der Maßnahme (zb. Anpassung Heizgrenze)
    --> diagramm sgtarten dann auf basis des zeitpunkts der maßnahnme
    - dann noch anpassung der default dauer von 28 tagen



- Beispielsweise nur 3 tickettypen
    - temperaturspreizung zu hoch/tief
    - sommerabschaltung nicht aktiv
    - ...

- Hat was mit verknüpfung der daten zu tun, aber nicht mit visualisierung
- Cool wenn in ticket die prognostizierte Einsparung pro Jahr ist. (Hier ist die frage oib es diese eigenschaft schon in energielenker gibt)

- zwei verschiedene draw.ios
    - visualisierungs anwendung
        - links 2 drop downs
        - rechts die 9 diagramme
        - (optional natürlich hier direkt manuelle ticketerstellung)
    - ticketübersicht
        - 
- beides basiert auf verküpfung der daten aus EL und Eneffco. Werden dann zusammengefasst und daraus ergeben sich die beiden webanwendungen. 
- Frage beide Webanwendungen oder schafft man nur eine. Was ist die priorität
- 


# Ticketsystem
- sortierung der Tickets
    - nach progrnostizierter Einsparung
        - wieviel noch in diesem jahr, wieviel in folgejahr
- Abfrage
- Wenn er die werte schon erstellt wär es einfach. 

- Scopes definieren
    - Scope 1 innerhalb erster 2 wochen
        - daten liegen vor
    - Scope 2 diagramme erstellen 1 Woche
    - ...
    - wenn dann Scope 1 nach 3 wochen nciht geschafft, fällt scope 3 weg
    - Optionale scopes, die in Ausblick kommen. Generell alles was ich nicht schaffe in Ausblick. (zb zeitdauer des Intervalls)

--> TODO: Tobias nach Prioriserung fragen
- den dingen Namen geben
    - Ticketliste
    - Analysegraphiken...

- Datenquelle EL
- Datenquelle Eneffco
- Webanwendung Datenvisualisieung
- Webanwendung Ticketsystem
- Datenbank Ticketsystem
- 


Ausblick
- Webanwendung --> Authentifizierung


- Ziel besprechen von Ticketsystem und Visualisierung
- Scopes definieren








# Besprechung mit Tobias
- User Stories/Ziele hinter den verschiedenen Features
- Priorisierung der Features
- Ticketsystem vs Visualisierungsanwendung
    - Komplexität Ticketsystem
        - Kennwerte bereits abrufbar und Bedingungen vorgegeben
            - dort keine Fachliche einarbeitung 
    - Featureumfang Visualisierungen

- Anlagenspezifische Visualisierungen:
    - Pro diagramm: Aktueller zeitraum, Vergleichbarer Zeitraum vor Umstellung, Cluster durchschnitt
        - TODO: genaue Vorstellungen der Zeitraum-wahl klären. bisherige Auffassung: aktuellen zeitraum fest definieren und dann gleichlangen Zeitraum vor gewissen Zeitpunkt wählen, bei dem durchschnittstemperatur ähnlich ist. 
    - Diagramme für einzelne Anlage im vergleich mit Cluster
    - Cluster bildung:
        - gleicher Anlagentyp (zb nur Gasanlagen, oder nur BHKW, Erdgas-Brennkesses) 
        - Anlagengröße, also Anschlussleistung in KWH
        - alternativ anhand von PLZ/Ort
        - Alternativ: exakt gleiche anlagen (gleicher Anlagentyp und hersteller) 
    - hier Mölichkeit zur Ticket-Erstellung?
    - Backlinks zu EL, Eneffco ?

- Anlagenübergreifende Visualisierungen
    - Nutzungsgrade/... verschiedener anlagen in Balkendiagramm mit Färbung der Balken (grün = guter nutztungsgrad, rot = schlechter Nutzungsgrad, stufen dazwischen...)
    - Evt. manuelle Auswahl zweier Anlagen, welche dann in den Graphen analog zur Anlagenspezifischen Visualisierung dargestellt werden.
    - Dashboard welches diagramme megrerer Abkageb anzeigt anhand von:
        - Auswahl einer Menge von Anlagen
        - Eines Zeitraums
        - Eines Kennwertes (zb Nutzungsgrad)
    - Heatmap verschiedene Anlagen, dass zb rot bei einer anklage sieht und das anklicken kann

- Feature-Umfang Ticketsystem
    - Anhand bereits verfügbaren Kennwerten und Bedinungen Ticket-Vorschläge generieren
    - Tickets in EL speichern und vorschlag als erstellt markieren
    - Ticket-Vorschläge bearbeiten
    - Ticket-Vorschläge löschen und absichern, dass sie nicht erneut vorgeschlagen werden
    - Tickets sortieren
        - Anhand prognostizierter Einsparungen / prognostizierter Einsparungen abzüglich Kosten
            - in diesem jahr, im nächsten Jahr,...
        - ...?
    - Alle Kennwerte verfügbar vs in Anwendung berechnen? 
    - Ausgefallene Anlagen 
    - Bei analyse: anstelle von tickets erstellen textbausteine erstellen
        - entweder Textbausteine in EL schreiben
        - direkt in 


- Niemand bei Bafa erreicht. Die können einem nicht helfen. Man muss einreichen indem man begründet,  was der wissenschaftliche mehrwert ist und die schauen sich dann begründung an ob ok oder nicht.
- was hilft uns am meisten weiter?
    - Anlagenoptimierung
        - Vor hintergrund: wie kann man da begründiung für badfa aufbauen?
            - (hilft uns auch ziemlich weiter) Automatische Anlagenanalyse
                -  System bauen, was Daten aus Eneffco und paar Stammdaten und so aus EL zieht. 
                    - berechnete Datenpunkte aus Eneffco
                - Oberflächse die sich diese berechneten Datenpunkten rauszieht.
                    - zb der Wert XY liegt im bereich 5-15, dann erstelle folgenden Textbaustein und schreibe ihn bei Energielenker in folgende Zelle..
                - Man kriegt anlagenliste, kann auswählen welche man automatisch analysiert haben möchte. 
                - Dann textbausteine vordefiniert 
                - gucken uns daten an, abh in welchem bereichen die liegen Textbausteine erstellen.
                - Nächste ausbauschritte wären dann es garnicht in EL zu schreiben, sondern gleich PDF protokoll erstellen. 
                    - oder überwachung bauen, die einmal im monat rüber laufen, und dann rausfiltern welche werte sich maßgeblich verändert haben. 
                - 
    - Temperaturspreizung = Vorlauf-Rücklauf
    - Resultat in einem einzelnen Feld in EL schreiben\
        - Variante 2: Daraus direkt nen Protokoll erstellen. Das solll enthalten:
            - 
            - zusätzlich werte von Regelparametern_ist und Regelparametern_soll. 
            - Diagramme aus eneffco
            - Bemerkung: textfelder aus meiner Anwendung
    - Komplexität:
        - Intelligenz der Berechung in Eneffco-Datenpunkt. 
        - Textbaustein ergbit sich aus von-bis spanne möglicher werte (z.b. Temperaturspreizung zw 5-10Grad), welche in Eneffco abgefragt wewrden können
            - dort können noch weitere Variablen einfließén. Z.b. ein Datenpunkt : Maximum-vorlauftemperatur soll (z.b. fix 60 Grad). Daraus dann zb Textbaustein. Bekommen zb von einem Datenpunkt zurück der Ist_VL im vgl zu soll_VL zurückgibt, zb 112%. Daraus dann Textstein: der Vorlauf ist mit 120% zu hoch, sollte maximal 60 Grad sein. 
                - oder einfacher vorlauftemperatur liegt über der soll temperatur
        
    - Anfangen mit einer manuellen Analyse (statt regelmäßig).Also nicht extra für jede Anlage sondern global für alle. 
        - Auswahl der Anlagen (zb alle anlagen von Systeo, oder einzeln,...)
        - Zu analysierende Felder auswählbar in globalen Grundeinstellungen
        - Für einzelne Anlagen im Zweifel spezifische Analyseziele entfernen 
            - Wenn man sich einmal anlage angeschat hat und dann sieht bei Heizkurve kommt nur mist raus. Weil zb Installation auf bestimmte weise, dass nicht möglich. 

- Reinschreiben priorität 1
- Protocoll priorität 2
- Webanwendung:
    - Administrationsebene
        - welche analysen
        - welche datenpunkte dafür gewählt
        - welche Textbausteine wählen
        - für welchen Bereich...
    - Arbeitsebene
        - tatsächslich Anlagen auswählen, die dann Analysiert werden
        - Analyseergebnisse (generierte Texte) entweder daneben darstellen oder excel/csv datei
            - die werden in EL reingeschrieben, aber man soll vorher nochmal drüber schauen, was drinnen steht
                - z.b. Tabelle Anlagen | Textbausteine | Fehler..
        -   - oder in weboberfläche 
                - in bildschirmbreite 6 Spalten: Anlage 5 Kommentare
                - zeilen: anlagen
                    - entweder zb 5 anlagen oder wie bei systeno 500, also am besten mit scrollen

- Dieses Thema, weil er sich Erklärung für Bafa gut vorstellen kannn.
- Bei beschreibung der Maßnahmen in BA: was bedeuten die werte die man herausbekommt. Der großteil der Text (Aufabue, vorgehen, technologien) nicht fuer bafa relevant

- Anlagenüberwachen über grenzwertüberwachung in ENeffco daraus email generieren. Alternativ gleich im Protokoll woanders mit hin: "Wahrscheinlich wurde die Anlage am " + Datum aus Eneffco + " umgestellt"

- mithile von meinme neuen Tool die Anlagen analysiern. Die Hoffnung, dass man nicht für 20 Anlagen die Anlagen nochmal anschauen muss ob es überschrieben werden muss. 
    - daher wichtig, dass man analyse maßnahmen hinzufügen kann. 













## Features

- Adminoberfläche: globale Einstellungen
    - Welche Analysen 
        - Global und Anlagenspezifisch (TODO: in Admin- oder Arbeitsoberfläche?)
        - z.b. Für jeden Analyseteil TreeSelect der Anlagen für die er ausgeführt werden soll
        - zb (und/oder) für jede Anlage (/definierbare liste von Anlagen) Select der Analyseteile
            - bei von Anlagen sinnvoll für jede analyse Buttons Aktivieren/Deaktivieren (anstelle von Select, um nur was gewynscht ist zu überschreiben)
    - Welche Datenpunkte dafür gewählt werden
    - Welche Textbausteine für welche Wertebereiche
- Arbeitsoberfläche
    - Auswahl zu analysierender Anlagen
        - Antd TreeSelect: Multiple and checkable
            - Firmen (zb STO (Systeno))
                - direkt Liegenschaften oder alternativ gruppiert (z.b. STO.0xx, STO.1xx, STO.01x)
                    - TODO was hiervon sinnvoll?
    - Analyse/Auswertung auslösen
        - Holt aus Eneffco Werte und aus Energielenker Stammdaten und Werte
            - TODO: ist logik/Zeitraum für abrufen der Werte von Eneffco notwendig? 
                - Wenn ja: wie ist diese und muss sie von Benutzer angegeben werden
        - Teilmengen der Wertebereiche verschiedener Variablen auf entsprechende Textbausteine mappen
            - zb. 
                - Nachtabsenkung unter 33% für Zeitraum xy --> Es liegt keine Nachtabsenkung für den Zeitraum xy vor;
                - Nachtabsenkung zw. 33% und 66% --> Es kann basierend auf den Messwerten für Zeitraum xy keine Aussage zur Nachtabsenkung getroffen werden
                - Nachtabsenkung über 66% --> Es liegt eine Nachtabsenkung im Zeitraum xy vor
            - zb Temperaturspreizung (VL - RL)
            - zb Verhältnis Ist von Soll Maximum-Vorlauftemperatur (und VL_max_soll, VL_max_ist)
                - z.b. Verhältnis: 112%, VL_max_soll: 60, VL_max_ist: 67,2 --> `Der Vorlauf ist mit 67.2 Grad zu hoch, überschreitet Soll-Wert von 60 Grad mit Verhältnis von 112%`

    - Analyseergebnisse anzeigen (als Editierbare Textfelder)
        - Für die Anlagen untereinander (und paginiert), damit für wenige (zb 5) und viele (zb 400) benutzbar
    - Fehler anzeigen 
        - zb Bereich der Anlage / des vom Fehler betroffenen Anzeigebereichs rot markieren und mit Button "Fehler anzeigen" Fehlermeldung ausgeben
    - Analyseergebnisse bestätigen und dann in EL speichern
        - Zb mittels Haken an Einzelnen Feldern, sowie an Anlage (check = alle unterfelder, uncheck = kein unterfeld)
        - initial alle auswählen/keins auswählen mittels switch ändern. 
            - Default in probephase keins, später alle
        - TODO: Tobias fragen: Soll signalisiert werden wenn in betroffenem Feld bereits wert steht?
            - zb Icon bei/Farbige hinterlegung von Checkbox oder sogar nachfrage

    - Neben einzelnen Analyseergebnisse Möglichkeit diese Analyse für die betroffene Anlage permanent zu deaktivieren3
        - TODO: usability check

- In DB schreiben welche Analyseergebnisse für welche Anlagen akzeptiert und welche abgelehnt/bearbeitet wurden für zukünftiges automatisches Schreiben nach EL

### Ausblick / Optionale Features
- Ausblick: anstatt Analyseergebnbisse in EL zu schreiben ein Protokoll generieren
    - Stammdaten aus Energielenker
    - Diagramme aus Eneffco
    - unten: Textbausteine im Bereich Bemerkung zusammengefügt.
- Ausblick: Anlagenüberwachung, die 1x/Monat analysiert und dann herausfiltert welche Werte sich maßgeblich verändert haben
- TODO: Tobias nach folgenden Ausblicks-Vorschlägen fragen:
    - Authentifizierung
    - Kosten und Einsparnisse durch bestimmte Umstellungen
        - Dafür zb analysieren weilche Einsparungen durch gewisse Umstellungen erreicht wurden um diese Information dann zukünftig als Kosten/Einsparungs-Prognose zu nutzen. 
    - evt. für Erfüllung der Open Source Komponente: Die Entwicklungen der Anlagenumstellungen anonymisiert sammeln und der öffentlichkeit zur verfügung stellen

## Wissenschaftlicher Teil der Arbeit
- Ein Abschnitt in dem darauf eingegangen wird was die Werte Bedeuten, die herauskommen (TODO: woraus?)
- Vorgehen
    - Anforderungs-Erhebung (Use Cases...)
    - Anforderungsanalyse (Strukturierte Notation der Anforderungen die sich aus Use Cases ergeben und von nicht-funktionalen Anforderungen)
- Technologien
- Aufbau
