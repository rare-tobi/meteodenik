package cz.utb.meteodenik.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.utb.meteodenik.R

@Composable
fun DetailObrazovka(mesto: String, teplota: Double) {
    Scaffold { insety ->
        Column(modifier = Modifier.padding(insety).padding(16.dp)) {
            Text(text = mesto, style = MaterialTheme.typography.headlineMedium)
            Text(
                text = if (teplota.isNaN()) {
                    stringResource(R.string.teplota_chybi)        // extra chybělo
                } else {
                    stringResource(R.string.teplota_format, teplota)
                }
            )
            // krok 6: sem přibude SdiletTlacitko(mesto = mesto, teplota = teplota)
        }
    }
}