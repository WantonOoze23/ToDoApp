package com.tyshko.domain.repository

import com.tyshko.domain.model.ToDoModel
import kotlinx.coroutines.flow.Flow

interface ToDoRepository {
    fun getToDos(): Flow<List<ToDoModel>>
    suspend fun insertToDo(todo: ToDoModel): Boolean
    suspend fun getCertainToDo(id: Long): ToDoModel?
    suspend fun updateToDo(todo: ToDoModel): Boolean
    suspend fun deleteToDO(id: Long): Boolean
    suspend fun getUserIP(): String
}