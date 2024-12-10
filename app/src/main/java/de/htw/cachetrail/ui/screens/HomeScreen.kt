package de.htw.cachetrail.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import de.htw.cachetrail.ui.nav.Routes

@Composable
fun HomeScreen(navController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = {
                navController.navigate(Routes.PLAY)
            }) {
                Text("Play")
            }
            Spacer(modifier = Modifier.size(4.dp))
            Button(onClick = {
                navController.navigate(Routes.EDIT)
            }) {
                Text("Edit")
            }
            Spacer(modifier = Modifier.size(4.dp))
            Button(onClick = {
                navController.navigate(Routes.MAP)
            }) {
                Text("Map")
            }
        }
    }
}