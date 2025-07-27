package com.example.mystudy.model

import androidx.room.Entity
import androidx.room.PrimaryKey
//create a task table
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int =0,
    val name : String,
    val date: String
)