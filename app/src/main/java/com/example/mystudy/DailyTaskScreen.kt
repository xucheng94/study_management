package com.example.mystudy
import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mystudy.viewmodel.TaskViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import com.example.mystudy.model.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyTaskScreen(viewModel: TaskViewModel) {

    val tasks by viewModel.tasks.collectAsState(initial = emptyList())

    var showDialog by remember { mutableStateOf(false) }
    var taskname by remember {mutableStateOf("")}
    var taskDate by remember { mutableStateOf("") }
    var filterDate by remember { mutableStateOf("") }

    val filteredTasks = if (filterDate.isNotBlank()) {
        tasks.filter { it.date == filterDate }
    } else {
        tasks
    }


    Scaffold(floatingActionButton = {FloatingActionButton(onClick = {showDialog=true})
    {Icon(Icons.Filled.Add, contentDescription = "Add task") }
    })
    {innerPadding
        ->Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
        OutlinedTextField(
            value = filterDate,
            onValueChange = { filterDate = it },
            label = { Text("Filter by Date (e.g. 2025-08-01)") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )
        Text(text = "today's tasks",
            style=MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
       TaskList(tasks=filteredTasks,onDeleteTask = { task -> viewModel.deleteTask(task) })

    }  }
    if (showDialog){
        AlertDialog(onDismissRequest = {showDialog=false}, title = {Text("Add Task")},
            text = {Column {
                OutlinedTextField(
                    value = taskname,
                    onValueChange = { taskname = it },
                    label = { Text("Task Name") },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = taskDate,
                    onValueChange = { taskDate = it },
                    label = { Text("Task Date") },
                    singleLine = true
                )
            }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (taskname.isNotBlank() && taskDate.isNotBlank()) {
                        viewModel.addTask(taskname, taskDate)
                        taskname = ""
                        taskDate = ""
                    }
                    showDialog = false
                }) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun TaskList(tasks: List<Task>, onDeleteTask: (Task) -> Unit) {
    if (tasks.isEmpty()) {
        Text("No tasks yet. Click + to add me")
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            tasks.forEach { task ->
                var showDeleteDialog by remember { mutableStateOf(false) }
                if (showDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = { showDeleteDialog = false },
                        confirmButton = {
                            TextButton(onClick = {
                                onDeleteTask(task)
                                showDeleteDialog = false
                            }) {
                                Text("Delete")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                showDeleteDialog = false
                            }) {
                                Text("Cancel")
                            }
                        },
                        title = { Text("Delete Task") },
                        text = { Text("Are you sure you want to delete this task?") }
                    )
                }

                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp).clickable{showDeleteDialog=true}

                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(task.name)

                        Text("Due: ${task.date}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
