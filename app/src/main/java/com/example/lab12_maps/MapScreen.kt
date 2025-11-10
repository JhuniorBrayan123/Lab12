package com.example.lab12_maps

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen() {
    val context = LocalContext.current

    // 📍 Coordenada base
    val arequipaLocation = LatLng(-16.4040102, -71.559611)

    // 🎥 Estado de cámara
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(arequipaLocation, 12f)
    }

    // 🔄 Estado del tipo de mapa
    var selectedMapType by remember { mutableStateOf(MapType.NORMAL) }

    // 🎥 Animación inicial de cámara
    LaunchedEffect(Unit) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(
                LatLng(-16.2520984, -71.6836503),
                12f
            ),
            durationMs = 3000
        )
    }

    // 📍 Otras ubicaciones
    val locations = listOf(
        LatLng(-16.433415, -71.5442652), // JLByR
        LatLng(-16.4205151, -71.4945209), // Paucarpata
        LatLng(-16.3524187, -71.5675994)  // Zamacola
    )

    // 🔺 Polígonos
    val mallAventuraPolygon = listOf(
        LatLng(-16.432292, -71.509145),
        LatLng(-16.432757, -71.509626),
        LatLng(-16.433013, -71.509310),
        LatLng(-16.432566, -71.508853)
    )

    val parqueLambramaniPolygon = listOf(
        LatLng(-16.422704, -71.530830),
        LatLng(-16.422920, -71.531340),
        LatLng(-16.423264, -71.531110),
        LatLng(-16.423050, -71.530600)
    )

    val plazaDeArmasPolygon = listOf(
        LatLng(-16.398866, -71.536961),
        LatLng(-16.398744, -71.536529),
        LatLng(-16.399178, -71.536289),
        LatLng(-16.399299, -71.536721)
    )

    // 🗺️ Contenedor principal
    Box(modifier = Modifier.fillMaxSize()) {

        // 🗺️ Mapa con tipo dinámico
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(mapType = selectedMapType)
        ) {
            // 🏔️ Ícono personalizado
            val scaledIcon = BitmapDescriptorFactory.fromBitmap(
                Bitmap.createScaledBitmap(
                    BitmapFactory.decodeResource(context.resources, R.drawable.montana_icon),
                    200,
                    200,
                    false
                )
            )

            // 📍 Marcador principal
            Marker(
                state = rememberMarkerState(position = arequipaLocation),
                title = "Arequipa, Perú",
                snippet = "Ciudad Blanca",
                icon = scaledIcon
            )

            // 📍 Varios marcadores
            locations.forEach { location ->
                Marker(
                    state = rememberMarkerState(position = location),
                    title = "Ubicación",
                    snippet = "Punto de interés"
                )
            }

            // 🔺 Polígonos
            Polygon(
                points = plazaDeArmasPolygon,
                strokeColor = Color.Red,
                fillColor = Color.Blue,
                strokeWidth = 5f
            )
            Polygon(
                points = parqueLambramaniPolygon,
                strokeColor = Color.Red,
                fillColor = Color.Blue,
                strokeWidth = 5f
            )
            Polygon(
                points = mallAventuraPolygon,
                strokeColor = Color.Red,
                fillColor = Color.Blue,
                strokeWidth = 5f
            )
        }

        // 🎛️ Menú desplegable para cambiar el tipo de mapa
        var expanded by remember { mutableStateOf(false) }
        val opciones = listOf("Normal", "Satélite", "Terreno", "Híbrido")

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Button(onClick = { expanded = !expanded }) {
                Text("Tipo de mapa")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                opciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion) },
                        onClick = {
                            selectedMapType = when (opcion) {
                                "Normal" -> MapType.NORMAL
                                "Satélite" -> MapType.SATELLITE
                                "Terreno" -> MapType.TERRAIN
                                "Híbrido" -> MapType.HYBRID
                                else -> MapType.NORMAL
                            }
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}