package com.ring.ring.kmptodo.todos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ring.ring.kmptodo.R

data class TodosUiState(
    val todos: List<TodosItemUiState>
)

data class TodosItemUiState(
    val id: Long,
    val title: String,
    val done: Boolean,
    val deadline: String,
)

interface TodosStateUpdater {
    fun setDone(id: Long, done: Boolean) {}
}

@Composable
fun TodosScreen(
    viewModel: TodosViewModel = hiltViewModel(),
    onNavigateToEditTodo: (Long?) -> Unit
) {
    val todosUiState by viewModel.todosUiState.collectAsState()
    TodosScreen(
        uiState = todosUiState,
        stateUpdater = viewModel,
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
    stateUpdater: TodosStateUpdater,
    onNavigateToEditTodo: (Long?) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.todos_screen_title)) })
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            TodosContent(
                todos = uiState.todos,
                onNavigateToEditTodo = onNavigateToEditTodo,
                setDone = stateUpdater::setDone,
            )
            FloatingActionButton(
                onClick = { onNavigateToEditTodo(null) },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    Icons.Filled.Create,
                    contentDescription = "create",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
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
fun TodosItem(
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
        stateUpdater = object : TodosStateUpdater {}
    ) {}
}