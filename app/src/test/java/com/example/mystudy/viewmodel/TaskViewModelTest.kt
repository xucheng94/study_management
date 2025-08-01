package com.example.mystudy.viewmodel

import com.example.mystudy.model.FakeTaskDao
import com.example.mystudy.model.Task
import com.example.mystudy.model.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModelTest {
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)
    private lateinit var viewModel: TaskViewModel
    private lateinit var fakeDao: FakeTaskDao

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeDao = FakeTaskDao()
        viewModel = TaskViewModel(TaskRepository(fakeDao))
    }

    @Test
    fun `addTask should add task to list`() = runTest(testScheduler) {
        // When
        viewModel.addTask("Test Task", "2025-08-01")
        testScheduler.advanceUntilIdle() // 更精确的控制

        // Then
        val tasks = viewModel.tasks.value
        println("Current tasks: $tasks") // 调试日志
        assertEquals(1, tasks.size)
        assertEquals("Test Task", tasks[0].name)
    }

    @Test
    fun `deleteTask should remove task`() = runTest(testScheduler) {
        // Given
        viewModel.addTask("Task to delete", "2025-08-01")
        testScheduler.advanceUntilIdle()
        val initialTasks = viewModel.tasks.value
        assertTrue(initialTasks.isNotEmpty())
        val task = initialTasks.first()

        // When
        viewModel.deleteTask(task)
        testScheduler.advanceUntilIdle()

        // Then
        assertEquals(0, viewModel.tasks.value.size)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}