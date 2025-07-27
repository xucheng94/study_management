package com.example.mystudy.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao{
    @Insert
    suspend fun insertTask(task: Task)

    @Query("SELECT*FROM tasks ORDER BY id DESC")
    fun getAllTasks():Flow<List<Task>>

    @Query("DELETE FROM tasks")
    suspend fun deleteAll()

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Int)

    @Query("SELECT * FROM tasks WHERE date = :targetDate ORDER BY date ASC")
    fun getTasksByDate(targetDate: String): Flow<List<Task>>


}