package com.tyshko.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tyshko.domain.model.ToDoModel

@Entity("todos")
data class ToDoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String = "",
    val description: String = "",
    val isCompleted: Boolean = false
){
    fun toTodoModel(): ToDoModel {
        return ToDoModel(
            id = id,
            title = title,
            description = description,
            isCompleted = isCompleted
        )
    }

    companion object {
        fun fromToDoModel(model: ToDoModel): ToDoEntity {
            return ToDoEntity(
                id = model.id,
                title = model.title,
                description = model.description,
                isCompleted = model.isCompleted
            )
        }
    }
}
