# oebs-fullmakt-godkjenning-api
Henter fullmakt fra fullmaktregister for OeBS

## Arkitektur
- Enkel tegning av dataflyten med systemene som inngår

## Funkjsonalitet 
APIet eksponerer tre endepunkter:
- fullmakter: Henter ut alle fullmakter mot eksternt system
- internal/isready: Helsekontroll for databasen som sjekker om den er oppe 
- internal/isalive: Identiske med isready


- Hva slags data flyter gjennom systemet? 
- Gjøres det transformering av data, og i så fall hva slags transformering?

## Dependecies
- Hvilke systemer er dette systemet avhengige av?

## Hvordan kjøre lokalt
- Hvordan sette opp miljøet for å kjøre lokalt?

## Testing
- Hvordan er funksjonalitet testet? 
- Er det satt opp integrasjonstester, og hvordan kjøre disse?

## Deploy
- Hvordan skal utvikling skje?
  - Skal man lage ny branch eller tilgang til å pushe til main?
  - Skal det gjøres code review før merge til main og brancher?
  - Navngivning på branch og commit i sammenheng med oppgaven man jobber på? 
  
- Hvordan skal deploy til ulike miljøer skje? 
    - Skal det skje via main branch eller via branch for eget miljø?
    - Hvilke kriterier skal være oppfylt for å kunne deploye til neste instans?

## Dokumentasjon
- Hvordan skal dokumentasjon oppdateres og vedlikeholdes?
- Lenke til swagger hvis det finnes 
- Lenke til Confluence hvis det finnes


Tjenesten eksponerer et REST API som eksponerer et endepunkt som henter
ut fullmakter. Jeg tror det er oebs som kaller apiet også gjøres det et 

Tjenesten eksponerer tre endpunkter: 

fullmakter: Henter ut alle fullmakter mot eksternt system
internal/isready: Helsekontroll for databasen som sjekker om den er oppe 
internal/isalive:

Går mot fullmaktsregister-external-proxy og henter ut fullmakter.
Dett er et api som er hostet av tjenesten vaktor? Vet ikke hva det er? 


De restenrende propertiene er for å kunne gjøre helsesjekk for å sjekke at databasen
er oppe og går. Skjønner ikke helt hvorfor dette er nødvendig?
Databasen vil vel kalle denne tjenesten når den trenger, hvorfor skal vi ha overvåkning
av den her? Hvem som bruker det er kanskje ikke så relevnat 

Beredskapsvaktstystem som skal motta dataen fra denne tjenesten. 