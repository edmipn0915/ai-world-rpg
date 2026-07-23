package com.aiworld.rpg.ui.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route: String) {
    object WorldSelect : Screen("world_select")
    object CharacterCreate : Screen("character_create/{worldType}") {
        fun createRoute(worldType: String) = "character_create/$worldType"
    }
    object Game : Screen("game/{saveId}") {
        fun createRoute(saveId: String) = "game/$saveId"
    }
    object LoadGame : Screen("load_game")
    object Settings : Screen("settings")
}

@Composable
fun MainNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.WorldSelect.route
    ) {
        composable(Screen.WorldSelect.route) {
            WorldSelectScreen(
                onWorldSelected = { worldType ->
                    navController.navigate(Screen.CharacterCreate.createRoute(worldType.name))
                },
                onLoadGame = {
                    navController.navigate(Screen.LoadGame.route)
                },
                onSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(Screen.CharacterCreate.route) { backStackEntry ->
            val worldTypeName = backStackEntry.arguments?.getString("worldType") ?: "FANTASY"
            CharacterCreateScreen(
                worldTypeName = worldTypeName,
                onCharacterCreated = { saveId ->
                    navController.navigate(Screen.Game.createRoute(saveId)) {
                        popUpTo(Screen.WorldSelect.route)
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Game.route) { backStackEntry ->
            val saveId = backStackEntry.arguments?.getString("saveId") ?: ""
            GameScreen(
                saveId = saveId,
                onExit = {
                    navController.popBackStack(Screen.WorldSelect.route, inclusive = false)
                }
            )
        }

        composable(Screen.LoadGame.route) {
            LoadGameScreen(
                onSaveSelected = { saveId ->
                    navController.navigate(Screen.Game.createRoute(saveId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
