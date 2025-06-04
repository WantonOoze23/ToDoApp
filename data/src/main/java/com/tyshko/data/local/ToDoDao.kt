package com.tyshko.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tyshko.data.local.entity.ToDoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoDao{

    @Query("SELECT * FROM todos ORDER BY id DESC")
    fun getToDos(): Flow<List<ToDoEntity>>

    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getCertainToDO(id: Long): ToDoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDO(todo: ToDoEntity): Long

    @Update
    suspend fun updateToDo(todo: ToDoEntity): Int

    @Query("DELETE FROM todos WHERE id = :id")
    suspend fun deleteToDO(id: Long): Int

}