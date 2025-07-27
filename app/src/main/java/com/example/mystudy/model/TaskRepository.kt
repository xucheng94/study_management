package com.example.mystudy.model
import kotlinx.coroutines.flow.Flow

class TaskRepository (private val taskDao: TaskDao){
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()
    suspend fun insert(task: Task){
        taskDao.insertTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTaskById(task.id)
    }

    fun getTasksByDate(date: String): Flow<List<Task>> {
        return taskDao.getTasksByDate(date)
    }


    suspend fun clearAll(){
        taskDao.deleteAll()
    }
}