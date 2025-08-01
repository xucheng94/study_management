package com.example.mystudy

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mystudy.model.Task
import com.example.mystudy.model.TaskDao
import com.example.mystudy.model.TaskDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class TaskDaoTest {

    private lateinit var db: TaskDatabase
    private lateinit var dao: TaskDao

    @Before
    fun createDb() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TaskDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.taskDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insert_and_read_task() = runBlocking {
        val task = Task(name = "Test Task", date = "2025-08-01")
        dao.insertTask(task)

        val result = dao.getAllTasks().first()
        assertEquals(1, result.size)
        assertEquals("Test Task", result[0].name)
    }

    @Test
    fun delete_task_by_id() = runBlocking {
        val task = Task(name = "To Delete", date = "2025-08-01")
        dao.insertTask(task)
        val inserted = dao.getAllTasks().first().first()
        dao.deleteTaskById(inserted.id)

        val result = dao.getAllTasks().first()
        assertTrue(result.isEmpty())
    }

    @Test
    fun get_tasks_by_date() = runBlocking {
        val task1 = Task(name = "Task A", date = "2025-08-01")
        val task2 = Task(name = "Task B", date = "2025-08-02")
        dao.insertTask(task1)
        dao.insertTask(task2)

        val result = dao.getTasksByDate("2025-08-01").first()
        assertEquals(1, result.size)
        assertEquals("Task A", result[0].name)
    }

    @Test
    fun clear_all_tasks() = runBlocking {
        dao.insertTask(Task(name = "T1", date = "2025-08-01"))
        dao.insertTask(Task(name = "T2", date = "2025-08-02"))

        dao.deleteAll()
        val result = dao.getAllTasks().first()
        assertTrue(result.isEmpty())
    }
}
