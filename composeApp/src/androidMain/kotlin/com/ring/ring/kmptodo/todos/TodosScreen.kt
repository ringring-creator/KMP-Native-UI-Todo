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
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
    val setDone: (Boolean) -> Unit,
    val deadline: String,
)

@Composable
fun TodosScreen(
    viewModel: TodosViewModel = hiltViewModel(),
    onNavigateToEditTodo: (Long?) -> Unit
) {
    val todosUiState by viewModel.todosUiState.collectAsState()
    TodosScreen(
        state = todosUiState,
        onNavigateToEditTodo = onNavigateToEditTodo
    )
}

@Composable
fun TodosScreen(
    state: TodosUiState,
    onNavigateToEditTodo: (Long?) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.todos_screen_title)) })
        }
    ) {
        TodosContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            state = state,
            onNavigateToEditTodo = onNavigateToEditTodo,
        )
    }
}

@Composable
private fun TodosContent(
    modifier: Modifier,
    state: TodosUiState,
    onNavigateToEditTodo: (Long?) -> Unit
) {
    LazyColumn(modifier) {
        items(state.todos) {
            TodosItem(it, onNavigateToEditTodo)
        }
    }
}

@Composable
fun TodosItem(
    item: TodosItemUiState,
    onNavigateToEditTodo: (Long?) -> Unit
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
                    onCheckedChange = item.setDone,
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
    @PreviewParameter(TodosPreviewParameterProvider::class) state: TodosUiState
) {
    TodosScreen(state = state, onNavigateToEditTodo = {})
}