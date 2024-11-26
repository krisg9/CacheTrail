package de.htw.cachetrail.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import de.htw.cachetrail.data.model.Trail
import de.htw.cachetrail.ui.viewmodel.EditTrailsViewModel
import de.htw.cachetrail.ui.viewmodel.PlayViewModel

@Composable
fun PlayScreen() {

    val playViewModel: PlayViewModel = viewModel()

    val trails by playViewModel.trails.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        TrailList(trails, {})
    }
}

@Composable
fun TrailList(
    trails: List<Trail>,
    onTrailClick: (Trail) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(trails) { trail ->
                TrailItem(trail) { onTrailClick(trail) }
            }
        }
    }
}

@Composable
fun TrailItem(trail: Trail, onClick: () -> Unit) {
    val editTrailsViewModel: EditTrailsViewModel = viewModel()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = trail.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        IconButton(onClick = { editTrailsViewModel.deleteTrail(trail.id) }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Trail")
        }
    }
}