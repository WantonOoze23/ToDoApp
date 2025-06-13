package com.tyshko.todoapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tyshko.todoapp.vm.mvi.ToDoEditViewModel
import com.tyshko.todoapp.vm.mvi.ToDoIntent

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("RememberReturnType")
@Composable
fun ViewEditScreen(
    modifier: Modifier = Modifier,
    toDoId: Long?,
    viewModel: ToDoEditViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.toDoState.collectAsState()
    val showDialog = remember { mutableStateOf(false) }

    LaunchedEffect(toDoId) {
        toDoId?.let {
            viewModel.onIntent(ToDoIntent.isToDoGet(it))
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            confirmButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("OK")
                }
            },
            title = { Text("Error") },
            text = { Text("Fill in all fields.") }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding()
    ) {
        IconButton(
            onClick = {navController.popBackStack()}
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                tint = Color.Black,
                contentDescription = "Turn Back",
            )
        }
        OutlinedTextField(
            value = state.title,
            onValueChange = { viewModel.onIntent(ToDoIntent.SetTitle(it)) },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.description,
            onValueChange = { viewModel.onIntent(ToDoIntent.SetDescription(it)) },
            label = { Text("Description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Checkbox(
                checked = state.isCompleted,
                onCheckedChange = {
                    viewModel.onIntent(ToDoIntent.SetCompleted(it))
                }
            )
            Text(
                text = "Mark as done",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (state.title.isBlank() || state.description.isBlank()) {
                    showDialog.value = true
                } else {
                    viewModel.onIntent(ToDoIntent.SavaToDo)
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}