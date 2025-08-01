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
        fakeDao = FakeTaskDao().apply {
            // 初始插入一个测试任务确保流激活
            runTest(testScheduler) {
                insertTask(Task(id = 0, name = "Init Task", date = "2025-01-01"))
                testScheduler.advanceUntilIdle()
            }
        }
        viewModel = TaskViewModel(TaskRepository(fakeDao))
        testScheduler.advanceUntilIdle() // 等待初始收集完成
    }

    @Test
    fun `addTask should add task to list`() = runTest(testScheduler) {
        // 初始任务数
        val initialCount = viewModel.tasks.value.size

        // When
        viewModel.addTask("Test Task", "2025-08-01")
        testScheduler.advanceUntilIdle()

        // Then
        assertEquals(initialCount + 1, viewModel.tasks.value.size)
        assertTrue(viewModel.tasks.value.any { it.name == "Test Task" })
    }

    @Test
    fun `deleteTask should remove task`() = runTest(testScheduler) {
        // Given - 添加一个新任务
        viewModel.addTask("Task to delete", "2025-08-01")
        testScheduler.advanceUntilIdle()
        val taskToDelete = viewModel.tasks.value.last()

        // When
        viewModel.deleteTask(taskToDelete)
        testScheduler.advanceUntilIdle()

        // Then
        assertFalse(viewModel.tasks.value.contains(taskToDelete))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}