package com.example.to_do

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.to_do.ui.add_edit.AddEditTodoScreen
import com.example.to_do.ui.theme.ToDoTheme
import com.example.to_do.ui.todo_list.TodosScreen
import com.example.to_do.util.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ToDoTheme {

                // navigation for these two screens
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Routes.TODO_LIST
                ) {
                    composable(
                        route = Routes.TODO_LIST
                    ) {
                        TodosScreen(
                            onNavigate = { event ->
                                navController.navigate(event.route)
                            }
                        )
                    }
                    composable(
                        route = Routes.ADD_EDIT_TODO + "?todoId={todoId}",
                        arguments = listOf(
                            navArgument(
                                name = "todoId",
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )

                    ) {
                        AddEditTodoScreen(
                            onPopBackStack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}

