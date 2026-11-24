package cz.utb.meteodenik.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.utb.meteodenik.R
import cz.utb.meteodenik.model.Mesto
import cz.utb.meteodenik.ui.theme.MeteoDenikTheme

val ukazkovaMesta = listOf(
    Mesto(id = 1, nazev = "Zlín", teplotaC = 21.4),
    Mesto(id = 2, nazev = "Brno", teplotaC = 23.1),
    Mesto(id = 3, nazev = "Ostrava", teplotaC = null)
)

// 1. Stavová obrazovka – jediná s rememberSaveable a remember
@Composable
fun MestaObrazovka(
    onKlikMesto: (Mesto) -> Unit = {}
) {
    var nazevMesta by rememberSaveable { mutableStateOf("") }
    var mesta by remember { mutableStateOf(ukazkovaMesta) }
    var dalsiId by remember { mutableIntStateOf(ukazkovaMesta.size + 1) }

    MestaObsah(
        mesta = mesta,
        nazev = nazevMesta,
        onNazevChange = { nazevMesta = it },
        onPridat = {
            val ocistenyNazev = nazevMesta.trim()
            if (ocistenyNazev.isNotEmpty()) {
                val noveMesto = Mesto(
                    id = dalsiId++,
                    nazev = ocistenyNazev,
                    teplotaC = null
                )
                mesta = mesta + noveMesto
                nazevMesta = ""
            }
        },
        onKlikMesto = onKlikMesto
    )
}

// 2. Bezstavový obsah obrazovky (Scaffold s TopAppBar, řádek, oddělovač, seznam)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MestaObsah(
    mesta: List<Mesto>,
    nazev: String,
    onNazevChange: (String) -> Unit,
    onPridat: () -> Unit,
    onKlikMesto: (Mesto) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.mesta_titulek)) }
            )
        }
    ) { vnitrniOkraje ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vnitrniOkraje)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PridatMestoRadek(
                nazev = nazev,
                onNazevChange = onNazevChange,
                onPridat = onPridat
            )

            HorizontalDivider()

            MestaSeznam(
                mesta = mesta,
                onKlikMesto = onKlikMesto,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun MestoKarta(
    mesto: Mesto,
    onKlik: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onKlik() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = mesto.nazev,
                style = MaterialTheme.typography.titleMedium
            )

            val textTeploty = if (mesto.teplotaC != null) {
                stringResource(R.string.mesto_teplota, mesto.teplotaC)
            } else {
                stringResource(R.string.teplota_chybi)
            }

            Text(
                text = textTeploty,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun PridatMestoRadek(
    nazev: String,
    onNazevChange: (String) -> Unit,
    onPridat: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = nazev,
            onValueChange = onNazevChange,
            label = { Text(stringResource(R.string.mesto_pole_label)) },
            singleLine = true,
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = onPridat,
            enabled = nazev.isNotBlank()
        ) {
            Text(stringResource(R.string.mesto_pridat))
        }
    }
}

@Composable
fun MestaSeznam(
    mesta: List<Mesto>,
    onKlikMesto: (Mesto) -> Unit = {},
    modifier: Modifier = Modifier
) {
    if (mesta.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.mesto_prazdny_seznam),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = mesta,
                key = { mesto -> mesto.id }
            ) { mesto ->
                MestoKarta(
                    mesto = mesto,
                    onKlik = { onKlikMesto(mesto) }
                )
            }
        }
    }
}

// --- Tři náhledy celé obrazovky ---

@Preview(
    name = "1. Světlý režim",
    showBackground = true
)
@Composable
private fun MestaObsahSvetlyPreview() {
    MeteoDenikTheme(darkTheme = false) {
        MestaObsah(
            mesta = ukazkovaMesta,
            nazev = "Praha",
            onNazevChange = {},
            onPridat = {}
        )
    }
}

@Preview(
    name = "2. Tmavý režim",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun MestaObsahTmavyPreview() {
    MeteoDenikTheme(darkTheme = true) {
        MestaObsah(
            mesta = ukazkovaMesta,
            nazev = "",
            onNazevChange = {},
            onPridat = {}
        )
    }
}

@Preview(
    name = "3. Prázdný seznam",
    showBackground = true
)
@Composable
private fun MestaObsahPrazdnyPreview() {
    MeteoDenikTheme(darkTheme = false) {
        MestaObsah(
            mesta = emptyList(),
            nazev = "",
            onNazevChange = {},
            onPridat = {}
        )
    }
}
