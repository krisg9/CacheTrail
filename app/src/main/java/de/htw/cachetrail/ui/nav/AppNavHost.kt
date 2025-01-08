package de.htw.cachetrail.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import de.htw.cachetrail.ui.screens.AddScreen
import de.htw.cachetrail.ui.screens.CongratulationsScreen
import de.htw.cachetrail.ui.screens.EditScreen
import de.htw.cachetrail.ui.screens.EditTrailScreen
import de.htw.cachetrail.ui.screens.HomeScreen
import de.htw.cachetrail.ui.screens.Map
import de.htw.cachetrail.ui.screens.PlayScreen

@Composable
fun AppNavHost(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(navController)
        }
        composable(Routes.PLAY) {
            PlayScreen(navController = navController)
        }
        composable(Routes.EDIT) {
            EditScreen(navController)
        }

        composable(Routes.EDIT_TRAIL) { backStackEntry ->
            val trailId = backStackEntry.arguments?.getString("trailId")
            trailId?.let {
                EditTrailScreen(navController = navController, trailId)
            }
        }
        composable(Routes.ADD_SCREEN) {
            AddScreen(navController = navController)
        }
        composable(Routes.PLAY_MAP) { backStackEntry ->
            val trailId = backStackEntry.arguments?.getString("trailId")
            trailId?.let {
                Map(trailId, navController)
            }
        }
        composable(Routes.COMPLETED) {
            CongratulationsScreen(navController = navController)
        }
    }
}