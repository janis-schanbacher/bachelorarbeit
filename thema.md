## Technologien / Skills
- Microservice basiertes Backend
- React Frontend
    - UI/UX
    - Visualisierungen
- Datenbank, daten der Heizräume

## Monitoring tool 
- Richtung co2 tool
- monitoring tool, was erleichtert die daten aus eneffco und energielenker darzustellen. Also ein uebergrifendens bild errreichen, statt nur gute darstellung von einzelnen anlagen. Bisher wird dann eine csv datei rausgezogen 
- Ebene ueber energielenker und eneffco. Wie ich schonmal mit den tickets angefangen habe. Die vernünftig strukturieren (biser exporrt notwendig fuer gute filtermöglichkeiten, zb nach status und eiknzelne felder.).
- Ebenso bei eineffco wenn man zählerstände oder spezifische verbräuche ansehen möchte muss man es bisher exportieren und dann kann man es sich in tabelle ansehen. Ist aber alles ein  wenig stückwerk bisher, sprich die massenauswertungen wären super als anwendung in entwas. 
- Plattfornm die die beiden schnittstellen zu energielenker und eneffco nutzt 
    - und eventuell abfragen angepasst werden können ohne programmierkenntnisse.
        - gewisse grundanfragen und dann neue zusätzliche abfragefelder hinzufügen. Neue abfrage durch graphische progrtammierung zb bauklötze/linien ziehen...

- bestandteile:
    - Filtern
    - Visualisierung
        - Möglichkeit wie man aus wust an daten die sinnvollen sachen rauskriegt
        - z.b. bei gestörten anlagen verknüpfung der abfragen. Also zb bei eneffco kann man liver wert von wärmemengen zähler und gaszähler, ssi level von modem. Wenn beide oder einer von beiden live werten nicht da ist (letzter wert älter als 2 wochen).   ..auf der anderen seite Energielenker, dort vlt sogar schon ein ticket angelegt. Sprich dann muss es sich keiner bei eneffco ansehen. "Gucke bei energielenker welches erklärt warum da keine daten kommen". 
            - wenn es störungen gibt wofür es kein ticket gibt: entweder auf energielenker gehen oder in webanwendung direkt ticket anlegen. 
        - in der richtung gibt es sehr viel.   
            - nutzungsgrad
                - bisher in Energielenker und Eneffco. Wäre dann andere ansicht, zb bzgl effizienz bewerten. Ziehe dafuer aus Eneffco (als DB ersatz) daten und bereitte es schön auf. Stelle zb die mit nutzungsgrad über 80 grün dar, unter 70 rot,...
            - mittlere leistung
            - fehlende werte / Monitoring
            - Jahreszählerstand rausziehen
            - 
        - Darstellung: als Tabellen. Wo man vlt direkt etw eintragen kann (z.b. durch bearbeiten knopf.) zb ticket erstellen schließen. (als weiterer Entwicklungsschritt)
Im fokus: 
- Monitoring   
    - Weil da ist es so, dass es zwar bisher einen Prozess für gibt (1x pro woche schaut sich wer die werte an und legt ggf tickets). 
    - Dazu noch dashboard mit anlagenerreichbarkeit.
        - dafür bereits in datenbank für alle ROhdatenpounkte werte speichern. (wann in woche erreichbar war). Dafür graphhik machen. Wieviele ANlagen gibt es, wann waren die erreichbar.
            - entweder als große graphik mit allen Anlagen die man aht untereinander. und dann grüne / rote bereiche. Das man sieht qwo viel rot wo viel grün. Dann auch evt. noch kundenspezifisch.
            - Einzelnen Anlangen / Zählern (wahrscheinlich auf zählerebene) status angeben
                - z.b., erreichbar, nicht erreichbar, auch warten auf kundeninfo
            - Wichtig an der stelle, dass man mnöglichst wenigh zusätzliche infos pflegen muss und möglich viel aus den vorhandenen rausholen. 

