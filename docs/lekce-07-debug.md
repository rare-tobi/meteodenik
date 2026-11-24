# Debug analýza obrazovky (Lekce 07 – Cvičení 7.6)

## Tabulka nalezených chyb

| # | Vinný řádek / místo | Proč se chyba děje | Oprava                                                                                                                                    |
|---|---|---|-------------------------------------------------------------------------------------------------------------------------------------------|
| A | var novyNazev = "" | Jde o obyčejnou lokální proměnnou Kotlinu, nikoli o stav (State) sledovaný Compose. Při zadávání znaků se hodnota v paměti mění, ale nedochází k rekompozici UI, takže pole zůstane vizuálně prázdné. Tlačítko Přidat ale čte tuto proměnnou v paměti a přidá zadaný text (např. „Otrokovice“) do seznamu, i když v poli nebyl vidět. Při následné rekompozici se funkce spustí znovu a novyNazev se resetuje zpět na "". | Použít stav přežívající rekompozici i otočení obrazovky: var novyNazev by rememberSaveable { mutableStateOf("") }                         |
| B | .padding(8.dp).background(...) | V Jetpack Compose záleží na pořadí modifikátorů (obalují se zvenku dovnitř). Zde .padding(8.dp) vytvoří nejprve vnější okraj (margin) a až uvnitř něj .background(...) vykreslí barevné pozadí těsně kolem textu. | Prohodit pořadí modifikátorů: nejprve obarvit pozadí a teprve uvnitř něj přidat vnitřní odsazení: .background(...).padding(8.dp)          |
| C | items(mesta) { ... } (chybějící key) | Bez parametru key váže LazyColumn stav položek (var rozbaleno by remember ...) na pořadový index v seznamu (0, 1, 2, 3), nikoli na konkrétní město. „Ostrava“ byla na indexu 2. Po smazání „Zlína“ (index 0) se všechny položky posunuly o jednu pozici nahoru – na index 2 se tak dostala „Olomouc“ a zdědila stav rozbaleno = true po Ostravě. | Předat unikátní stabilní klíč položky: items(items = mesta, key = { it }) { ... } (v ostré aplikaci pak jedinečné id objektu).            |
| D | Text("Název města"), Text("Přidat"), atd. | Texty jsou zapsány přímo v kódu (hardcoded strings). Nástroj Android Lint je na rozdíl od XML v Compose kódu nehlídá, ale aplikace tak nelze lokalizovat do dalších jazyků přes values-en/strings.xml. | Všechny uživatelské texty přesunout do res/values/strings.xml (a values-en/strings.xml) a v kódu je načítat přes stringResource(R.string.…). |

---

## Bonus: Architektonická vada obrazovky

I po opravě všech čtyř chyb zůstává celý kód uzavřen v jediné stavové funkci (RozbityMestaSeznam), která v sobě míchá logiku správy dat (remember, mutableStateOf) s vykreslováním uživatelského rozhraní.

Důsledek: Komponentu nelze znovupoužít s jiným zdrojem dat a nelze pro ni napsat čisté @Preview (např. prázdný seznam nebo seznam se čtyřmi konkrétními městy bez nutnosti upravovat vnitřní kód funkce).
Řešení (State Hoisting): Rozdělit obrazovku na dvě funkce:
    1. Bezstavovou (MestaObsah), která přijímá mesta, novyNazev a lambda funkce (onNazevChange, onPridat, onSmazat) jako parametry – tu pak lze snadno vložit do libovolného počtu @Preview.
    2. Stavovou (MestaObrazovka), která drží rememberSaveable / remember a předává stav dolů do bezstavové části.
