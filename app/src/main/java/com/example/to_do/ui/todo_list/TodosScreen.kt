package com.example.to_do.ui.todo_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.to_do.R
import com.example.to_do.util.UiEvent
import kotlinx.coroutines.flow.collectLatest

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
                    val result =
                        scaffoldState.showSnackbar(message = it.message, actionLabel = it.action)
                    when (result) {
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.background))
                .padding(padding)
        ) {
            items(todos.value) { todo ->
                TodoItem(
                    todo = todo,
                    onEvent = viewModel::onEvent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.onEvent(ToDosEvent.OnTouchTodo(todo))
                        }
                )
            }
        }
    }

}