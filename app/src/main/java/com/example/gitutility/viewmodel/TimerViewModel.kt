package com.example.gitutility.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * TimerViewModel handles both the Stopwatch and the Countdown Timer logic.
 * It uses Coroutines to keep track of time in the background.
 */
class TimerViewModel : ViewModel() {
    // --- Stopwatch Logic ---

    // Total milliseconds elapsed on the stopwatch
    var stopwatchTime by mutableLongStateOf(0L)
        private set
    
    // Whether the stopwatch is currently ticking
    var isStopwatchRunning by mutableStateOf(false)
        private set
    
    // Background task reference for the stopwatch
    private var stopwatchJob: Job? = null

    // --- Timer Logic ---

    // Milliseconds remaining on the countdown timer
    var timerTime by mutableLongStateOf(0L)
        private set
    
    // The original time set by the user (needed for resetting)
    var initialTimerTime by mutableLongStateOf(0L)
        private set
    
    // Whether the countdown timer is active
    var isTimerRunning by mutableStateOf(false)
        private set
    
    // Background task reference for the timer
    private var timerJob: Job? = null

    /**
     * Starts the stopwatch. It increases 'stopwatchTime' every 10 milliseconds.
     */
    fun startStopwatch() {
        if (isStopwatchRunning) return
        isStopwatchRunning = true
        stopwatchJob = viewModelScope.launch {
            val startTime = System.currentTimeMillis() - stopwatchTime
            while (isActive) {
                stopwatchTime = System.currentTimeMillis() - startTime
                delay(10)
            }
        }
    }

    /**
     * Pauses the stopwatch.
     */
    fun pauseStopwatch() {
        isStopwatchRunning = false
        stopwatchJob?.cancel()
    }

    /**
     * Resets the stopwatch back to 0.
     */
    fun resetStopwatch() {
        pauseStopwatch()
        stopwatchTime = 0L
    }

    /**
     * Sets the countdown timer to a specific number of minutes and seconds.
     */
    fun setTimer(minutes: Int, seconds: Int) {
        timerTime = (minutes * 60 + seconds) * 1000L
        initialTimerTime = timerTime
    }

    /**
     * Starts the countdown timer. It decreases 'timerTime' every 100 milliseconds.
     */
    fun startTimer() {
        if (isTimerRunning || timerTime <= 0) return
        isTimerRunning = true
        timerJob = viewModelScope.launch {
            while (isActive && timerTime > 0) {
                delay(100)
                timerTime -= 100
                if (timerTime <= 0) {
                    timerTime = 0
                    isTimerRunning = false
                    break
                }
            }
        }
    }

    /**
     * Pauses the countdown timer.
     */
    fun pauseTimer() {
        isTimerRunning = false
        timerJob?.cancel()
    }

    /**
     * Resets the timer back to its starting value.
     */
    fun resetTimer() {
        pauseTimer()
        timerTime = initialTimerTime
    }

    /**
     * Stops and clears the timer entirely.
     */
    fun clearTimer() {
        pauseTimer()
        timerTime = 0L
        initialTimerTime = 0L
    }

    /**
     * Formats milliseconds into a standard "MM:SS.ms" stopwatch string.
     */
    fun formatTime(timeMillis: Long): String {
        val totalSeconds = timeMillis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val millis = (timeMillis % 1000) / 10
        return String.format("%02d:%02d.%02d", minutes, seconds, millis)
    }

    /**
     * Formats milliseconds into a standard "HH:MM:SS" or "MM:SS" timer string.
     */
    fun formatTimer(timeMillis: Long): String {
        val totalSeconds = timeMillis / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        
        return if (hours > 0) {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%02d:%02d", minutes, seconds)
        }
    }
}
