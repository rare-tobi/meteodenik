# Dokumentace oprávnění: ACCESS_COARSE_LOCATION (Lekce 06)

## 1. ACCESS_COARSE_LOCATION vs. ACCESS_FINE_LOCATION

Pro aplikaci jsem zvolil oprávnění ACCESS_COARSE_LOCATION z následujících důvodů:
1. ACCESS_COARSE_LOCATION poskytuje přibližnou polohu odvozenou ze signálu mobilní sítě (BTS) a Wi-Fi s přesností přibližně na úrovni městského bloku / několika set metrů až kilometrů. Oproti tomu ACCESS_FINE_LOCATION využívá GPS a určuje polohu s přesností na jednotky metrů.
2. Pro předpověď počasí zajímá uživatele stav v daném městě či městské části. Přesnost na metry je pro počasí zbytečná.
3. Aplikace žádá pouze minimální nutná data k naplnění své funkce (zásada minimalizace údajů v Androidu a GDPR).

---

## 2. Proč se trvalé zamítnutí vyhodnocuje v callbacku a ne při startu?

Metoda ActivityCompat.shouldShowRequestPermissionRationale() vrací false ve dvou zcela odlišných situacích:
1. Uživatel o oprávnění zatím ještě nikdy nebyl požádán (čerstvě nainstalovaná aplikace).
2. Uživatel oprávnění zamítl trvale (zaškrtl Don't ask again nebo jej zamítl dvakrát po sobě).

Při startu aplikace (v onCreate / inicializaci) systém nedokáže tyto dva stavy odlišit – v obou případech checkSelfPermission hlásí neuděleno a shouldShowRequestPermissionRationale vrací false.

Teprve v callbacku výsledku žádosti (ActivityResultLauncher), kdy uživatel zvolil Don't allow, víme s jistotou, že žádost proběhla. Pokud shouldShowRequestPermissionRationale vrátí false až v tomto momentě, znamená to, že systém už dialog znovu nezobrazí a uživatel jej zamítl trvale (ODMITNUTO_NATRVALO).

---

## 3. Tabulka čtyř stavů

| Stav | Podmínka v kódu | Chování UI | Screenshot |
|---|---|---|---|
| 1. Výchozí (NEZNÁMÝ) | Nemá oprávnění, rationale = false (před spuštěním) | Zobrazeno tlačítko Zjistit polohu. Po kliknutí se otevře systémový dialog. | stav-1-vychozi.png |
| 2. Zamítnuto (RATIONALE) | Nemá oprávnění, rationale = true | Zobrazeno vysvětlení přínosu oprávnění a tlačítko Pokračovat k udělení. | stav-2-rationale.png |
| 3. Trvalé zamítnutí | V callbacku je povoleno = false a rationale = false | Vysvětlení, že bez polohy vše funguje, a tlačítko Otevřít nastavení. Žádný systémový dialog. | stav-3-natrvalo.png |
| 4. Uděleno | checkSelfPermission == PERMISSION_GRANTED | Zobrazeny souřadnice polohy ze sítě (getLastKnownLocation), případně fallback text. | stav-4-udeleno.png |

---

## 4. Screenshoty

### Stav 1: Výchozí stav
Výchozí stav (stav-1-vychozi.png)

### Stav 2: Vysvětlení před druhým dialogem (Rationale)
Rationale (stav-2-rationale.png)

### Stav 3: Trvalé zamítnutí s odkazem do Nastavení
Trvalé zamítnutí (stav-3-natrvalo.png)

### Stav 4: Udělené oprávnění se souřadnicemi
Uděleno (stav-4-udeleno.png)