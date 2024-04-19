package com.example.to_do.ui.add_edit

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.to_do.data.Todo
import com.example.to_do.data.TodoRepository
import com.example.to_do.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val repository: TodoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var todo by mutableStateOf<Todo?>(null)
        private set

    var title by mutableStateOf("")
        private set
    var description by mutableStateOf("")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    init {
        val todoId = savedStateHandle.get<Int>("todoId")!!
        if (todoId != -1) {
            viewModelScope.launch {
                repository.getTodoById(todoId)?.let { todo ->
                    title = todo.title
                    todo.description?.let { desc ->
                        description = desc
                    }
                    this@AddEditViewModel.todo = todo
                }

            }
        }
    }

    fun onEvent(event: AddEditTodoEvent) {

        when (event) {
            is AddEditTodoEvent.OnDescriptionChange -> {
                description = event.desc

            }

            AddEditTodoEvent.OnSaveTodoClick -> {
                viewModelScope.launch {
                    if (title.isBlank()) {
                        sendUiEvent(UiEvent.ShowSnackBar("The title can't be empty"))
                        return@launch
                    }
                    repository.insertTodo(Todo(title, description, todo?.isDone ?: false, todo?.id))
                    sendUiEvent(UiEvent.PopBackStack)
                }
            }

            is AddEditTodoEvent.OnTitleChange -> {
                title = event.title

            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}