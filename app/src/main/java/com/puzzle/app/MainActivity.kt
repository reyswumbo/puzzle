package com.puzzle.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.puzzle.app.ui.PuzzleNavigation
import com.puzzle.app.ui.theme.PuzzleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PuzzleTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PuzzleNavigation()
                }
            }
        }
    }
}
