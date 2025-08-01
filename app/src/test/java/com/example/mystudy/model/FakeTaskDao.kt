package com.example.mystudy.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeTaskDao : TaskDao {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    private var nextId = 1

    override fun getAllTasks(): Flow<List<Task>> = _tasks

    override suspend fun insertTask(task: Task) {
        _tasks.update { currentTasks ->
            currentTasks + task.copy(id = nextId++).also {
                println("Inserted task: $it") // 调试日志
            }
        }
    }

    override suspend fun deleteAll() {
        _tasks.value = emptyList()
    }

    override suspend fun deleteTaskById(taskId: Int) {
        _tasks.value = _tasks.value.filter { it.id != taskId }
    }

    override fun getTasksByDate(targetDate: String): Flow<List<Task>> {
        return _tasks.map { tasks -> tasks.filter { it.date == targetDate } }
    }
}