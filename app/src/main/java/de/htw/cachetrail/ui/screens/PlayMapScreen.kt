package de.htw.cachetrail.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import de.htw.cachetrail.data.model.Station
import de.htw.cachetrail.ui.viewmodel.MapScreenViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun Map(trailId: String) {
    val playViewModel: MapScreenViewModel = viewModel()

    val trail by playViewModel.currentTrail.collectAsState()
    val station by playViewModel.station.collectAsStateWithLifecycle()
    playViewModel.chooseTrail(trailId)

    AndroidView(
        factory = { ctx ->
            val mapView = MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                clipToOutline = true
            }

            addMarker(mapView, station)
            mapView
        },
        update = { mapView ->
            mapView.overlays.clear()
            addMarker(mapView, station)
            mapView.controller.setZoom(15.0)
            mapView.controller.setCenter(
                GeoPoint(
                    station.latitude,
                    station.longitude
                )
            )
        }
    )
}

private fun addMarker(mapView: MapView, station: Station) {
    val marker = Marker(mapView).apply {
        position = GeoPoint(station.latitude, station.longitude)
        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        title =
            "Station: ${station.id},\n${station.latitude}, ${station.longitude}\n${station.question}"
    }
    mapView.overlays.add(marker)
}