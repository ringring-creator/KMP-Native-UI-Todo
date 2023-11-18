package com.ring.ring.kmptodo

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ring.ring.kmptodo.todos.TodosScreen

@Composable
fun TodoApp(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Todos.route) {
        composable(Screen.Todos.route) { TodosScreen() }
    }
}

sealed class Screen(val route: String) {
    data object Todos : Screen("todos")
}