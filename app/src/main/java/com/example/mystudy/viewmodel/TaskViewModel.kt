package com.example.mystudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystudy.model.Task
import com.example.mystudy.model.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
//viewmodel class, it will be used to manage tasks in home page
class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {
    val tasks: StateFlow<List<Task>> = repository.allTasks
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    fun addTask(name: String) {
        viewModelScope.launch {
            repository.insert(Task(name = name))
        }
    }


}