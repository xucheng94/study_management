package com.example.mystudy
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
    Scaffold(floatingActionButton = {FloatingActionButton(onClick = {showDialog=true})
    {Icon(Icons.Filled.Add, contentDescription = "Add task") }
    })
    {innerPadding
        ->Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
        Text(text = "today's tasks",
            style=MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))
       TaskList(tasks=tasks)

    }  }
    if (showDialog){
        AlertDialog(onDismissRequest = {showDialog=false}, title = {Text("Add Task")},
            text = {Column { OutlinedTextField(
                value = taskname,
                onValueChange = {taskname=it},
                label = {Text("Task Name")},
                singleLine = true
            )
            }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (taskname.isNotBlank()) {
                        viewModel.addTask(taskname)
                        taskname = ""
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
fun TaskList(tasks: List<Task>) {
    if (tasks.isEmpty()) {
        Text("No tasks yet. Click + to add me")
    } else {
        Column {
            tasks.forEach { task ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        Icon(Icons.Filled.CheckCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(task.name) // 注意：显示 Task 的 name 属性
                    }
                }
            }
        }
    }
}
