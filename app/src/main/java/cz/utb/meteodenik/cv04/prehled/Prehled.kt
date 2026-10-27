package cz.utb.meteodenik.cv04.prehled

fun Double.jakoCelsia(): String = "$this C"

data class Mesto(
    val nazev: String,
    val teplota: Double
) {
    fun popis(): String = "$nazev: ${teplota.jakoCelsia()}"
}

sealed interface StavPrehledu {
    data object Nacita : StavPrehledu
    data object Chyba : StavPrehledu
    data object Prazdno : StavPrehledu
    data class Hotovo(val mesta: List<Mesto>) : StavPrehledu
}

class Prehled(
    private val mesta: List<Mesto>,
    var stav: StavPrehledu = if (mesta.isEmpty()) StavPrehledu.Prazdno else StavPrehledu.Hotovo(mesta)
) {

    fun textStavu(): String = when (val aktualniStav = stav) {
        is StavPrehledu.Nacita -> "Načítám…"
        is StavPrehledu.Chyba -> "Chyba"
        is StavPrehledu.Prazdno -> "Nic tu není"
        is StavPrehledu.Hotovo -> "Měst: ${aktualniStav.mesta.size}"
    }

    fun nejteplejsi(): Mesto? = mesta.maxByOrNull { it.teplota }

    fun vypis() {
        mesta.forEach { println(it.popis()) }
        println(textStavu())
        nejteplejsi()?.let { nej ->
            println("Nejtepleji: ${nej.nazev} (${nej.teplota.jakoCelsia()})")
        }
    }
}

fun main() {
    val prehled = Prehled(
        listOf(
            Mesto("Zlín", 21.4),
            Mesto("Brno", 23.0),
            Mesto("Otrokovice", 20.9)
        )
    )
    prehled.vypis()
}