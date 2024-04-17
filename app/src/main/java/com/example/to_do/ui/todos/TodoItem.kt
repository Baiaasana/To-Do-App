package com.example.to_do.ui.todos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.to_do.data.Todo
import com.example.to_do.ui.ToDosEvent

@Composable
fun TodoItem(
    todo: Todo,
    onEvent: (ToDosEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = todo.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = {
                    onEvent(ToDosEvent.DeleteTodo(todo))
                }) {
                    Icon(
                        imageVector = Icons.Default.Delete, contentDescription = "Remove"
                    )
                }
            }
            todo.description?.let {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = it,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        Checkbox(
            checked = todo.isDone,
            onCheckedChange = { isCheck ->
                onEvent(ToDosEvent.OnDoneChange(todo, isCheck))
            }
        )
    }


}