package com.tyshko.todoapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tyshko.todoapp.ui.components.ToDoCard
import com.tyshko.todoapp.vm.mvvm.ToDoViewViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ToDosScreen(
    modifier: Modifier = Modifier,
    viewModel: ToDoViewViewModel = hiltViewModel(),
    navController: NavController,
    onEdit: () -> Unit,
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

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(toDoList) { todo ->
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