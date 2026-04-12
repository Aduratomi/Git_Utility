package com.example.gitutility.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.keepScreenOn
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gitutility.components.GlassCard

sealed class NavItem(val title: String, val icon: ImageVector, val index: Int) {
    object Home : NavItem("Home", Icons.Default.Home, 0)
    object Convert : NavItem("Convert", Icons.Default.CurrencyExchange, 1)
    object Calculator : NavItem("Calculator", Icons.Default.Calculate, 2)
    object Notes : NavItem("Notes", Icons.AutoMirrored.Filled.Notes, 3)
    object Timer : NavItem("Timer", Icons.Default.Timer, 4)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedNavItem by rememberSaveable { mutableIntStateOf(0) }
    var selectedConvertTabIndex by rememberSaveable { mutableIntStateOf(0) }

    val navItems = listOf(
        NavItem.Home,
        NavItem.Convert,
        NavItem.Calculator,
        NavItem.Notes,
        NavItem.Timer
    )

    BoxWithConstraints(modifier = Modifier
        .fillMaxSize()
        .keepScreenOn()
    ) {
        val isLandscape = maxWidth > maxHeight

        Row(modifier = Modifier.fillMaxSize()) {
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

            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                when (selectedNavItem) {
                                    0 -> "Smart Toolkit"
                                    1 -> "Converter"
                                    2 -> "Calculator"
                                    3 -> "Notes"
                                    4 -> "Timer & Stopwatch"
                                    else -> "Utilities"
                                },
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                },
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
                modifier = Modifier.weight(1f)
            ) { padding ->
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
                        1 -> ConvertSection(
                            selectedTabIndex = selectedConvertTabIndex,
                            onTabSelected = { selectedConvertTabIndex = it }
                        )
                        2 -> CalculatorScreen(viewModel())
                        3 -> NotesScreen(viewModel())
                        4 -> TimerScreen(viewModel())
                    }
                }
            }
        }
    }
}

@Composable
fun ConvertSection(selectedTabIndex: Int, onTabSelected: (Int) -> Unit) {
    val tabs = listOf("Units", "Currency", "Temperature")

    Column(modifier = Modifier.fillMaxSize()) {
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

            if (isLandscape) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item { HomeButton("Unit Converter", Icons.Default.CurrencyExchange, onNavigateToConvert) }
                    item { HomeButton("Calculator", Icons.Default.Calculate, onNavigateToCalculator) }
                    item { HomeButton("Notes", Icons.AutoMirrored.Filled.Notes, onNavigateToNotes) }
                    item { HomeButton("Timer & Stopwatch", Icons.Default.Timer, onNavigateToTimer) }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    HomeButton("Unit Converter", Icons.Default.CurrencyExchange, onNavigateToConvert)
                    HomeButton("Calculator", Icons.Default.Calculate, onNavigateToCalculator)
                    HomeButton("Notes", Icons.AutoMirrored.Filled.Notes, onNavigateToNotes)
                    HomeButton("Timer & Stopwatch", Icons.Default.Timer, onNavigateToTimer)
                }
            }
        }
    }
}

@Composable
fun HomeButton(title: String, icon: ImageVector, onClick: () -> Unit) {
    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onClick)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.size(56.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, modifier = Modifier.size(32.dp))
                }
            }
            Text(title, fontSize = 20.sp, fontWeight = FontWeight.Medium)
        }
    }
}
