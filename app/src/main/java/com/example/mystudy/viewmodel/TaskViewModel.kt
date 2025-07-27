package com.example.mystudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystudy.model.Task
import com.example.mystudy.model.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow

//viewmodel class, it will be used to manage tasks in home page
class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {
    private val _filteredTasks = MutableStateFlow<List<Task>>(emptyList())
    val filteredTasks: StateFlow<List<Task>> = _filteredTasks
    val tasks: StateFlow<List<Task>> = repository.allTasks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    fun addTask(name: String,date: String) {
        viewModelScope.launch {
            repository.insert(Task(name = name, date = date))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
    fun getTasksByDate(date: String) {
        viewModelScope.launch {
            repository.getTasksByDate(date).collect { list ->
                _filteredTasks.value = list
            }
        }
    }



}