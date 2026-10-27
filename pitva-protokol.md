Co jsem napsal                         | Generovaný člen                       | Počet řádků ručně |
data class Zapis                       | Zapis.java                            | 116
object class Zapis                     | $FF: synthetic method                 | 41
object class Zapis                     | $FF: synthetic method                 | 44
object Prevody                         | Prevody.java                          | 28
fun Double, fun prumer, suspend fun.   | PitvaKt.java                          | 124

1. 10 metod, nemá toString(), HashCode(), getMesto(), getTeplota(), getPoznamka(), component1(), component2(), component3()
2. Kotlin jimi emuluje výchozí hodnoty
3. Prvním parametrem je double $this$zaokrouhlenoNaDesetiny. Funkce není polymorfní, protože je přeložena jako static metoda.
4. Druhý parametr má typ @NotNull Function1 vyber. Kotlin reprezentuje lambda výraz/referenci na funkci jako instanci třídy ze skupiny Function0,Function1 atd.
5. Kotlin kompilátor ke každé suspend funkci přidá skrytý parametr Continuation, který nese stav provádění a callback pro obnovení koroutiny.
Switch se objevil proto, že kompilátor Kotlinu převádí kód každé pozastavitelné funkce na konečný stavový automat.