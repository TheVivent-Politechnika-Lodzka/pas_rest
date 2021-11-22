# PAS REST API

projekt stworzony w środowisku Intellij IDEA

## Po sklonowaniu

aby móc uruchomić projekt, należy dodać konfigurację startową. W tym celu:

1. klikamy na `add configuration` w prawym górnym rogu przy przycisku budowania
2. dodajemy konfigurację `Payara Server` -> `Local`
3. upewniamy się, że wybrana wersja Javy to 11. Intellij pozwala na pobranie dowolnej wersji dla danej konfiguracji.
4. Może krzyczyć o brak `war` czy coś takiego, ale nas to nie interesuje

## Zapamiętać

- na koniec pracy ZAWSZE pushuj projekt
- w sumie to nie zapominaj o commitach ;)

## Nasz model

składa się z 3 klas:

- `User` - reprezentuje użytkownika (użytkownik)
- `HotelRoom` - reprezentuje pokój hotelowy (zasób)
- `Reservation` - reprezentuje rezerwację (alokacja)