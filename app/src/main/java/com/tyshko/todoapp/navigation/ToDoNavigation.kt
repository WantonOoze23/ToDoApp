@file:Suppress("UNREACHABLE_CODE")

package com.tyshko.todoapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tyshko.todoapp.ui.screens.LoadingScreen
import com.tyshko.todoapp.ui.screens.ToDosScreen
import com.tyshko.todoapp.ui.screens.ViewEditScreen

@Composable
fun ToDoNavigation(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = "loading"
    ) {
        composable("loading"){
            LoadingScreen(
                onFinish = {
                    navController.navigate("main") {
                        popUpTo("loading") { inclusive = true }
                    }
                }
            )
        }
        composable("main"){
            ToDosScreen(
                onEdit = {},
                navController = navController,
            )
        }
        composable("todo?todoId={todoId}"){ backStackEntry ->
            val todoID = backStackEntry.arguments?.getString("todoId")?.toLongOrNull()
            ViewEditScreen(
                toDoId = todoID,
                navController = navController
            )
        }
    }
}