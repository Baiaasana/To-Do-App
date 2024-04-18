package com.example.to_do.ui.add_edit
sealed class AddEditTodoEvent {
    //for data
    data class OnTitleChange(val title:String): AddEditTodoEvent()
    data class OnDescriptionChange(val desc:String): AddEditTodoEvent()
    // for clicks
    object OnSaveTodoClick: AddEditTodoEvent()
}