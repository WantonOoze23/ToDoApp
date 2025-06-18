package com.tyshko.todoapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tyshko.todoapp.ui.components.ToDoCard
import com.tyshko.todoapp.vm.mvvm.ToDoViewViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ToDosScreen(
    viewModel: ToDoViewViewModel = hiltViewModel(),
    navController: NavController,
) {
    val toDoList by viewModel.todos.collectAsState()
    val userIP by viewModel.publicIP.collectAsState()

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                containerColor = Color.Red,
                onClick = {
                    navController.navigate("todo")
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add ToDo",
                    tint = Color.Black
                )
            }
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
        ) {
            Text(
                text = "IP: $userIP",
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(8.dp)
            )

            val scope = rememberCoroutineScope()

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(toDoList, key = { it.id }) { todo ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { dismissValue ->
                            if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                                scope.launch {
                                    viewModel.deleteToDo(todo.id)
                                }
                                true
                            } else {
                                false
                            }
                        }
                    )

                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                                    .background(Color.Red, shape = RoundedCornerShape(16.dp)),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Text(
                                    text = "Delete",
                                    color = Color.White,
                                    modifier = Modifier.padding(end = 24.dp)
                                )
                            }
                        },
                        modifier = Modifier.animateItemPlacement()
                    ) {
                        ToDoCard(
                            todo = todo,
                            onEdit = {
                                navController.navigate("todo?todoId=${todo.id}")
                            },
                            onCheckedChange = { viewModel.onCheckClick(todo) }
                        )
                    }
                }
            }
        }
    }
}