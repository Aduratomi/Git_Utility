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

class TimerViewModel : ViewModel() {
    var stopwatchTime by mutableLongStateOf(0L)
        private set
    
    var isStopwatchRunning by mutableStateOf(false)
        private set
    
    private var stopwatchJob: Job? = null

    var timerTime by mutableLongStateOf(0L) 
        private set
    
    var initialTimerTime by mutableLongStateOf(0L)
        private set
    
    var isTimerRunning by mutableStateOf(false)
        private set
    
    private var timerJob: Job? = null

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

    fun pauseStopwatch() {
        isStopwatchRunning = false
        stopwatchJob?.cancel()
    }

    fun resetStopwatch() {
        pauseStopwatch()
        stopwatchTime = 0L
    }

    fun setTimer(minutes: Int, seconds: Int) {
        timerTime = (minutes * 60 + seconds) * 1000L
        initialTimerTime = timerTime
    }

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

    fun pauseTimer() {
        isTimerRunning = false
        timerJob?.cancel()
    }

    fun resetTimer() {
        pauseTimer()
        timerTime = initialTimerTime
    }

    fun clearTimer() {
        pauseTimer()
        timerTime = 0L
        initialTimerTime = 0L
    }

    fun formatTime(timeMillis: Long): String {
        val totalSeconds = timeMillis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        val millis = (timeMillis % 1000) / 10
        return String.format("%02d:%02d.%02d", minutes, seconds, millis)
    }

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
