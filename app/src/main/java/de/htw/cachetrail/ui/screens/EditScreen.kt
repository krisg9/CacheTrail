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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import de.htw.cachetrail.data.model.Trail
import de.htw.cachetrail.ui.viewmodel.EditTrailsViewModel
import de.htw.cachetrail.ui.viewmodel.PlayViewModel
import java.util.UUID

@Composable
fun EditScreen(navController: NavHostController) {

    var showDialog by remember { mutableStateOf(false) }
    val editTrailsViewModel: EditTrailsViewModel = viewModel()

    val playViewModel: PlayViewModel = viewModel()

    val trails by playViewModel.trails.collectAsStateWithLifecycle()

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
            TrailList(trails, onTrailClick = { trail ->
                navController.navigate(Routes.EDIT_TRAIL.replace("{trailId}", trail.id))
            })
        }

        Button(
            onClick = {
                showDialog = true
            }, modifier = Modifier
                .padding(16.dp)
        ) {
            Text("Add new Trail")
        }

        if (showDialog) {
            AddTrailDialog(
                onDismiss = { showDialog = false },
                onAdd = { trailName ->
                    val newTrail = Trail(
                        id = UUID.randomUUID().toString(),
                        name = trailName,
                        stations = emptyList()
                    )
                    editTrailsViewModel.addTrail(newTrail)
                }
            )
        }
    }
}