package com.tyshko.todoapp.vm.mvi

sealed interface ToDoIntent{
    data class SetTitle(val title: String) : ToDoIntent
    data class SetDescription(val description: String) : ToDoIntent
    data class SetCompleted(val isCompleted: Boolean) : ToDoIntent
    data object SavaToDo : ToDoIntent
    data class isToDoGet(val id: Long): ToDoIntent
}