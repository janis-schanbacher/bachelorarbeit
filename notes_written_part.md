## Klassisches (Traditionelles TODO Begriff) vs Agiles Vorgehen (Software Project Management)

## Agile Testing

Agile testing is managed differently than traditional testing.

Traditional testing happens after a build is completed. Test cases are defined early on. Most test runs are completed manually. If there are automated tests involved, those testing results are tracked manually too. Testers handle their work individually. Most importantly, traditional test plans are rarely reviewed or changed.

Agile testing happens continuously. Test cases are set based on user stories. As new user stories are added, new test cases should be added too. Testing is done in sprints, often utilizing test automation to increase test coverage. Most importantly, test plans are constantly reviewed and changed to meet changing needs.
- 


## Links to check/include
[ The Manifesto for Agile Software Development](https://en.wikipedia.org/wiki/Agile_software_development#The_Agile_Manifesto)

## Beginning of august:
- hinzukommende anforderungen (flexibeles einstellen der grenzwerte ohne programmierung)
- mehr zwischenschritte notwendig um aus den gegebenen kennwerten texrbausteine zu generieren. 






- TODO: library für Requests, die wiederverwendbar ist
- 
# Notes Process
## Juli:

- Anforderungsanalyse
- Besprechung/Entwicklung Analysen und zugehörige Textbausteine
  - Dafür unerwartet datenpunkte statt einzelnen Kennwerten auszulesen
  - Hinzu kam Anforderung die Grenzwerte für die verschiedenen Textbausteine flexibel anpassen zu können (und dem zuge wahrsch auch die Textbausteine)
- Zuerst Service für Energielenker und Eneffco getrennt. Benötigen beide Datenabnkanbindung. Da sinngemäß alle Datensbeschaffung ist kombiniert in einem Microservice

## August

- Neuer Service für Service-Analyse Configurationen
- Frontend
  - yarn
    ```
    sudo npm install --global yarn
    ```
  - Create-React-app (yarn) mit Redux template
    - https://redux.js.org/introduction/getting-started
    ```
    npx create-react-app frontend --template redux
    ```
  - Styled Components
  - Ant Design (Design Framework)
  - Redux (Local Storage)
  - Axios (Http Requests)
    ```
    yarn add axios

    ```
  - 
