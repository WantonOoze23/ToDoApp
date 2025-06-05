package com.tyshko.todoapp.vm.mvi

data class ToDoState(
    val id: Long = 0L,
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false,
    val isToDoGet: Boolean = false
)
