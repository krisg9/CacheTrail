package de.htw.cachetrail.ui.screens

import android.annotation.SuppressLint
import android.view.MotionEvent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.htw.cachetrail.data.model.MapUtils
import de.htw.cachetrail.data.model.Station
import de.htw.cachetrail.ui.viewmodel.EditTrailsViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.UUID

@Composable
fun AddTrailDialog(onDismiss: () -> Unit, onAdd: (String) -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .padding(16.dp)
                    .size(300.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    var trailName by remember { mutableStateOf("") }
                    val isValidTrailName = trailName.isNotBlank()

                    Text(text = "Add New Trail", style = MaterialTheme.typography.headlineMedium)

                    TextField(
                        value = trailName,
                        onValueChange = { trailName = it },
                        label = { Text("Trail Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                onAdd(trailName)
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f),
                            enabled = isValidTrailName
                        ) {
                            Text("Add Trail")
                        }

                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddScreen(navController: NavController) {

    var showDialog by remember { mutableStateOf(false) }
    val viewModel: EditTrailsViewModel = viewModel()

    val stations = viewModel.stations

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxHeight()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

            StationList(stations)
        }
        if (showDialog) {
            AddMarkerDialog(onDismiss = { showDialog = false }, onAdd = {})
        }

        Button(onClick = {
            showDialog = true
        }) {
            Text("Add Marker")
        }
    }
}

@Composable
fun StationList(stations: List<Station>) {

    val viewModel: EditTrailsViewModel = viewModel()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(stations) { station ->
                StationItem(station, onDelete = { viewModel.deleteStation(station) })
            }
        }
    }
}

@Composable
fun StationItem(station: Station, onDelete: (Station) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(120.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = station.question,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = station.answer,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            IconButton(onClick = { onDelete(station) }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Station")
            }
        }
    }
}

@Composable
fun AddMarkerDialog(onDismiss: () -> Unit = {}, onAdd: (Station) -> Unit) {
    var question by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }
    var latitude by remember { mutableStateOf("") }
    var longitude by remember { mutableStateOf("") }
    var isMapDialogOpen by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    val isFormValid = isValidLongitude(longitude) && isValidLatitude(latitude)

                    Text(
                        text = "Add New Station",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    TextField(
                        value = question,
                        onValueChange = { question = it },
                        label = { Text("Question") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    TextField(
                        value = answer,
                        onValueChange = { answer = it },
                        label = { Text("Answer") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = { isMapDialogOpen = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (latitude.isEmpty() || longitude.isEmpty()) {
                                "Pick Location"
                            } else {
                                "Edit Location (${latitude.take(8)}, ${longitude.take(8)})"
                            }
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                val newStation = Station(
                                    id = UUID.randomUUID().toString(),
                                    latitude = latitude.toDouble(),
                                    longitude = longitude.toDouble(),
                                    question = "$question?",
                                    answer = answer
                                )
                                onAdd(newStation)
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f),
                            enabled = isFormValid
                        ) {
                            Text("Add Marker")
                        }

                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }
    }

    if (isMapDialogOpen) {
        MapPickerDialog(
            onDismiss = { isMapDialogOpen = false },
            onSubmit = { lat, lon ->
                latitude = lat.toString()
                longitude = lon.toString()
                isMapDialogOpen = false
            }
        )
    }
}

private fun isValidLatitude(latitude: String): Boolean {
    val lat = latitude.toDoubleOrNull()
    return lat != null && lat in -90.0..90.0
}

fun isValidLongitude(longitude: String): Boolean {
    val lon = longitude.toDoubleOrNull()
    return lon != null && lon in -180.0..180.0
}

@SuppressLint("ClickableViewAccessibility")
@Composable
fun MapPickerDialog(onDismiss: () -> Unit, onSubmit: (Double, Double) -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Pick a Location",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    val context = LocalContext.current
                    val mapView = remember { MapView(context) }
                    val selectedLocation = remember { mutableStateOf<GeoPoint?>(null) }

                    DisposableEffect(context) {
                        mapView.apply {
                            setTileSource(TileSourceFactory.MAPNIK)
                            controller.setZoom(15)
                            controller.setCenter(MapUtils.BERLIN_COORDINATES)
                            clipToOutline = true
                        }


                        onDispose {
                            mapView.onDetach()
                        }
                    }

                    AndroidView(
                        factory = { mapView },
                        modifier = Modifier
                            .height(300.dp)
                            .padding(30.dp)
                    ) { map ->

                        map.setOnTouchListener { _, event ->
                            if (event.action == MotionEvent.ACTION_DOWN) {
                                val geoPoint =
                                    mapView.projection.fromPixels(event.x.toInt(), event.y.toInt())

                                mapView.overlays.clear()
                                val marker = Marker(mapView)

                                marker.position = geoPoint as GeoPoint

                                mapView.overlays.add(marker)
                                selectedLocation.value = geoPoint
                            }
                            false
                        }
                    }

                    Button(
                        onClick = {
                            selectedLocation.value?.let { location ->
                                onSubmit(location.latitude, location.longitude)
                            }
                        },
                        enabled = selectedLocation.value != null
                    ) {
                        Text("Submit Location")
                    }

                    Button(
                        onClick = onDismiss
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}