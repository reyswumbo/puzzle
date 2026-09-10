package com.puzzle.app.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "puzzle_statistics")

data class Statistics(
    val totalTimeMs: Long = 0L,
    val puzzlesPlayed: Int = 0,
    val puzzlesCompleted: Int = 0,
    val totalMoves: Long = 0L
) {
    val formattedTime: String
        get() {
            val totalSeconds = totalTimeMs / 1000
            val hours = totalSeconds / 3600
            val minutes = (totalSeconds % 3600) / 60
            return when {
                hours > 0 -> "$hours jam $minutes menit"
                minutes > 0 -> "$minutes menit"
                else -> "${totalSeconds % 60} detik"
            }
        }
}

class StatisticsRepository(private val context: Context) {

    companion object {
        private val KEY_TOTAL_TIME = longPreferencesKey("total_time_ms")
        private val KEY_PUZZLES_PLAYED = intPreferencesKey("puzzles_played")
        private val KEY_PUZZLES_COMPLETED = intPreferencesKey("puzzles_completed")
        private val KEY_TOTAL_MOVES = longPreferencesKey("total_moves")
    }

    val statistics: Flow<Statistics> = context.dataStore.data.map { preferences ->
        Statistics(
            totalTimeMs = preferences[KEY_TOTAL_TIME] ?: 0L,
            puzzlesPlayed = preferences[KEY_PUZZLES_PLAYED] ?: 0,
            puzzlesCompleted = preferences[KEY_PUZZLES_COMPLETED] ?: 0,
            totalMoves = preferences[KEY_TOTAL_MOVES] ?: 0L
        )
    }

    suspend fun incrementPlayed() {
        context.dataStore.edit { preferences ->
            preferences[KEY_PUZZLES_PLAYED] = (preferences[KEY_PUZZLES_PLAYED] ?: 0) + 1
        }
    }

    suspend fun recordCompletion(timeMs: Long, moves: Int) {
        context.dataStore.edit { preferences ->
            preferences[KEY_PUZZLES_COMPLETED] = (preferences[KEY_PUZZLES_COMPLETED] ?: 0) + 1
            preferences[KEY_TOTAL_TIME] = (preferences[KEY_TOTAL_TIME] ?: 0L) + timeMs
            preferences[KEY_TOTAL_MOVES] = (preferences[KEY_TOTAL_MOVES] ?: 0L) + moves
        }
    }

    suspend fun reset() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
