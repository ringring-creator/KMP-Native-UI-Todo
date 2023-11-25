package com.ring.ring.kmptodo

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ring.ring.kmptodo.editTodo.EditTodoScreen
import com.ring.ring.kmptodo.todos.TodosScreen

@Composable
fun TodoNavGraph(navController: NavHostController, mainViewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = Screen.Todos.route) {
        composable(Screen.Todos.route) {
            TodosScreen(mainViewModel = mainViewModel) {
                navController.navigate(
                    Screen.EditTodo.createRoute(
                        it
                    )
                )
            }
        }
        composable(
            Screen.EditTodo.route,
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
                nullable = true
            })
        ) {
            EditTodoScreen(popBackStack = navController::popBackStack)
        }
    }
}

sealed class Screen(val route: String) {
    data object Todos : Screen("todos")
    data object EditTodo : Screen("todo/{${EDIT_ID_ARGS}}") {
        fun createRoute(id: Long?) = "todo/$id"
    }

    companion object {
        const val EDIT_ID_ARGS = "id"
    }
}