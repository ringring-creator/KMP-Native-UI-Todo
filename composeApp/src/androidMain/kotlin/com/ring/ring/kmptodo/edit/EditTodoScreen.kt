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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ring.ring.kmptodo.R

data class EditTodoUiState(
    val title: String,
    val description: String,
    val done: Boolean,
    val deadline: Deadline,
    val showDatePicker: Boolean,
) {
    data class Deadline(
        val initialYear: Int = 0,
        val initialMonth: Int = 0,
        val initialDay: Int = 0,
    ) {
        override fun toString(): String = "${initialYear}-${initialMonth}-${initialDay}"
    }
}

interface EditTodoChangeState {
    fun setTitle(title: String) {}
    fun setDescription(description: String) {}
    fun setDone(done: Boolean) {}
    fun setDate(year: Int, month: Int, day: Int) {}
    fun save() {}
    fun delete() {}
}

data class EditTodoStateHolder(
    val viewModel: EditTodoViewModel,
) : EditTodoChangeState by viewModel {
    val editTodoUiState: EditTodoUiState
        @Composable get() = EditTodoUiState(
            viewModel.title.collectAsState().value,
            viewModel.description.collectAsState().value,
            viewModel.done.collectAsState().value,
            viewModel.deadline.collectAsState().value,
            viewModel.showDatePicker.collectAsState().value,
        )
}

@Composable
fun rememberEditTodoUiState(
    id: Long? = null,
    viewModel: EditTodoViewModel = hiltViewModel(),
) = remember {
    EditTodoStateHolder(viewModel)
}

@Composable
fun EditTodoScreen(
    id: Long? = null,
    stateHolder: EditTodoStateHolder = rememberEditTodoUiState(
        id = id,
    )
) {
    EditTodoScreen(stateHolder.editTodoUiState, stateHolder)
}

@Composable
fun EditTodoScreen(
    editTodoUiState: EditTodoUiState,
    changeState: EditTodoChangeState,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.edit_todo_screen_title)) })
        }
    ) {
        EditTodoContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            state = editTodoUiState,
            changeState = changeState,
        )
    }
}

@Composable
fun EditTodoContent(
    modifier: Modifier,
    state: EditTodoUiState,
    changeState: EditTodoChangeState,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = modifier.padding(8.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = state.done, onCheckedChange = changeState::setDone)
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.title,
                    onValueChange = changeState::setTitle,
                    label = { Text(stringResource(id = R.string.title)) }
                )
            }

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                value = state.description,
                onValueChange = changeState::setDescription,
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
                Text(state.deadline.toString())
            }
        }
        Row(
            Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            FloatingActionButton(
                onClick = changeState::save,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    Icons.Filled.Create,
                    contentDescription = "create",
                    modifier = Modifier.size(24.dp)
                )
            }
            FloatingActionButton(
                onClick = changeState::delete,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "delete",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        DeadlineDatePicker(
            state.deadline,
            state.showDatePicker,
            changeState::setDate,
        )
    }
}

@Composable
private fun DeadlineDatePicker(
    deadline: EditTodoUiState.Deadline,
    showDatePicker: Boolean,
    setDate: (Int, Int, Int) -> Unit
) {
    DatePicker(
        deadline.initialYear,
        deadline.initialMonth,
        deadline.initialDay,
        setDate,
        showDatePicker,
    )
}

@Preview(showSystemUi = true)
@Composable
fun EditTodoScreenPreview(
    @PreviewParameter(EditTodoPreviewParameterProvider::class) editTodoUiState: EditTodoUiState
) {
    EditTodoScreen(
        editTodoUiState = editTodoUiState,
        changeState = object : EditTodoChangeState {},
    )
}
