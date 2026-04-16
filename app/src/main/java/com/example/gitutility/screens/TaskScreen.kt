package com.example.gitutility.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gitutility.data.local.Task
import com.example.gitutility.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TaskScreen(viewModel: TaskViewModel = viewModel()) {
    val tasks by viewModel.tasks.collectAsState()
    val isAddingTask by viewModel.isAddingTask.collectAsState()
    val editingTask by viewModel.editingTask.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Tasks",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            IconButton(
                onClick = { viewModel.onAddTaskClick() },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .size(40.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Task",
                    tint = Color.White
                )
            }
        }

        if (tasks.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No tasks yet. Tap + to add one!",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tasks) { task ->
                    TaskItem(
                        task = task,
                        onToggle = { viewModel.toggleTaskCompletion(task) },
                        onDelete = { viewModel.deleteTask(task) },
                        onEdit = { viewModel.onEditTaskClick(task) }
                    )
                }
            }
        }
    }

    if (isAddingTask) {
        TaskDialog(
            onDismiss = { viewModel.onDismissAddTask() },
            onSave = { title, reminder -> viewModel.saveTask(title, reminder) }
        )
    }

    editingTask?.let { task ->
        TaskDialog(
            initialTitle = task.title,
            initialReminder = task.reminderTimestamp,
            onDismiss = { viewModel.onDismissEditTask() },
            onSave = { title, reminder -> viewModel.updateTask(task, title, reminder) }
        )
    }
}

@Composable
fun TaskItem(
    task: Task,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggle() }
            )
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    modifier = Modifier.clickable { onEdit() },
                    fontSize = 18.sp,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (task.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurface
                )
                if (task.reminderTimestamp != null) {
                    val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = sdf.format(Date(task.reminderTimestamp)),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDialog(
    initialTitle: String = "",
    initialReminder: Long? = null,
    onDismiss: () -> Unit,
    onSave: (String, Long?) -> Unit
) {
    var title by remember { mutableStateOf(initialTitle) }
    var reminderTimestamp by remember { mutableStateOf(initialReminder) }
    
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = reminderTimestamp ?: System.currentTimeMillis()
    )
    val timePickerState = rememberTimePickerState(
        initialHour = if (reminderTimestamp != null) {
            val cal = Calendar.getInstance().apply { timeInMillis = reminderTimestamp!! }
            cal.get(Calendar.HOUR_OF_DAY)
        } else 12,
        initialMinute = if (reminderTimestamp != null) {
            val cal = Calendar.getInstance().apply { timeInMillis = reminderTimestamp!! }
            cal.get(Calendar.MINUTE)
        } else 0
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialTitle.isEmpty()) "Add Task" else "Edit Task") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (reminderTimestamp != null) {
                            val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
                            "Reminder: ${sdf.format(Date(reminderTimestamp!!))}"
                        } else "No reminder set",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Set Reminder")
                    }
                }
                
                if (reminderTimestamp != null) {
                    TextButton(onClick = { reminderTimestamp = null }) {
                        Text("Clear Reminder", color = Color.Red)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(title, reminderTimestamp)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    showTimePicker = true
                }) { Text("Next") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val calendar = Calendar.getInstance()
                    datePickerState.selectedDateMillis?.let { calendar.timeInMillis = it }
                    calendar.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                    calendar.set(Calendar.MINUTE, timePickerState.minute)
                    reminderTimestamp = calendar.timeInMillis
                    showTimePicker = false
                }) { Text("OK") }
            },
            title = { Text("Select Time") },
            text = { 
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    TimePicker(state = timePickerState) 
                }
            }
        )
    }
}
