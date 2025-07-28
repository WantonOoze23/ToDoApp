package com.tyshko.todoapp.vm.mvvm

import com.tyshko.domain.model.ToDoModel
import com.tyshko.domain.repository.ToDoRepository
import io.mockk.Awaits
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ToDoViewViewModelTest {

    private var repository: ToDoRepository = mockk(relaxed = true)
    private lateinit var toDoViewViewModel: ToDoViewViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `init - loads todos and ip successfully`() = runTest {
        val todos = listOf(
            ToDoModel(1, "Test 1", "Desc 1", false),
            ToDoModel(2, "Test 2", "Desc 2", true)
        )

        coEvery { repository.getUserIP() } returns "1.2.3.4"
        every { repository.getToDos() } returns flowOf(todos)

        toDoViewViewModel = ToDoViewViewModel(repository)
        advanceUntilIdle()

        assertEquals("1.2.3.4", toDoViewViewModel.publicIP.value)
        assertEquals(todos, toDoViewViewModel.todos.value)
    }

    @Test
    fun `init - handles IP exception gracefully`() = runTest {
        coEvery { repository.getUserIP() } throws RuntimeException("fail")
        every { repository.getToDos() } returns flowOf(emptyList())

        toDoViewViewModel = ToDoViewViewModel(repository)
        advanceUntilIdle()

        assertEquals("Unable to get current IP", toDoViewViewModel.publicIP.value)
        assertEquals(emptyList<ToDoModel>(), toDoViewViewModel.todos.value)
    }

    @Test
    fun `deleteToDo - calls repository with correct ID`() = runTest {
        coEvery { repository.deleteToDO(any()) } just Awaits
        every { repository.getToDos() } returns flowOf(emptyList())
        coEvery { repository.getUserIP() } returns "1.2.3.4"

        toDoViewViewModel = ToDoViewViewModel(repository)
        advanceUntilIdle()

        toDoViewViewModel.deleteToDo(42)
        advanceUntilIdle()

        coVerify { repository.deleteToDO(42) }
    }

    @Test
    fun `onCheckClick - toggles isCompleted and updates todo`() = runTest {
        val todo = ToDoModel(1, "Test", "Desc", isCompleted = false)
        val expected = todo.copy(isCompleted = true)

        coEvery { repository.updateToDo(expected) } just Awaits
        every { repository.getToDos() } returns flowOf(listOf(todo))
        coEvery { repository.getUserIP() } returns "1.2.3.4"

        toDoViewViewModel = ToDoViewViewModel(repository)
        advanceUntilIdle()

        toDoViewViewModel.onCheckClick(todo)
        advanceUntilIdle()

        coVerify { repository.updateToDo(expected) }
    }
}