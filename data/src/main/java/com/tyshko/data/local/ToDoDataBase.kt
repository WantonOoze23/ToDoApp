package com.tyshko.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tyshko.data.local.entity.ToDoEntity

@Database(
    entities = [ToDoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ToDoDataBase : RoomDatabase() {

    abstract fun toDoDao() : ToDoDao

}