package com.example.mystudy.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
//viewmodel class, it will be used to manage tasks in home page
class TaskViewModel: ViewModel(){
    private val _tasks=mutableStateListOf("report","meeting","assignment")
    val tasks=_tasks
    fun addTasks(task: String){
        _tasks.add(task)

    }
    fun removeTask(task: String){
        _tasks.remove(task)

    }


}
