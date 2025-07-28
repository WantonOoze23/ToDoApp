package com.tyshko.todoapp.ui.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.tyshko.domain.model.ToDoModel
import com.tyshko.todoapp.vm.mvvm.ToDoViewViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ToDosScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var viewModel: ToDoViewViewModel
    private var testTodos = listOf(

        ToDoModel(
            id = 1L,
            title = "First todo title",
            description = "First todo description",
            isCompleted = false
        ),
        ToDoModel(
            id = 2L,
            title = "Second todo title",
            description = "Second todo description",
            isCompleted = true
        ),
        ToDoModel(
            id = 3L,
            title = "Todo to delete",
            description = "Third todo description",
            isCompleted = false
        )

    )

    @Before
    fun setUp() {
        viewModel = mockk(relaxed = true) {
            coEvery { deleteToDo(any()) } returns Unit
        }

        val state = MutableStateFlow(testTodos)
        val testUserIP = MutableStateFlow("12.34.56.78")

        every{ viewModel.todos } returns state
        every { viewModel.publicIP } returns testUserIP

    }

    private fun setContent() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        composeRule.setContent {
            ToDosScreen(
                viewModel = viewModel,
                navController = navController
            )
        }
    }

    @Test
    fun toDosScreen_showToDos() {
        setContent()

        composeRule.onNodeWithText("First todo title").assertExists()
        composeRule.onNodeWithText("Second todo title").assertExists()
        composeRule.onNodeWithText("Todo to delete").assertExists()

        composeRule.onNodeWithText("IP: 12.34.56.78").assertExists()
    }

    @Test
    fun toDosScreen_swipeToDelete_callsDelete() {
        setContent()

        composeRule.onNodeWithText("Todo to delete")
            .performTouchInput {
                swipeLeft(startX = right - 10f, endX = left + 10f, durationMillis = 300)
            }

        verify {
            viewModel.deleteToDo(3L)
        }
    }

    @Test
    fun toDosScreen_makeToDoDone(){
        setContent()

        composeRule.onNodeWithContentDescription("CheckBox1").performClick()

        verify {
            viewModel.onCheckClick(
                withArg { todo ->
                    assert(todo.id == 1L)
                }
            )
        }
    }

}