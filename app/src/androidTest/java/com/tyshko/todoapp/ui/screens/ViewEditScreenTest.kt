package com.tyshko.todoapp.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.tyshko.todoapp.vm.mvi.ToDoEditViewModel
import com.tyshko.todoapp.vm.mvi.ToDoIntent
import com.tyshko.todoapp.vm.mvi.ToDoState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class ViewEditScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var viewModel: ToDoEditViewModel
    private lateinit var state: MutableStateFlow<ToDoState>
    private lateinit var onPopBackStack: () -> Unit

    @Before
    fun setUp() {
        state = MutableStateFlow(
            ToDoState(
                id = 1,
                title = "Test Title",
                description = "Test Description",
                isCompleted = false,
                isToDoGet = true
            )
        )

        viewModel = mockk(relaxed = true) {
            every { toDoState } returns state
        }

        onPopBackStack = {}
    }

    private fun setContent() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        composeRule.setContent {
            ViewEditScreen(
                toDoId = 1L,
                viewModel = viewModel,
                navController = navController
            )
        }
    }

    @Test
    fun editScreen_showsToDoData() {
        setContent()
        composeRule.onNodeWithText("Test Title").assertExists()
        composeRule.onNodeWithText("Test Description").assertExists()
    }

    @Test
    fun editScreen_canEditTitleAndDescription() {
        setContent()

        val newTitle = "Updated Title"
        val newDesc = "Updated Description"

        composeRule.onNodeWithText("Test Title").performTextReplacement(newTitle)

        composeRule.onNodeWithText("Test Description").performTextReplacement(newDesc)

        verify { viewModel.onIntent(ToDoIntent.SetTitle(newTitle)) }
        verify { viewModel.onIntent(ToDoIntent.SetDescription(newDesc)) }
    }

    @Test
    fun editScreen_toggleIsCompleted() {
        setContent()

        composeRule.onNodeWithContentDescription("Checkbox").performClick()

        verify { viewModel.onIntent(ToDoIntent.SetCompleted(true)) }
    }

    @Test
    fun editScreen_saveToDo_whenFieldsAreFilled() {
        setContent()

        composeRule.onNodeWithText("Save").performClick()

        verify { viewModel.onIntent(ToDoIntent.SavaToDo) }
    }
}