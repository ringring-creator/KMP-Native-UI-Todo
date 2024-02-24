package com.ring.ring.kmptodo.todos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.ring.ring.kmptodo.MainViewModel
import com.ring.ring.kmptodo.R
import com.ring.ring.kmptodo.todos.TodosScreenNav.ROUTE

data class TodosUiState(
    val todos: List<TodosItemUiState>,
)

data class TodosItemUiState(
    val id: Long,
    val title: String,
    val done: Boolean,
    val deadline: String,
)

object TodosScreenNav {
    const val ROUTE = "todos"
}

fun NavGraphBuilder.todosScreen(
    mainViewModel: MainViewModel,
    onNavigateToEditTodo: (Long?) -> Unit
) {
    composable(ROUTE) {
        TodosScreen(
            mainViewModel = mainViewModel,
            onNavigateToEditTodo = onNavigateToEditTodo
        )
    }
}

@Composable
fun TodosScreen(
    viewModel: TodosViewModel = hiltViewModel(),
    mainViewModel: MainViewModel,
    onNavigateToEditTodo: (Long?) -> Unit
) {
    val todosUiState by viewModel.todosUiState.collectAsStateWithLifecycle()
    val isDarkMode by mainViewModel.isDarkMode.collectAsStateWithLifecycle()

    TodosScreen(
        uiState = todosUiState,
        isDarkMode = isDarkMode,
        setDone = viewModel::setDone,
        setDarkMode = mainViewModel::setDarkMode,
        onNavigateToEditTodo = onNavigateToEditTodo
    )

    DisposableEffect(Unit) {
        viewModel.refresh()

        onDispose { }
    }
}

@Composable
fun TodosScreen(
    uiState: TodosUiState,
    isDarkMode: Boolean,
    setDone: (id: Long, done: Boolean) -> Unit,
    setDarkMode: (done: Boolean) -> Unit,
    onNavigateToEditTodo: (Long?) -> Unit,
) {
    Scaffold(
        topBar = {
            TodosTopBar(isDarkMode, setDarkMode)
        },
        floatingActionButton = {
            TodosFloatingButton(onNavigateToEditTodo)
        }
    ) {
        TodosContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            todos = uiState.todos,
            onNavigateToEditTodo = onNavigateToEditTodo,
            setDone = setDone,
        )
    }
}

@Composable
private fun TodosTopBar(
    isDarkMode: Boolean,
    setDarkMode: (done: Boolean) -> Unit
) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.todos_screen_title)) },
        actions = {
            IconToggleButton(checked = isDarkMode, onCheckedChange = setDarkMode) {
                Icon(
                    painterResource(id = R.drawable.baseline_dark_mode_24),
                    contentDescription = null,
                    tint = if (isDarkMode) Color.Black else Color.White
                )
            }
        }
    )
}

@Composable
private fun TodosFloatingButton(onNavigateToEditTodo: (Long?) -> Unit) {
    FloatingActionButton(onClick = { onNavigateToEditTodo(null) }) {
        Icon(
            Icons.Filled.Create,
            contentDescription = "create",
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun TodosContent(
    modifier: Modifier = Modifier,
    todos: List<TodosItemUiState>,
    onNavigateToEditTodo: (Long?) -> Unit,
    setDone: (id: Long, done: Boolean) -> Unit,
) {
    LazyColumn(modifier) {
        items(todos) { item ->
            TodosItem(item, onNavigateToEditTodo) {
                setDone(item.id, it)
            }
        }
    }
}

@Composable
private fun TodosItem(
    item: TodosItemUiState,
    onNavigateToEditTodo: (Long?) -> Unit,
    setDone: (done: Boolean) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onNavigateToEditTodo(item.id) }
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = item.done,
                    onCheckedChange = setDone,
                )
                Text(
                    item.title,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Deadline",
                    modifier = Modifier.size(24.dp)
                )
                Text(item.deadline)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun TodosScreenPreview(
    @PreviewParameter(TodosPreviewParameterProvider::class) uiState: TodosUiState
) {
    TodosScreen(
        uiState = uiState,
        isDarkMode = false,
        setDone = { _, _ -> },
        setDarkMode = {}
    ) {}
}