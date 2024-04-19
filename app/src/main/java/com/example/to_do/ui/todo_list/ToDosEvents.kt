package com.example.to_do.ui.todo_list

import com.example.to_do.data.Todo

sealed class ToDosEvent {
    data class DeleteTodo(val todo: Todo) : ToDosEvent()
    data class OnDoneChange(val todo: Todo, val isDone: Boolean) : ToDosEvent()
    object OnUndoRemoveClick : ToDosEvent()
    data class OnTouchTodo(val todo: Todo) : ToDosEvent()
    object OnAddToDo : ToDosEvent()

}