package de.htw.cachetrail.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import de.htw.cachetrail.data.model.Station
import de.htw.cachetrail.ui.nav.Routes
import de.htw.cachetrail.ui.viewmodel.Feedback
import de.htw.cachetrail.ui.viewmodel.MapScreenViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun Map(trailId: String, navController: NavHostController) {
    val mapViewModel: MapScreenViewModel = viewModel()

    val gameState by mapViewModel.gameState.collectAsStateWithLifecycle()
    val currentStation = gameState.currentStation
    val feedback = gameState.feedback
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(trailId) {
        mapViewModel.loadTrail(trailId)
    }

    LaunchedEffect(gameState.isGameCompleted) {
        if (gameState.isGameCompleted) {
            navController.navigate(Routes.COMPLETED)
        }
    }

    AndroidView(
        factory = { context ->
            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
            }
        },
        update = { mapView ->
            mapView.overlays.clear()
            currentStation?.let { station ->
                addMarker(mapView, station) {
                    showDialog = true
                }
                mapView.controller.setZoom(15.0)
                mapView.controller.setCenter(GeoPoint(station.latitude, station.longitude))
            }
        }
    )

    if (showDialog) {
        currentStation?.let { station ->
            StationQuestionDialog(
                station = station,
                feedback = feedback,
                onDismiss = { showDialog = false },
                onSubmit = { answer ->
                    mapViewModel.submitAnswer(answer)
                }
            )
        }
    }
}

@Composable
fun StationQuestionDialog(
    station: Station,
    feedback: Feedback?,
    onDismiss: () -> Unit,
    onSubmit: (String) -> Unit,
) {
    var answer by remember { mutableStateOf("") }


    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = station.question) },
        text = {
            Column {
                feedback?.let {
                    Text(
                        text = when (it) {
                            Feedback.CORRECT -> "Correct!"
                            Feedback.INCORRECT -> "Wrong!"
                            Feedback.COMPLETED -> "Congratulations you have completed the trail!"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = when (it) {
                            Feedback.CORRECT -> MaterialTheme.colorScheme.primary
                            Feedback.INCORRECT -> MaterialTheme.colorScheme.error
                            Feedback.COMPLETED -> MaterialTheme.colorScheme.primary
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = answer,
                    onValueChange = { answer = it },
                    label = { Text("Your Answer") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSubmit(answer) }) {
                Text("Submit")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}

private fun addMarker(
    mapView: MapView,
    station: Station,
    onMarkerClick: () -> Unit
) {
    val marker = Marker(mapView).apply {
        position = GeoPoint(station.latitude, station.longitude)
        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

        infoWindow = null

        setOnMarkerClickListener { _, _ ->
            onMarkerClick()
            true
        }
    }
    mapView.overlays.add(marker)
}