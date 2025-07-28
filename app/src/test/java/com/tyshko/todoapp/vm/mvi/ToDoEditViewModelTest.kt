package com.tyshko.todoapp.vm.mvi

import com.tyshko.domain.model.ToDoModel
import com.tyshko.domain.repository.ToDoRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ToDoEditViewModelTest {

    private lateinit var viewModel: ToDoEditViewModel
    private val repository: ToDoRepository = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = ToDoEditViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onIntent isToDoGet - updates state with todo`() = runTest {
        val todo = ToDoModel(
            id = 1,
            title = "Test title",
            description = "Test description",
            isCompleted = true
        )
        coEvery { repository.getCertainToDo(1) } returns todo

        viewModel.onIntent(ToDoIntent.isToDoGet(1))

        advanceUntilIdle()

        val state = viewModel.toDoState.value
        assertEquals(todo.id, state.id)
        assertEquals(todo.title, state.title)
        assertEquals(todo.description, state.description)
        assertEquals(todo.isCompleted, state.isCompleted)
        assertTrue(state.isToDoGet)
    }

    @Test
    fun `onIntent SetTitle - updates title in state`() = runTest {
        viewModel.onIntent(ToDoIntent.SetTitle("New title"))
        val state = viewModel.toDoState.value
        assertEquals("New title", state.title)
    }

    @Test
    fun `onIntent SavaToDo - calls insertToDo with valid data`() = runTest {
        viewModel.onIntent(ToDoIntent.SetTitle("Hello"))
        viewModel.onIntent(ToDoIntent.SetDescription("World"))
        viewModel.onIntent(ToDoIntent.SetCompleted(true))

        viewModel.onIntent(ToDoIntent.SavaToDo)

        advanceUntilIdle()

        coVerify {
            repository.insertToDo(
                match {
                    it.title == "Hello" && it.description == "World" && it.isCompleted
                }
            )
        }
    }

    @Test
    fun `onIntent SavaToDo - does not insert when title or description is blank`() = runTest {
        viewModel.onIntent(ToDoIntent.SetTitle(""))
        viewModel.onIntent(ToDoIntent.SetDescription(""))

        viewModel.onIntent(ToDoIntent.SavaToDo)

        advanceUntilIdle()

        coVerify(exactly = 0) { repository.insertToDo(any()) }
    }
}
