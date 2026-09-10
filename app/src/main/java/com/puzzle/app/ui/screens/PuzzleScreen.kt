package com.puzzle.app.ui.screens

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.puzzle.app.data.StatisticsRepository
import com.puzzle.app.engine.PuzzleEngine
import com.puzzle.app.engine.PuzzleState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuzzleScreen(
    image: Bitmap,
    rows: Int,
    cols: Int,
    showNumbers: Boolean,
    onToggleNumbers: () -> Unit,
    onBack: () -> Unit,
    onNewPuzzle: () -> Unit
) {
    val context = LocalContext.current
    val statisticsRepository = remember { StatisticsRepository(context) }
    val scope = rememberCoroutineScope()

    var puzzleState by remember {
        mutableStateOf(PuzzleEngine.shuffle(PuzzleEngine.createSolvedState(rows, cols)))
    }
    var moves by remember { mutableIntStateOf(0) }
    var isSolved by remember { mutableStateOf(false) }
    var showImagePreview by remember { mutableStateOf(false) }
    var startTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var elapsedTime by remember { mutableLongStateOf(0L) }

    LaunchedEffect(Unit) {
        statisticsRepository.incrementPlayed()
    }

    LaunchedEffect(isSolved) {
        if (!isSolved) {
            while (true) {
                delay(100)
                elapsedTime = System.currentTimeMillis() - startTime
            }
        }
    }

    fun formatTime(ms: Long): String {
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun handleMove(index: Int) {
        if (isSolved) return
        val newState = puzzleState.move(index)
        if (newState != null) {
            puzzleState = newState
            moves++
            if (newState.isSolved()) {
                isSolved = true
                elapsedTime = System.currentTimeMillis() - startTime
                scope.launch {
                    statisticsRepository.recordCompletion(elapsedTime, moves)
                }
            }
        }
    }

    fun shufflePuzzle() {
        puzzleState = PuzzleEngine.shuffle(PuzzleEngine.createSolvedState(rows, cols))
        moves = 0
        isSolved = false
        startTime = System.currentTimeMillis()
        elapsedTime = 0L
    }

    fun formatMoves(count: Int): String {
        return count.toString()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Puzzle") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Timer and Moves
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("⏱ ", fontSize = 18.sp)
                            Text(
                                text = formatTime(elapsedTime),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🔄 ", fontSize = 18.sp)
                            Text(
                                text = formatMoves(moves),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Puzzle Board
                PuzzleBoard(
                    state = puzzleState,
                    image = image,
                    showNumbers = showNumbers,
                    onTileClick = { handleMove(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { shufflePuzzle() },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        )
                    ) {
                        Text("🔀 Acak")
                    }

                    Button(
                        onClick = { showImagePreview = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Icon(
                            Icons.Default.Visibility,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Lihat Gambar")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    FilterChip(
                        selected = showNumbers,
                        onClick = onToggleNumbers,
                        label = { Text("Tampilkan Angka") },
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Image Preview Overlay
            AnimatedVisibility(
                visible = showImagePreview,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.85f))
                        .clickable { showImagePreview = false },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .aspectRatio(1f),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                        ) {
                            Image(
                                bitmap = image.asImageBitmap(),
                                contentDescription = "Original Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        FilledTonalButton(
                            onClick = { showImagePreview = false },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("✕  Tutup", fontSize = 16.sp)
                        }
                    }
                }
            }

            // Completion Overlay
            AnimatedVisibility(
                visible = isSolved,
                enter = fadeIn() + scaleIn(),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .shadow(16.dp, RoundedCornerShape(24.dp)),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🎉",
                                fontSize = 64.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Puzzle Selesai!",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "⏱ Waktu: ${formatTime(elapsedTime)}",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "🔄 Gerakan: $moves",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = { shufflePuzzle() },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Main Lagi")
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedButton(
                                onClick = onNewPuzzle,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Puzzle Baru")
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            TextButton(
                                onClick = onBack,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Kembali")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PuzzleBoard(
    state: PuzzleState,
    image: Bitmap,
    showNumbers: Boolean,
    onTileClick: (Int) -> Unit
) {
    val tileWidth = (image.width / state.cols).toFloat()
    val tileHeight = (image.height / state.rows).toFloat()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(state.cols.toFloat() / state.rows)
            .shadow(8.dp, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(4.dp)
    ) {
        val boardWidth = maxWidth - 8.dp
        val boardHeight = maxHeight - 8.dp
        val tileSize = minOf(boardWidth / state.cols, boardHeight / state.rows)
        val totalWidth = tileSize * state.cols
        val totalHeight = tileSize * state.rows
        val offsetX = (boardWidth - totalWidth) / 2 + 4.dp
        val offsetY = (boardHeight - totalHeight) / 2 + 4.dp

        Box(modifier = Modifier.fillMaxSize()) {
            state.tiles.forEachIndexed { index, tileValue ->
                if (tileValue != state.totalTiles - 1) {
                    val currentRow = index / state.cols
                    val currentCol = index % state.cols
                    val solvedRow = tileValue / state.cols
                    val solvedCol = tileValue % state.cols

                    val targetX = offsetX + tileSize * currentCol
                    val targetY = offsetY + tileSize * currentRow

                    val cropLeft = solvedCol * tileWidth
                    val cropTop = solvedRow * tileHeight

                    Box(
                        modifier = Modifier
                            .offset(x = targetX, y = targetY)
                            .size(tileSize - 2.dp)
                            .shadow(2.dp, RoundedCornerShape(4.dp))
                            .clip(RoundedCornerShape(4.dp))
                            .clickable { onTileClick(index) }
                    ) {
                        Image(
                            bitmap = image.asImageBitmap(),
                            contentDescription = "Tile ${tileValue + 1}",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        if (showNumbers) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Black.copy(alpha = 0.3f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${tileValue + 1}",
                                    color = Color.White,
                                    fontSize = (tileSize.value / 4).sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
