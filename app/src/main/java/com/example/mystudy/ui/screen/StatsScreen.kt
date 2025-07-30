package com.example.mystudy.ui.screen
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mystudy.model.Task
import com.example.mystudy.viewmodel.TaskViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun StatsScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState()

    val taskStats = remember(tasks) {
        tasks.groupBy { it.date }
            .mapValues { it.value.size }
            .toList()
            .sortedByDescending { it.second }
    }

    val totalTasks = tasks.size
    val avgTasks = if (taskStats.isNotEmpty()) totalTasks / taskStats.size else 0
    val mostActive = taskStats.maxByOrNull { it.second }?.first ?: "N/A"

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF0F0F0))
        .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {

        Text("📊 Task Stats", style = MaterialTheme.typography.headlineMedium)

        Text("Total Tasks: $totalTasks")
        Text("Average per Day: $avgTasks")
        Text("Most Active Day: $mostActive")

        Divider()

        Text("Recent Activity:")

        taskStats.take(7).forEach { (date, count) ->
            Text("- $date: $count task(s)")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("💡 Tip: Try to keep tasks under 5 per day to avoid burnout!",
            style = MaterialTheme.typography.bodySmall)
    }
}
