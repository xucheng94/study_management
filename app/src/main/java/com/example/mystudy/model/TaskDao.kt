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
}