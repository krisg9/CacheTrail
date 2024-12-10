package de.htw.cachetrail.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

@Composable
fun MapTest(navController: NavController) {
AndroidView(
    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        .systemBarsPadding(),
        factory = { context ->
            MapView(context).apply {
                setMultiTouchControls(true)
                controller.setZoom(15.0)
                controller.setCenter(GeoPoint(52.46476, 13.51666))
                clipToOutline = true
            }
        },
    )
}