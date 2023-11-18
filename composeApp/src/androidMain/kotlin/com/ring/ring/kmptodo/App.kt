package com.ring.ring.kmptodo

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ring.ring.kmptodo.edit.EditTodoScreen
import com.ring.ring.kmptodo.todos.TodosScreen

@Composable
fun TodoApp(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Todos.route) {
        composable(Screen.Todos.route) {
            TodosScreen { navController.navigate(Screen.EditTodo.createRoute(it)) }
        }
        composable(
            Screen.EditTodo.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            EditTodoScreen(id = backStackEntry.arguments?.getLong("id"))
        }
    }
}

sealed class Screen(val route: String) {
    data object Todos : Screen("todos")
    data object EditTodo : Screen("todo/{id}") {
        fun createRoute(id: Long?) = "todo/$id"
    }
}