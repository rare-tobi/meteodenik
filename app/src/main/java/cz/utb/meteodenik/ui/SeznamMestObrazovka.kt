package cz.utb.meteodenik.ui

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.utb.meteodenik.DetailMestaActivity
import cz.utb.meteodenik.R
import cz.utb.meteodenik.model.UKAZKOVA_MESTA
import androidx.compose.foundation.background
import androidx.compose.ui.res.colorResource

@Composable
fun SeznamMestObrazovka() {
    val context = LocalContext.current

    Scaffold { insety ->
        Column(modifier = Modifier.padding(insety).padding(16.dp)) {
            val pocet = UKAZKOVA_MESTA.size
            val textPocet = pluralStringResource(R.plurals.pocet_zaznamu, pocet, pocet)

            Text(
                text = stringResource(R.string.titulek_seznam_mest, textPocet),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            PolohaKarta(onPolohaPovolena = {})

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            UKAZKOVA_MESTA.forEach { mesto ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorResource(R.color.pozadi_karty))
                        .clickable {
                            val intent = Intent(context, DetailMestaActivity::class.java).apply {
                                putExtra(DetailMestaActivity.EXTRA_MESTO, mesto.nazev)
                                putExtra(DetailMestaActivity.EXTRA_TEPLOTA, mesto.teplota)
                            }
                            context.startActivity(intent)
                        }
                        .padding(16.dp)
                ) {
                    Text(
                        text = mesto.nazev,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = stringResource(R.string.teplota_format, mesto.teplota),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                HorizontalDivider()
            }
        }
    }
}