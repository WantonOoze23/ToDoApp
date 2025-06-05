package com.tyshko.todoapp.vm.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyshko.domain.model.ToDoModel
import com.tyshko.domain.repository.ToDoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoEditViewModel @Inject constructor(
    private val repository: ToDoRepository
) : ViewModel() {


    private val _todoState = MutableStateFlow(ToDoState())
    val toDoState: StateFlow<ToDoState> = _todoState.asStateFlow()

    fun onIntent(intent: ToDoIntent){
        when(intent){
            is ToDoIntent.isToDoGet -> {
                if (_todoState.value.isToDoGet) return

                viewModelScope.launch {
                    val todo = repository.getCertainToDo(intent.id)
                    if (todo != null){
                        _todoState.update {
                            it.copy(
                                id = todo.id,
                                title = todo.title,
                                description = todo.description,
                                isCompleted = todo.isCompleted,
                                isToDoGet = true
                            )
                        }
                    }
                }
            }
            ToDoIntent.SavaToDo -> {
                viewModelScope.launch {
                    val id = _todoState.value.id
                    val title = _todoState.value.title
                    val description = _todoState.value.description
                    val isCompleted = _todoState.value.isCompleted

                    if (title.isBlank() || description.isBlank()) return@launch

                    val todo = ToDoModel(
                        id = id,
                        title = title,
                        description = description,
                        isCompleted = isCompleted
                    )
                    repository.insertToDo(todo)
                }
            }
            is ToDoIntent.SetCompleted -> {
                _todoState.update { it.copy(
                    isCompleted = intent.isCompleted
                ) }
            }
            is ToDoIntent.SetDescription -> {
                _todoState.update { it.copy(
                    description = intent.description
                ) }
            }
            is ToDoIntent.SetTitle -> {
                _todoState.update { it.copy(
                    title = intent.title
                ) }
            }
        }
    }

}