package com.example.to_do.ui.todo_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.to_do.R
import com.example.to_do.util.UiEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteBackground(
    swipeDismissState: DismissState,
) {

    val color = if (swipeDismissState.dismissDirection == DismissDirection.EndToStart) {
        colorResource(id = R.color.text_primary)
    } else {
        Color.Transparent
    }

    Surface(
        color = color,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 16.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Remove",
                tint = colorResource(id = R.color.white)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit,
) {
    var isRemoved by remember {
        mutableStateOf(false)
    }
    val dismissState = rememberDismissState(
        confirmValueChange = { newValue ->
            if (newValue == DismissValue.DismissedToStart) {
                isRemoved = true
                true
            } else {
                false
            }
        },
    )

    LaunchedEffect(key1 = isRemoved) {
        if (isRemoved) {
            delay(animationDuration.toLong())
            onDelete(item)
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismiss(
            state = dismissState,
            background = {
                DeleteBackground(
                    swipeDismissState = dismissState
                )
            },
            dismissContent = {
                content(item)
            },
            directions = setOf(DismissDirection.EndToStart)
        )
    }

    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == DismissValue.DismissedToStart) {
            dismissState.reset()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodosScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TodosViewModel = hiltViewModel()
) {

    val todos = viewModel.todos.collectAsState(initial = emptyList())
    val scaffoldState = remember { SnackbarHostState() }

    // LaunchEffect trigger only once, when screen show first try, so if we want action that run once not every time screen recreate, we use LaunchedEffect block
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collectLatest {
            when (it) {
                is UiEvent.ShowSnackBar -> {
                    when (scaffoldState.showSnackbar(
                        message = it.message,
                        actionLabel = it.action,
                        duration = SnackbarDuration.Short
                    )
                    ) {
                        SnackbarResult.ActionPerformed -> {
                            viewModel.onEvent(ToDosEvent.OnUndoRemoveClick)
                        }

                        SnackbarResult.Dismissed -> {
                            Unit
                        }
                    }
                }

                is UiEvent.Navigate -> {
                    onNavigate(it)
                }

                else -> Unit
            }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = colorResource(id = R.color.text_primary),
                ),
                title = {
                    Text("Daily Tasks")
                }
            )
        },
        snackbarHost = { SnackbarHost(scaffoldState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(ToDosEvent.OnAddToDo)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        },

        ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.background))
                .padding(padding)
        ) {
            items(
                items = todos.value,
                key = { it.id!! }
            ) { todo ->
                SwipeToDeleteContainer(
                    item = todo,
                    onDelete = {
                        viewModel.onEvent(ToDosEvent.DeleteTodo(todo))

                    },
                ) {
                    TodoItem(
                        todo = todo,
                        onEvent = viewModel::onEvent,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.onEvent(ToDosEvent.OnTouchTodo(todo))
                            }
                            .padding(vertical = 8.dp) // Adjust the padding as needed
                    )
                }
            }
        }
    }
}

