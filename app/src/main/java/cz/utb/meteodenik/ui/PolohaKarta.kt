package cz.utb.meteodenik.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import cz.utb.meteodenik.R

private const val OPRAVNENI_POLOHA = Manifest.permission.ACCESS_COARSE_LOCATION

enum class StavOpravneni { NEZNAMY, ODMITNUTO, ODMITNUTO_NATRVALO, UDELENO }

@Composable
fun PolohaKarta(
    onPolohaPovolena: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = LocalActivity.current

    var stav by remember {
        mutableStateOf(
            if (ContextCompat.checkSelfPermission(context, OPRAVNENI_POLOHA) == PackageManager.PERMISSION_GRANTED) {
                StavOpravneni.UDELENO
            } else {
                StavOpravneni.NEZNAMY
            }
        )
    }

    var zobrazitVysvetleni by remember { mutableStateOf(false) }
    var posledniPoloha by remember { mutableStateOf<Location?>(null) }

    fun nactiPolohu() {
        if (ContextCompat.checkSelfPermission(context, OPRAVNENI_POLOHA) == PackageManager.PERMISSION_GRANTED) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            posledniPoloha = try {
                locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            } catch (e: SecurityException) {
                null
            }
        }
    }

    remember(stav) {
        if (stav == StavOpravneni.UDELENO) {
            nactiPolohu()
        }
    }

    val spoustec = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { povoleno ->
        stav = when {
            povoleno -> {
                nactiPolohu()
                onPolohaPovolena()
                StavOpravneni.UDELENO
            }
            activity != null && ActivityCompat.shouldShowRequestPermissionRationale(activity, OPRAVNENI_POLOHA) -> {
                StavOpravneni.ODMITNUTO
            }
            else -> {
                StavOpravneni.ODMITNUTO_NATRVALO
            }
        }
    }

    val aktualniOnPolohaPovolena by rememberUpdatedState(onPolohaPovolena)
    val lifecycle = (activity as? LifecycleOwner)?.lifecycle
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, udalost ->
            if (udalost == Lifecycle.Event.ON_RESUME) {
                val jePovoleno = ContextCompat.checkSelfPermission(context, OPRAVNENI_POLOHA) == PackageManager.PERMISSION_GRANTED
                if (jePovoleno && stav != StavOpravneni.UDELENO) {
                    stav = StavOpravneni.UDELENO
                    nactiPolohu()
                    aktualniOnPolohaPovolena()
                }
            }
        }
        lifecycle?.addObserver(observer)
        onDispose { lifecycle?.removeObserver(observer) }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            when (stav) {
                StavOpravneni.UDELENO -> {
                    val poloha = posledniPoloha
                    if (poloha != null) {
                        Text(
                            text = stringResource(
                                R.string.poloha_povolena_souradnice,
                                poloha.latitude,
                                poloha.longitude
                            ),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.poloha_nedostupna),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                StavOpravneni.ODMITNUTO_NATRVALO -> {
                    Text(
                        text = stringResource(R.string.poloha_bez_opravneni),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = {
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        )
                        context.startActivity(intent)
                    }) {
                        Text(stringResource(R.string.otevrit_nastaveni))
                    }
                }

                else -> {
                    if (zobrazitVysvetleni) {
                        Text(
                            text = stringResource(R.string.poloha_vysvetleni),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = {
                            zobrazitVysvetleni = false
                            spoustec.launch(OPRAVNENI_POLOHA)
                        }) {
                            Text(stringResource(R.string.poloha_pokracovat))
                        }
                    } else {
                        Button(onClick = {
                            val rationale = activity != null && ActivityCompat.shouldShowRequestPermissionRationale(activity, OPRAVNENI_POLOHA)
                            if (rationale) {
                                zobrazitVysvetleni = true
                            } else {
                                spoustec.launch(OPRAVNENI_POLOHA)
                            }
                        }) {
                            Text(stringResource(R.string.poloha_zjistit))
                        }
                    }
                }
            }
        }
    }
}