package com.tyshko.todoapp

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.tyshko.todoapp.vm.mvvm.ToDoViewViewModel

@Composable
fun ToDosScreen(
    viewModel: ToDoViewViewModel = hiltViewModel()
){
    val publicIP by viewModel.publicIP.collectAsState()
    val todos by viewModel.todos.collectAsState()
    Text(
        text = publicIP
    )

    Text(
        text = todos.toString()
    )

}