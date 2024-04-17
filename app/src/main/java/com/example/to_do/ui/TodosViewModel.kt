package com.example.to_do.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_do.data.Todo
import com.example.to_do.data.TodoRepository
import com.example.to_do.util.Routes
import com.example.to_do.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor(
    val repository: TodoRepository
): ViewModel() {

    val todos = repository.getTodos()
    private var removedTodo: Todo? = null

    // one-time events, channel use to handle them like clicks, navigate, snackBars

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: ToDosEvent){
        when(event){
            is ToDosEvent.OnTouchTodo -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO + "todoId=${event.todo.id}"))
            }

            is ToDosEvent.DeleteTodo -> {
                viewModelScope.launch {
                    removedTodo = event.todo
                    repository.removeTodo(event.todo)
                    sendUiEvent(UiEvent.ShowSnackBar("Todo removed", "Undo"))
                }

            }
            is ToDosEvent.OnAddToDo -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TODO))
            }
            is ToDosEvent.OnDoneChange -> {
                viewModelScope.launch {
                    repository.insertTodo(
                        event.todo.copy(
                            isDone =  event.isDone
                        )
                    )
                }

            }
            is ToDosEvent.OnUndoRemoveClick -> {
                removedTodo?.let { todo ->
                    viewModelScope.launch {
                        repository.insertTodo(todo)
                    }
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }




}