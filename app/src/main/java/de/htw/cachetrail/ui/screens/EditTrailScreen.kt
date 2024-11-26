package de.htw.cachetrail.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.htw.cachetrail.ui.viewmodel.EditTrailsViewModel

@Composable
fun EditTrailScreen(navController: NavController, trailId: String) {
    val editTrailsViewModel: EditTrailsViewModel = viewModel()
    val trail by editTrailsViewModel.getTrailById(trailId).collectAsState(initial = null)

    var showDialog by remember { mutableStateOf(false) }

    trail?.let {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxHeight()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                StationList(it.stations)
            }

            Button(
                onClick = {
                    showDialog = true
                }, modifier = Modifier
                    .padding(16.dp)
            ) {
                Text("Add new Station")
            }

            if (showDialog) {
                AddMarkerDialog(
                    onDismiss = { showDialog = false },
                    onAdd = { station ->
                        editTrailsViewModel.addStation(trailId, station)
                    }
                )
            }
        }
    }
}