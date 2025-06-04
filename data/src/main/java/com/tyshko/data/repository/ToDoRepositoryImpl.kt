package com.tyshko.data.repository

import android.util.Log
import com.tyshko.data.local.ToDoDao
import com.tyshko.data.local.entity.ToDoEntity
import com.tyshko.data.network.NetworkApi
import com.tyshko.domain.model.ToDoModel
import com.tyshko.domain.repository.ToDoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ToDoRepositoryImpl @Inject constructor(
    private val toDoDao: ToDoDao,
    private val networkApi: NetworkApi
) : ToDoRepository{
    override fun getToDos(): Flow<List<ToDoModel>> {
        return toDoDao.getToDos().map { toDoEntities ->
            toDoEntities.map {
                it.toTodoModel()
            }
        }
    }

    override suspend fun insertToDo(todo: ToDoModel): Boolean {
        return try {
            toDoDao.insertToDO(ToDoEntity.fromToDoModel(todo))
            true
        } catch (e: Exception){
            Log.e("DB","Unable to INSERT ToDo: $e")
            false
        }
    }

    override suspend fun getCertainToDo(id: Long): ToDoModel? {
        return toDoDao.getCertainToDO(id)?.toTodoModel()
    }

    override suspend fun updateToDo(todo: ToDoModel): Boolean {
        return try{
            toDoDao.updateToDo(ToDoEntity.fromToDoModel(todo))
            true
        } catch (e: Exception){
            Log.e("DB","Unable to UPDATE ToDo: $e")
            false
        }
    }

    override suspend fun deleteToDO(id: Long): Boolean {
        return try{
            toDoDao.deleteToDO(id)
            true
        } catch (e: Exception){
            Log.e("DB","Unable to DELETE ToDo: $e")
            false
        }
    }

    override suspend fun getUserIP(): String {
        return networkApi.getUserIP()
    }

}