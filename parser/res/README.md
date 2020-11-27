## A fuzzer-hez használt tesztfájlok

| Fájlnév | Leírás |
|--|--|
| valid.caff | Egy normál CAFF fájl, helyes adatokkal, speciális karaktereket nélkül |
| big.caff | Minden számot tartalmazó helyen a lehető legnagyobb érték van jelen |
| bad_strings.caff | A caption végén nincs \n. A tagek után nincs \0, de van benne \n és tartalmaznak speciális karaktereket (pl. ♫☻•) |
| bad_length.caff | A header-ekben nem az adatnak megfelelő méret beállítva. Animációk száma nem egyezik a headerben lévővel |

 - Mindegyik fájl 3x3 pixel-t tartalmaz