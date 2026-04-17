package com.example.gitutility.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.keepScreenOn
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gitutility.components.GlassCard
import com.example.gitutility.viewmodel.TaskViewModel

/**
 * NavItem defines the different sections of the app that can be navigated to.
 */
sealed class NavItem(val title: String, val icon: ImageVector, val index: Int) {
    object Home : NavItem("Home", Icons.Default.Home, 0)
    object Convert : NavItem("Convert", Icons.Default.CurrencyExchange, 1)
    object Calculator : NavItem("Calculator", Icons.Default.Calculate, 2)
    object Notes : NavItem("Notes & Tasks", Icons.AutoMirrored.Filled.Notes, 3)
    object Timer : NavItem("Timer", Icons.Default.Timer, 4)
}

/**
 * MainScreen is the root composable that handles navigation and orientation changes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    // rememberSaveable ensures the selected tab is remembered even if the screen rotates
    var selectedNavItem by rememberSaveable { mutableIntStateOf(0) }
    var selectedConvertTabIndex by rememberSaveable { mutableIntStateOf(0) }
    var selectedNoteTabIndex by rememberSaveable { mutableIntStateOf(0) }

    val navItems = listOf(
        NavItem.Home,
        NavItem.Convert,
        NavItem.Calculator,
        NavItem.Notes,
        NavItem.Timer
    )

    // BoxWithConstraints allows us to check the available width and height to adapt the UI
    BoxWithConstraints(modifier = Modifier
        .fillMaxSize()
        .keepScreenOn()
    ) {
        // We consider it landscape if width is greater than height
        val isLandscape = maxWidth > maxHeight

        Row(modifier = Modifier.fillMaxSize()) {
            // In landscape, we use a NavigationRail on the side instead of a bottom bar
            if (isLandscape) {
                NavigationRail(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    navItems.forEach { item ->
                        NavigationRailItem(
                            selected = selectedNavItem == item.index,
                            onClick = { selectedNavItem = item.index },
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title, fontSize = 10.sp) }
                        )
                    }
                }
            }

            // Scaffold provides basic Material Design layout structure (TopBar, BottomBar, etc.)
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                when (selectedNavItem) {
                                    0 -> "Smart Toolkit"
                                    1 -> "Converter"
                                    2 -> "Calculator"
                                    3 -> "Notes & Tasks"
                                    4 -> "Timer & Stopwatch"
                                    else -> "Utilities"
                                },
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                },
                // In portrait, we use a standard NavigationBar at the bottom
                bottomBar = {
                    if (!isLandscape) {
                        NavigationBar {
                            navItems.forEach { item ->
                                NavigationBarItem(
                                    selected = selectedNavItem == item.index,
                                    onClick = { selectedNavItem = item.index },
                                    icon = { Icon(item.icon, contentDescription = item.title) },
                                    label = { Text(item.title, fontSize = 10.sp) }
                                )
                            }
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f) // Content fills the remaining space
            ) { padding ->
                // This Column switches between different feature screens
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    when (selectedNavItem) {
                        0 -> HomeScreen(
                            onNavigateToConvert = { selectedNavItem = 1 },
                            onNavigateToCalculator = { selectedNavItem = 2 },
                            onNavigateToNotes = { selectedNavItem = 3 },
                            onNavigateToTimer = { selectedNavItem = 4 }
                        )

                        1 -> ConverterConvertSection(
                            selectedTabIndex = selectedConvertTabIndex,
                            onTabSelected = { selectedConvertTabIndex = it }
                        )

                        2 -> CalculatorScreen(viewModel())
                        3 -> NoteConvertSection(
                            selectedTabIndex = selectedNoteTabIndex,
                            onTabSelected = { selectedNoteTabIndex = it }
                        )

                        4 -> TimerScreen(viewModel())
                    }
                }

            }
        }
    }
}

/**
 * Section for all the different converters (Units, Currency, Temperature).
 */
@Composable
fun ConverterConvertSection(selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf("Units", "Currency", "Temperature")

    Column(modifier = Modifier.fillMaxSize()) {
        // TabRow allows switching between categories at the top of the screen
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onTabSelected(index) },
                    text = {
                        Text(
                            title,
                            fontSize = 12.sp,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = Color.Gray
                )
            }
        }

        // Show the screen corresponding to the selected tab
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when (selectedTabIndex) {
                0 -> UnitConverterScreen(viewModel())
                1 -> CurrencyConverterScreen(viewModel())
                2 -> TemperatureScreen(viewModel())
            }
        }
    }
}

/**
 * Section for Notes and Tasks.
 */
@Composable
fun NoteConvertSection(selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    val tabbs = listOf("Notes", "Tasks")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            tabbs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { onTabSelected(index) },
                    text = {
                        Text(
                            title,
                            fontSize = 12.sp,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    selectedContentColor = MaterialTheme.colorScheme.primary,
                    unselectedContentColor = Color.Gray
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when (selectedTabIndex) {
                0 -> NotesScreen(viewModel())
                1 -> TaskScreen(viewModel())
            }
        }
    }
}

/**
 * The main Home dashboard with quick-access buttons to all features.
 */
@Composable
fun HomeScreen(
    onNavigateToConvert: () -> Unit,
    onNavigateToCalculator: () -> Unit,
    onNavigateToNotes: () -> Unit,
    onNavigateToTimer: () -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isLandscape = maxWidth > maxHeight

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Essential everyday utilities",
                fontSize = 24.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // In landscape, we use a grid to show buttons more efficiently
            if (isLandscape) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { HomeButton("Unit Converter","Length, Currency & Temperature", Icons.Default.CurrencyExchange, onNavigateToConvert) }
                    item { HomeButton("Calculator", "Basic arithmetic operations",Icons.Default.Calculate, onNavigateToCalculator) }
                    item { HomeButton("Notes & Tasks", "Quick notes & Tasks",Icons.AutoMirrored.Filled.Notes, onNavigateToNotes) }
                    item { HomeButton("Timer & Stopwatch", "Track time accurately",Icons.Default.Timer, onNavigateToTimer) }
                }
            } else {
                // In portrait, a simple vertical column is easier to use
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HomeButton("Unit Converter","Length, Currency & Temperature", Icons.Default.CurrencyExchange, onNavigateToConvert)
                    HomeButton("Calculator", "Basic arithmetic operations",Icons.Default.Calculate, onNavigateToCalculator)
                    HomeButton("Notes & Tasks", "Quick notes & Tasks",Icons.AutoMirrored.Filled.Notes, onNavigateToNotes)
                    HomeButton("Timer & Stopwatch", "Track time accurately",Icons.Default.Timer, onNavigateToTimer)
                }
            }
        }
    }
}

/**
 * A custom styled button used on the Home dashboard.
 */
@Composable
fun HomeButton(title: String, text: String, icon: ImageVector, onClick: () -> Unit) {
    /*GlassCard(
        modifier = Modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.primary,
                )
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(onClick = onClick)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.Top,
                   // horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Icon in a circular background
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Icon(icon, contentDescription = null, modifier = Modifier.size(60.dp))

                    }

                }
                Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }*/
    HomeCard(title = title,text = text,icon = icon, onClick = onClick )
}


@Composable
fun HomeCard(
    title: String, text: String,icon: ImageVector, onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onClick)
                .padding(24.dp)
        ) {
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.BottomStart),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}