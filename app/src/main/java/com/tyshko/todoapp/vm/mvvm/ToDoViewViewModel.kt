package com.tyshko.todoapp.vm.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tyshko.domain.model.ToDoModel
import com.tyshko.domain.repository.ToDoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoViewViewModel @Inject constructor(
    private val repository: ToDoRepository
) : ViewModel() {

    private var _todos = MutableStateFlow<List<ToDoModel>>(emptyList())
    val todos: StateFlow<List<ToDoModel>> = _todos

    private var _publicIP = MutableStateFlow("No ip found")
    val publicIP: StateFlow<String> = _publicIP

    init {
        viewModelScope.launch {
            try {
                _publicIP.value = repository.getUserIP()
            } catch (_: Exception){
                _publicIP.value = "Unable to get current IP"
            }
        }
        viewModelScope.launch {
            repository.getToDos().collect{ todos ->
                _todos.value = todos
            }
        }
    }

    fun deleteToDO(id: Long){
        viewModelScope.launch {
            repository.deleteToDO(id)
        }
    }

    fun onCheckClick(toDoModel: ToDoModel) {
        viewModelScope.launch {
            val updatedToDo = toDoModel.copy(isCompleted = !toDoModel.isCompleted)
            repository.updateToDo(updatedToDo)
        }
    }


}