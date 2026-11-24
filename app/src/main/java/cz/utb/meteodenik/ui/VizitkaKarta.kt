package cz.utb.meteodenik.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.utb.meteodenik.R
import cz.utb.meteodenik.ui.theme.MeteoDenikTheme

@Composable
fun VizitkaKarta(
    jmeno: String,
    obor: String,
    email: String,
    modifier: Modifier = Modifier
) {
    // Pomocný výpočet iniciál z prvních písmen slov jména (např. "Jan Novák" -> "JN")
    val inicialy = jmeno
        .trim()
        .split(" ")
        .filter { it.isNotEmpty() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

    val avatarPopis = stringResource(R.string.vizitka_avatar_popis, jmeno)
    val emailPopis = stringResource(R.string.vizitka_email_popis, email)

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Kruhový avatar s iniciálami a sémantikou pro TalkBack
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clearAndSetSemantics {
                        contentDescription = avatarPopis
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = inicialy,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Tři řádky textu vedle avataru
            // weight(1f) zajistí, že při velkém písmu (fontScale = 1.5f) text nepřeteče ven z karty
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = jmeno,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = obor,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = email,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.semantics {
                        contentDescription = emailPopis
                    }
                )
            }
        }
    }
}

// --- Tři náhledy ---

@Preview(
    name = "1. Světlý režim",
    showBackground = true
)
@Composable
private fun VizitkaKartaSvetlyPreview() {
    MeteoDenikTheme(darkTheme = false) {
        VizitkaKarta(
            jmeno = "Jan Novák",
            obor = "Softwarové inženýrství",
            email = "j_novak@utb.cz",
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(
    name = "2. Tmavý režim",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun VizitkaKartaTmavyPreview() {
    MeteoDenikTheme(darkTheme = true) {
        VizitkaKarta(
            jmeno = "Jan Novák",
            obor = "Softwarové inženýrství",
            email = "j_novak@utb.cz",
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(
    name = "3. Zvětšené písmo (1.5x)",
    showBackground = true,
    fontScale = 1.5f
)
@Composable
private fun VizitkaKartaVelkePismoPreview() {
    MeteoDenikTheme(darkTheme = false) {
        VizitkaKarta(
            jmeno = "Jan Novák",
            obor = "Softwarové inženýrství",
            email = "jan.novak.dlouhy.email@utb.cz",
            modifier = Modifier.padding(16.dp)
        )
    }
}
