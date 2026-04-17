package com.example.gitutility.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gitutility.viewmodel.TimerViewModel

/**
 * TimerScreen contains both a countdown Timer and a Stopwatch.
 */
@Composable
fun TimerScreen(viewModel: TimerViewModel = viewModel()) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Switch between Timer and Stopwatch at the top
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            divider = {},
            indicator = {}
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Timer", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Stopwatch", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Detect landscape or portrait to position the clock and controls
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isLandscape = maxWidth > maxHeight
            val contentModifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())

            if (selectedTab == 0) {
                // --- Timer View ---
                if (isLandscape) {
                    Row(
                        modifier = contentModifier,
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            TimerProgressCircle(viewModel)
                        }
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            TimerControls(viewModel)
                        }
                    }
                } else {
                    Column(
                        modifier = contentModifier,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TimerProgressCircle(viewModel)
                        Spacer(modifier = Modifier.height(48.dp))
                        TimerControls(viewModel)
                    }
                }
            } else {
                // --- Stopwatch View ---
                if (isLandscape) {
                    Row(
                        modifier = contentModifier,
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            StopwatchDisplay(viewModel)
                        }
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            StopwatchControls(viewModel)
                        }
                    }
                } else {
                    Column(
                        modifier = contentModifier,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        StopwatchDisplay(viewModel)
                        Spacer(modifier = Modifier.height(48.dp))
                        StopwatchControls(viewModel)
                    }
                }
            }
        }
    }
}

/**
 * Animated circle that shows how much time is left on the timer.
 */
@Composable
fun TimerProgressCircle(viewModel: TimerViewModel) {
    BoxWithConstraints {
        val size = minOf(maxWidth, maxHeight, 300.dp)
        val progress = if (viewModel.initialTimerTime > 0) {
            viewModel.timerTime.toFloat() / viewModel.initialTimerTime.toFloat()
        } else {
            0f
        }
        
        // animateFloatAsState makes the progress transition smooth
        val animatedProgress by animateFloatAsState(targetValue = progress, label = "timer_progress")
        val activeColor = if (viewModel.isTimerRunning) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary
        val trackColor = Color.LightGray.copy(alpha = 0.3f)

        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(size)) {
            // Canvas is used for custom drawing (the circles)
            Canvas(modifier = Modifier.size(size * 0.9f)) {
                drawCircle(
                    color = trackColor,
                    style = Stroke(width = (size.toPx() * 0.04f))
                )
                drawArc(
                    color = activeColor,
                    startAngle = -90f,
                    sweepAngle = 360f * animatedProgress,
                    useCenter = false,
                    style = Stroke(width = (size.toPx() * 0.04f), cap = StrokeCap.Round)
                )
            }
            
            Text(
                text = viewModel.formatTimer(viewModel.timerTime),
                fontSize = (size.value * 0.18f).sp,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

/**
 * Start, Pause, and Preset buttons for the Timer.
 */
@Composable
fun TimerControls(viewModel: TimerViewModel) {
    if (viewModel.timerTime == 0L && !viewModel.isTimerRunning) {
        // Quick-set buttons for common durations
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TimerSetButton("1m", onClick = { viewModel.setTimer(1, 0) })
            TimerSetButton("5m", onClick = { viewModel.setTimer(5, 0) })
            TimerSetButton("10m", onClick = { viewModel.setTimer(10, 0) })
        }
        Spacer(modifier = Modifier.height(24.dp))
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (viewModel.isTimerRunning) {
            Button(
                onClick = { viewModel.pauseTimer() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                modifier = Modifier.size(80.dp),
                shape = CircleShape
            ) {
                Text("Pause")
            }
        } else if (viewModel.timerTime > 0) {
            Button(
                onClick = { viewModel.startTimer() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                modifier = Modifier.size(80.dp),
                shape = CircleShape
            ) {
                Text("Start")
            }
        }

        if (viewModel.timerTime > 0 || viewModel.initialTimerTime > 0) {
            OutlinedButton(
                onClick = { viewModel.clearTimer() },
                modifier = Modifier.size(80.dp),
                shape = CircleShape
            ) {
                Text("Clear")
            }
        }
    }
}

/**
 * Large numeric display for the Stopwatch.
 */
@Composable
fun StopwatchDisplay(viewModel: TimerViewModel) {
    BoxWithConstraints {
        val size = minOf(maxWidth, maxHeight, 300.dp)
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(size)) {
            Surface(
                modifier = Modifier.size(size * 0.9f),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                shadowElevation = 0.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = viewModel.formatTime(viewModel.stopwatchTime),
                        fontSize = (size.value * 0.16f).sp,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

/**
 * Start, Pause, and Reset buttons for the Stopwatch.
 */
@Composable
fun StopwatchControls(viewModel: TimerViewModel) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        if (viewModel.isStopwatchRunning) {
            Button(
                onClick = { viewModel.pauseStopwatch() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFf44336)),
                modifier = Modifier.size(100.dp, 56.dp),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Pause", fontSize = 18.sp)
            }
        } else {
            Button(
                onClick = { viewModel.startStopwatch() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                modifier = Modifier.size(100.dp, 56.dp),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text("Start", fontSize = 18.sp)
            }
        }

        OutlinedButton(
            onClick = { viewModel.resetStopwatch() },
            modifier = Modifier.size(100.dp, 56.dp),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text("Reset", fontSize = 18.sp)
        }
    }
}

/**
 * Small circular button used to quickly set the timer duration.
 */
@Composable
fun TimerSetButton(label: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
        modifier = Modifier.size(64.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(label, fontWeight = FontWeight.Medium)
        }
    }
}
