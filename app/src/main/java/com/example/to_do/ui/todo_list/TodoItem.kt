package com.example.to_do.ui.todo_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.to_do.R
import com.example.to_do.data.Todo

@Composable
fun TodoItem(
    todo: Todo,
    onEvent: (ToDosEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.card_background))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = todo.title,
                        color = colorResource(id = R.color.text_primary),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                todo.description?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.text_secondary),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            Checkbox(
                checked = todo.isDone,
                onCheckedChange = { isCheck ->
                    onEvent(ToDosEvent.OnDoneChange(todo, isCheck))
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = colorResource(id = R.color.text_secondary),
                    uncheckedColor = colorResource(id = R.color.text_secondary),
                    checkmarkColor = colorResource(id = R.color.card_background),
                ),
            )
        }
    }
}