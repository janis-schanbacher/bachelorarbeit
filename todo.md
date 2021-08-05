TODO: 
- Use only one request library
- fetchliegenschaftsFields für die vom Benutzer gewählten Anlagen ausführen
- ElObject / AnalyseObject / EneffcoObject 
    - https://blog.scottlogic.com/2016/06/13/code-reuse-in-microservices-architecture.html
- AnalyseObjectController replace example from https://www.javaguides.net/2019/01/spring-boot-microsoft-sql-server-jpa-hibernate-crud-restful-api-tutorial.html
- Exception handling (abschnitt 10): https://www.javaguides.net/2019/01/spring-boot-microsoft-sql-server-jpa-hibernate-crud-restful-api-tutorial.html  
- Share Data between EL-MS, EneffcoMS and AnalyseMS
- replace usage of okhttp3

- Testing, orientiert an kbe projekt
- rückgabetypen und static für umgeschriebene Methoden überprüfen
- Move commits from Master to development


- klären ob configs in EL oder in separater db gespeichert werden sollen. Falls in EL, als 1 bool feld pro analyse oder liste.
  - alternativ in db  ewus_assets > auswertungs_configs


in analysis controller write equivalent to fillKiObjects
    - dafür geschicktes Kommunikationsformat wählen, wahrscheinlich json. Alternativ möglihkeit ElObject in AnalysisObjects zu überführen.
    - OkHttpLibrary überall verwenden, und möglichst code duplikate vermeiden


## Nachtabsenkung
Regelparameter_Soll_Werte, Zeitprogram Heizkreis
- Aktiv/nicht aktiv

- KI Wert 

## \Anlagengrößé anpassen:
- Auslastung datenpunkt bei Eneffco. Dann was is mittlere auslastung
  - Mittelwert über letztes halebs Jahr
  - (ist: leistung / maximalleistung der Anlage)
  - Dann mittlere Auslastung der Wintermonate (Dez, Jan, Feb). 


## Vor und RL Temperatur:
- Eneffco Datenpunkt Rücklauftemperatur angucken
  - Damit eneffco
  - Rohdatenpunnkte: Code: .RL, z.b. STO.001.WMZ.RL.1
  - .1 in meisten fällen, manchmal 2,3,10,11
  - Ich soll den Datenpunkt nehmen. Also z.b. ACO.001.WEZ.WMZ.RL.1
    - denn manchmal z.b. andere Einheiten in RohDP,...  
- Wenn Brennwertkessel nicht unter 55grad... Betrachten?
  - EL>Anlagentechnik>011 Brennwertkessel 1
  - EL>Anlagentechnik>021 Brennwertkessel 1
  - wenn nichts drin steht: Rücklauftemperatur zu niedrig. Kesseltyp ist nicht bekannt..
- Temperaturdifferenz --> dafü® neuer Datenpunkt in Eneffco
  - --> Text: pumpenreglung einstellen. Hydraulischer Abgleich,...
- Entweder überall berechneten Datenpunkt in Eneffco hinzufügen. (Oder selber berechnen.)
- Evt. noch Vorlauftemperatur
  - z.b. Differenz: Soll-Heizkurven-Temperatur und tatsächlicher maximaler Vorlauf-Temperatur. Wenn z.b. 5% drüber: weicht ab, ist zu groß

## Nutzungsgrad
- Datenpunkt aus Eneffco. Abh. von höhe Nutzungsgrad Aussage treffen
- - Datenpunkt: ACO.001.WEZ.ETA.1 (Nutzungsgrad pro zeitpunkt)
  - Daraus mittel holen
- bei Datenpunkten wird etwas berechnet wenn ich ihn abhole. 
- Timeinterval: wenn ich für nen Tag abrufe, gibt eneffco mir den mittelwert als tageswert
  - Man kann nichts dazwischen nehmen. Welche optionen es gibt siehe /timeinterval
  - also z.b. wenn ich 5 tage will 5x tag


Deployment: erstmal nur systeno

## Sommerabschaltung / Heizgrenze

125 ****


****

- Extrag Feld in Energielenker für Anlagen. Wenn werte fehlen, soll dort reingeschrieben werden was fehlt. 
- Wertebereiche und zugehörige Textbausteine in Datenbank, da rantasten wichtig ist.  
-
##