package com.example.to_do.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: Todo)

    @Delete
    suspend fun removeTodo(todo: Todo)

    @Query("SELECT * FROM todo WHERE id = :id")
    suspend fun getTodoById(id:Int): Todo?

    @Query("SELECT * FROM todo")
    fun getTodos(): Flow<List<Todo>>
}