package com.tyshko.domain.model


data class ToDoModel(
    val id: Long,
    val title: String,
    val description: String,
    val isCompleted: Boolean = false
)
