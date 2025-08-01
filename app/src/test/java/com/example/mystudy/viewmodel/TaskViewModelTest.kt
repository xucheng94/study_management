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
        testScheduler.advanceUntilIdle()
    }

    @Test
    fun addTask_should_add_task_to_list() = runTest(testScheduler) {
        val initialCount = viewModel.tasks.value.size
        viewModel.addTask("Test Task", "2025-08-01")
        testScheduler.advanceUntilIdle()
        assertEquals(initialCount + 1, viewModel.tasks.value.size)
        assertTrue(viewModel.tasks.value.any { it.name == "Test Task" }) // 检查内容
    }

    @Test
    fun deleteTask_should_remove_task() = runTest(testScheduler) {
        viewModel.addTask("Task to delete", "2025-08-01")
        testScheduler.advanceUntilIdle()
        val taskToDelete = viewModel.tasks.value.last()
        viewModel.deleteTask(taskToDelete)
        testScheduler.advanceUntilIdle()
        assertFalse(viewModel.tasks.value.contains(taskToDelete))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}