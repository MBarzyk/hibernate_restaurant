Rachunek (Invoice) ma pola:
- dataDodania
- nazwaKlienta
- czyOpłacony
- dataWydania
- dataIGodzinaOpłacenia
- kwotaNaRachunku (suma wartości produktów)
- zbiór produktów (relacja bazodanowa - osobna tabela)

Produkt na fakturze (InvoicePosition/Position) ma pola:
- nazwa
- cena (netto)
- kwota podatku
- ilość (int)

Możliwości aplikacji:
Stwórz metody które pozwalają na:

- dodawanie rachunku 
- dodawanie produktów (do rachunku)
- wydaj fakturę
- ustawianie rachunku jako opłaconego (po opłaceniu rachunku nie powinna istnieć możliwość dodawania produktów)
- sprawdzanie kwoty rachunku po identyfikatorze
- listowanie produktów na rachunku
- listowanie rachunków
- listowanie wszystkich produktów
- listowanie rachunków nieopłaconych
- listowanie rachunków z ostatniego tygodnia
- wypisywanie sumy kwot rachunku z obecnego dnia.