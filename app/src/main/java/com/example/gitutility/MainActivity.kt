package com.example.gitutility

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.gitutility.screens.MainScreen
import com.example.gitutility.ui.theme.GitUtilityTheme

/**
 * MainActivity is the primary entry point of the Android application.
 * In a Jetpack Compose app, this activity usually serves as a host for all composable screens.
 */
class MainActivity : ComponentActivity() {
    /**
     * onCreate is called when the activity is first created.
     * This is where initial setup like setting the content UI happens.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        /**
         * enableEdgeToEdge enables the app to draw content from the very top to the very bottom
         * of the screen, including behind the status bar and navigation bar.
         */
        enableEdgeToEdge()
        
        /**
         * setContent defines the activity's layout using composable functions.
         * Here, we call MainScreen, which coordinates the app's overall navigation and layout.
         */
        setContent {
            GitUtilityTheme  { MainScreen() }
        }
    }
}
