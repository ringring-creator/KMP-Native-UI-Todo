package com.ring.ring.kmptodo

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.ring.ring.kmptodo.editTodo.EditTodoScreenNav
import com.ring.ring.kmptodo.editTodo.editTodoScreen
import com.ring.ring.kmptodo.todos.TodosScreenNav
import com.ring.ring.kmptodo.todos.todosScreen

@Composable
fun TodoNavGraph(navController: NavHostController, mainViewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = TodosScreenNav.ROUTE) {
        todosScreen(
            mainViewModel = mainViewModel,
            onNavigateToEditTodo = {
                navController.navigate(EditTodoScreenNav.createRoute(it))
            }
        )
        editTodoScreen(navController::popBackStack)
    }
}