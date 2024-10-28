package de.htw.cachetrail.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavHost(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(navController)
        }
        composable(Routes.PLAY) {
            PlayScreen()
        }
        composable(Routes.EDIT) {
            EditScreen(navController = navController)
        }
        composable(Routes.MAP) {
            MapScreen(navController = navController)
        }
    }
}