package com.example.to_do.data

import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    suspend fun insertTodo(todo: Todo)

    suspend fun removeTodo(todo: Todo)

    suspend fun getTodoById(id:Int): Todo?

    fun getTodos(): Flow<List<Todo>>

}