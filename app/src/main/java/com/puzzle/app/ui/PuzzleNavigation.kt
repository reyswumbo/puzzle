package com.puzzle.app.ui

import android.graphics.Bitmap
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.puzzle.app.data.StatisticsRepository
import com.puzzle.app.engine.PuzzleState
import com.puzzle.app.ui.screens.HomeScreen
import com.puzzle.app.ui.screens.PuzzleScreen
import com.puzzle.app.ui.screens.SetupScreen
import com.puzzle.app.ui.screens.StatisticsScreen

@Composable
fun PuzzleNavigation() {
    val navController = rememberNavController()
    var puzzleImage by remember { mutableStateOf<Bitmap?>(null) }
    var puzzleState by remember { mutableStateOf<PuzzleState?>(null) }
    var selectedRows by remember { mutableIntStateOf(3) }
    var selectedCols by remember { mutableIntStateOf(3) }
    var showNumbers by remember { mutableStateOf(false) }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onStart = { navController.navigate("setup") },
                onStatistics = { navController.navigate("statistics") }
            )
        }
        composable("setup") {
            SetupScreen(
                onBack = { navController.popBackStack() },
                onStartGame = { image, rows, cols ->
                    puzzleImage = image
                    selectedRows = rows
                    selectedCols = cols
                    navController.navigate("puzzle") {
                        popUpTo("setup") { inclusive = true }
                    }
                }
            )
        }
        composable("puzzle") {
            val img = puzzleImage
            val rows = selectedRows
            val cols = selectedCols
            if (img != null) {
                PuzzleScreen(
                    image = img,
                    rows = rows,
                    cols = cols,
                    showNumbers = showNumbers,
                    onToggleNumbers = { showNumbers = !showNumbers },
                    onBack = {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    onNewPuzzle = {
                        navController.navigate("setup") {
                            popUpTo("home")
                        }
                    }
                )
            }
        }
        composable("statistics") {
            StatisticsScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
