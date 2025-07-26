package com.example.mystudy.model
import kotlinx.coroutines.flow.Flow

class TaskRepository (private val taskDao: TaskDao){
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()
    suspend fun insert(task: Task){
        taskDao.insertTask(task)
    }

    suspend fun clearAll(){
        taskDao.deleteAll()
    }
}