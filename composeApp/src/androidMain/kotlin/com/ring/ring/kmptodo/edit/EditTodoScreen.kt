package com.ring.ring.kmptodo.edit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Checkbox
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.ring.ring.kmptodo.R

data class EditTodoUiState(
    val id: Long?,
    val title: String,
    val description: String,
    val done: Boolean,
    val deadline: DeadlineUiState,
    val editTitle: (String) -> Unit,
    val editDescription: (String) -> Unit,
    val editDone: (Boolean) -> Unit,
    val save: () -> Unit,
    val delete: () -> Unit,
)

data class DeadlineUiState(
    val initialYear: Int,
    val initialMonth: Int,
    val initialDay: Int,
    val setDate: (year: Int, month: Int, day: Int) -> Unit,
    val showDatePickerEvent: Boolean,
) {
    fun deadline() = "${initialYear}-${initialMonth}-${initialDay}"
}

@Composable
fun EditTodoScreen(state: EditTodoUiState) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.edit_todo_screen_title)) })
        }
    ) {
        EditTodoContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            state = state
        )
    }
}

@Composable
fun EditTodoContent(modifier: Modifier, state: EditTodoUiState) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = modifier.padding(8.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = state.done, onCheckedChange = state.editDone)
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.title,
                    onValueChange = state.editTitle,
                    label = { Text(stringResource(id = R.string.title)) }
                )
            }

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                value = state.description,
                onValueChange = state.editDescription,
                label = { Text(stringResource(id = R.string.description)) }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Icon(
                    Icons.Filled.DateRange,
                    contentDescription = "dateRange",
                    modifier = Modifier.size(24.dp)
                )
                Text(state.deadline.deadline())
            }
        }
        Row(
            Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            FloatingActionButton(
                onClick = state.save,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    Icons.Filled.Create,
                    contentDescription = "create",
                    modifier = Modifier.size(24.dp)
                )
            }
            FloatingActionButton(
                onClick = state.delete,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "delete",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        DeadlineDatePicker(state.deadline)
    }
}

@Composable
private fun DeadlineDatePicker(deadline: DeadlineUiState) {
    DatePicker(
        deadline.initialYear,
        deadline.initialMonth,
        deadline.initialDay,
        deadline.setDate,
        deadline.showDatePickerEvent
    )
}

@Preview(showSystemUi = true)
@Composable
fun EditTodoScreenPreview(
    @PreviewParameter(EditTodoPreviewParameterProvider::class) state: EditTodoUiState
) {
    EditTodoScreen(state = state)
}