beweggrund:
- Erreichbarkeit sicherstellen
    - funktioniertt so, dass die anlagen angesehen werden (z.b. durch mails von eneffco wars nicht so für uns funktioniert, da sehr viele sachen. Wenn zb ein Gatetway ausfällt fallen 8 datenpunkte aus. wenn mehrere dann zb 60 datetnpunkte in mail aufgelistet()
- Wejnn identifiziert, dass etwas mit der anlage ist , da...
    - Fehlerarten:
        - Gatewayu ausgefallen
        - Zähler ausgefallen
        - keine plausiblen werte
- Weitere Schritte sind dann:
    - bei Gateway ausgefallen: Gateway austauschen
    - bei Gaszähler: da bekommen wir impulse. Wenn keine kommen, ist zb impulsadapter abgerissen oder so. Sind sachen die wir selbst machen können. --> wir schicken wen hin, nimm etwas für gaszähler mit
    - bei Wärmemengenzähler: Beim Kunden nachfragen. Da zb bei allen Häusern in einer Straße von Kunde die wärmemengenzähler ausfallen. Weil zb die Zähler gewechselt wurden. Sprich im Gateway die neuen Wärmemengenzähler registrieren. Oder noch sinnvoller: Kunden mail schicken: wir sehen wärmemengenzähler weg, wurde der gewechselt.
    - Keine plausiblen WerteL: individuell
- wenn dann einmal schnittstellen zu eneffco, energielenker,... da sind , simpel weitere Abfrage zu machen

- Automatische vorschläge für neue Tickets. 
    - sehen meist gleich aus:
        md tixibox ausgefallen letzte werte vom 15.3. Anlage muss angefahren werden. Dann noch feld wers angelegt hat
            - md = michael döring soll sich drum kümmern
    - System weiß:
        - wann lette daten, wann störung gekommen
        - alles weg --> gatewaystörung
        - aus datenpunktnamen weiß man ob tixifox oder nfox..
    - Vorschlag entweder akzeptieren, bearbeiten oder löschen
    - wenn man dann irgendwannmal nach zb paar wochen seht, dass einfach immer akzeptiert werden muss tickets automatisch erstellen
    - Tickets sollen in Energielenker liegen, dass es nur eine option ist und keine separate Datenbasis

- Authentifizierung 



- gibt noch baseline tool und ...
- kann bisher sowas wie ddurchhsuch alle anlagen uind gib mir alle nutzungsgrade [über 80] raus
    - geht in die richtige richtund. ist priorität 2 nach anlagenuebrwachung




Für unterwegs ist der plan ne PowerApp zusammenzubasteln. Sind da ungefähr bei 70% status. Bereits an Sharepoint angebunden. Können von DB an sharepoint schreiben. Jetzt müsste a npopch nen sharepoint produktiv aufgesetzt werden. Dann hätte die Powerapp den vorteil, dass man app hat, die nicht auf internetverbindung angewiesen ist (was in keller das problem sit). Kann bilder daraus hochladen
- bisher über whatsapp: montöre schicken bilder in whatsapp gruppe, schreiben adresse darunter. Mitarbeiter in büro hatt dann gleich alle bilder und sieht in welcher anage die waren. 
- Alternativ dafür telegram bot, der die infos aus der gruppe die infos rausholt und dann abspeichert. 




----
## Co2Tool
Wenn janosch etwas hat was bei co2tool notwenig ist wär da die priorität höher. 

----
## Machine learning

Hintergrund: Opensource Komponente muss fuer einsparzählerprojekt erfüllt werden, daten also der Wissenschaft zur verfügeung gestellt werden. und vlt auch etwas mit den Daten machen.
- Ich bin in dem fall die wissenschaft
- Eher nicht monitoring daten sondenrn messdaten. damit könnte man zb folgendes machen
    - würde auf Anlagenanalyse und -Auswertung rausgehen
    - Grundsetup gleich: Daten aus EL und Eneffco. Auf Plattform darstellen. Thema aber nicht monitoring, sondern anlagenanalyse. Sprich analysieren was haben unsere Umstellungen gebracht
        - man bildet verschiedene Kennzahlen je anlage. und vergleicht diese. 
            - man sagt zb nutzungsgrade über die Außentemperatur. Man hat also zb anlage
                Tabelle
                Anlage  |   Nutzungsgrad                 |  VL_max                        | VL_mittel                       | VL_min | ESZ_abs
                        | aktuell Entwicklung historisch | aktuell Entwicklung historisch |  aktuell Entwicklung historisch |   aktuell Entwicklung historisch |
                - Nutzungsgrad: zb für letzte 28  Tage Außentemperatur-abhängig. als diagramm y: NUtzungsgrad, x: Außentemperatur.  Dann noch für Vorjahr, auch 28 Tage, Außentemperatur-abh. Dann noch die beiden kurven vergleichen. Zb nen temperaturberech raussuchen, der bei beiden gleich ist. und daraus dann nen mittelwert oder so machen. Also kommt dann in die tabelle zb aktuell bei 85%, vorher bei 83%, also Entwicklung nach oben
                    - irgendwo noch temperatur angeben, die bei mittelwert war, z.b. 6 Grad (bei denen 85% Nutzungsgrad waren).
                    - gleiches dann bei Vorlauf_max. Um die änderungen im Vorlauf nachzuvollziehen zb ki api heizkurve verwenden. bei Einsparzähler wert von Eneffco nhemen. Z.b. Aktuell wert bei 6752k vor 1 Jahr bei 3242, Entwicklung nach oben
    - Ziel: Eine Analysegrapohic: Sehen was hat sich getan. Was sind die einflussgrösen. Haben wir da protokolle zurückbekommen und wie haben isch die einstellungen geändert. Was hat sich aus den Einstellungen entwickelt. 
    - Frontend: um strukturierte Auswertung zu machen. 
        - Dafür nochmal überlegen was mir als grpaphic tools einfälltE
    - Berechnungen können in Eneffco ausgelagert werden. Wenn zb kennzahlen viertelstündlich ausgerechnet werden sollen. Sowas kann man in Eneffco machen und ich kann mir dann das fertige ergebnis abrufen. So zb VL_min, VL_max, VL_min in eneffco. 
        - Was in eneffco nicht geh t ist vernünftige Darstellung. 
        - Such fenster gleicher temperatur,  berechne das mittel aus den letzten 28 tagen... nicht mehr  möglich in Eneffco. 
    - Inhaltlich kann tobias viel vorgeben

- wieso als gesonderte anwendung. Bei anderem weil es automatisch und kontinuierlich geschehen solll. Hier: Kann man auch mit excel tabelle machen. 
- Vielleicht kann es auf anderer Ebene verknüpft werden:
    - brauchen nicht nur tickets für ausfälle: 
        - grund des ganzen:: wollen anlagen effizienter betreiben. Dafür müssen wir wissen wann alage schlecht läuft.
        - Wir könnten die Anwendung auch so machen, dass wir die Daten die in Energielenker bereits vorhanden sind durch die ANwendung analysieren lassen (zb Nutzungsgrad, mittlere VL temperaturen oder so). Die KI-Kompponenten die es gibt (z.b. Monitoring), sollen ja in eneffco einlaufen. Bisher war der Plan, dass dafür  email generiert wird, die informiert über anlagen-umstellung. 
            - zum einen könnte man die email an die webanwendung schicken lassen, dass die der Anlage gleich zugordnet wird (dass man sieht welche anlage man sich anschauen sollte)
        - Die tickets also auch für analyse verwenden. Also informieren wenn grenzwerte der analyse anschlagen. zb dass automatisch eingetragen wird: 
            - z.b. bzgl nachtabsenkung-ki-tool: kommt bisher noch nicht auf protokoll. jemand guckt sich die resultate an. Aber es gibt noch nicht so was a la: ki tool hat rausgefunden zu 95% in den und den zeiten melden. 
            Nutzungsgrad anschauen und mit anderen anlagen vergleicehn. Oder anlagen mit schlechten Nutztungsgrad anschauen und dazu erste analysen durchführen und in die entsprechenden felder schreiben. ...in der richtung wär auch noch was sinnvolles möglich. 
        - Wennn in titel etwas mit Monitoring ist hat es wenig mit den messdaten zu tun. Automatisch Analyse mehr Sinn aus sicht des Bafas. 20% der fördersumme ca (5ct statt 7ct) hängen damit zusammen. Das Bafa hatn aber diesbzgl keine sinnvollen anforderungen um die Open Source Komponente zu erfüllen. (Daten bereit stellen, wissenschaftliche Arbeit, joa, sehr inkonkret)

- Würde noch mehr auf das produkt abziehlen, aber kann man nicht schön verkaufen. A la wuir brauchen schlanken prozess weil so häufig anlagen ausfallen. Sondern guckty mal hier wir haben schöne plattform die aus Analyseergebnissen der KI vorschläge macht, die man abhakt, und dann protokoll erstellen. 

- Sehr ähnlich: lies werte aus, mach basieren darauf bestimmte aktionen, zeige sachen an. Erweiterbar (statt ticket anlegen dann bei EL feld was reinschreiben wenn werte in bereich).  



---
dazu am rande zu open source:
- wir liefern anonymisierte messwerte und paar stammdaten (größé, fläche), PLZ (oder auch nur Bundesland) 




- triggers,

- ticket löschen: für die nächsten 30 tage oder so, dass man nicht tausende tickets oder so hat die dauerhaft und blt mehrfacht gelöscht werden
    - nicht zu sensitiv
- Call to action: löschen, erstellen, bearbeiten


- Auswahl der tage anhand von temperatur schweirig in eneffco. 


- oben eine anlage auswählen und dann habe ich fuer diese zb 9 diagramme. In denen jew. Nutzungsgrad/... für letzte 28 tage eine linie, dann vor einem Jahr für 28 tage,...
    - standardabweichung darf nicht zu hoch sein

- dann och cluster linie: zum vergleichen
    - cluster (durchschnitt von allen anderen in cluster) wird definiert durch: 
        - gleicher Anlagentyp (zb nur Gasanlagen, oder nur BHKW, Erdgas-Brennkesses)
        - Anlagengröße, also Anschlussleistung in KW
    - man könnte cluster auch selber auswählen: 
        - anhand von Postleitzahl / Ort
    - Teilweise gibts auch exakt gleiche anlagen (gleicher anschluss und hersteller), die dann gut zum vergleichen sind (Daten zum Anlagentyp,... in Energielenker. Werte die geliefert in Eneffco)

Also je diagram Cluster-Vergleichslinie, weitere Vergangenheit, derzeit


Hauptziel: (Erdgas-) Anlagen zu optimieren.
- wie: durch einzelmaßnahmen
- möchte wissen:
    - wie teuer (anfahrt, Materialkosten)
    - wieviel spart es ein
        - das am liebsten aus historie 
            - diesen prozentualen Einsparwert (von anderer Anlage) als vergleich sehen
        

## CO2Tool alternative ohne MS lösung

