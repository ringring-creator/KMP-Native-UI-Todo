package com.ring.ring.kmptodo.edit

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ring.ring.kmptodo.R
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class EditTodoUiState(
    val title: String,
    val description: String,
    val done: Boolean,
    val deadline: Deadline,
    val showDatePickerEvent: Flow<Boolean>,
) {
    data class Deadline(
        val year: Int,
        val month: Int,
        val day: Int,
    ) {
        companion object {
            fun createCurrentDate(): Deadline {
                val currentDate =
                    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                return Deadline(
                    year = currentDate.year,
                    month = currentDate.monthNumber,
                    day = currentDate.dayOfMonth,
                )
            }
        }

        override fun toString(): String = "${year}-${month}-${day}"
    }
}

interface EditTodoStateUpdater {
    fun setTitle(title: String) {}
    fun setDescription(description: String) {}
    fun setDone(done: Boolean) {}
    fun setDeadline(year: Int, month: Int, day: Int) {}
    fun showDatePicker() {}
    fun save() {}
    fun delete() {}
    fun onBack() {}
}

@Composable
fun rememberEditTodoUiState(
    viewModel: EditTodoViewModel,
    popBackStack: () -> Boolean,
): EditTodoUiState {
    LaunchedEffect(Unit) {
        viewModel.backEvent.collect {
            popBackStack()
        }
    }

    return EditTodoUiState(
        viewModel.title.collectAsState().value,
        viewModel.description.collectAsState().value,
        viewModel.done.collectAsState().value,
        viewModel.deadline.collectAsState().value,
        viewModel.showDatePickerEvent,
    )
}

@Composable
fun EditTodoScreen(
    popBackStack: () -> Boolean,
    viewModel: EditTodoViewModel = hiltViewModel(),
    uiState: EditTodoUiState = rememberEditTodoUiState(
        popBackStack = popBackStack,
        viewModel = viewModel
    ),
) {
    EditTodoScreen(uiState, viewModel)
}

@Composable
fun EditTodoScreen(
    editTodoUiState: EditTodoUiState,
    stateUpdater: EditTodoStateUpdater,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.edit_todo_screen_title)) },
                navigationIcon = {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.clickable { stateUpdater.onBack() }
                    )
                }
            )
        }
    ) {
        EditTodoContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            uiState = editTodoUiState,
            stateUpdater = stateUpdater,
        )
    }
}

@Composable
fun EditTodoContent(
    modifier: Modifier,
    uiState: EditTodoUiState,
    stateUpdater: EditTodoStateUpdater,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = modifier.padding(8.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = uiState.done, onCheckedChange = stateUpdater::setDone)
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.title,
                    onValueChange = stateUpdater::setTitle,
                    label = { Text(stringResource(id = R.string.title)) }
                )
            }

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                value = uiState.description,
                onValueChange = stateUpdater::setDescription,
                label = { Text(stringResource(id = R.string.description)) }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { stateUpdater.showDatePicker() }
            ) {
                Icon(
                    Icons.Filled.DateRange,
                    contentDescription = "dateRange",
                    modifier = Modifier.size(24.dp)
                )
                Text(uiState.deadline.toString())
            }
        }
        Row(
            Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            FloatingActionButton(
                onClick = stateUpdater::save,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    Icons.Filled.Create,
                    contentDescription = "create",
                    modifier = Modifier.size(24.dp)
                )
            }
            FloatingActionButton(
                onClick = stateUpdater::delete,
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
            uiState.deadline,
            uiState.showDatePickerEvent,
            stateUpdater::setDeadline,
        )
    }
}

@Composable
private fun DeadlineDatePicker(
    deadline: EditTodoUiState.Deadline,
    showDatePickerEvent: Flow<Boolean>,
    setDate: (Int, Int, Int) -> Unit
) {
    DatePicker(
        deadline.year,
        deadline.month,
        deadline.day,
        setDate,
        showDatePickerEvent,
    )
}

@Preview(showSystemUi = true)
@Composable
fun EditTodoScreenPreview(
    @PreviewParameter(EditTodoPreviewParameterProvider::class) editTodoUiState: EditTodoUiState
) {
    EditTodoScreen(
        editTodoUiState = editTodoUiState,
        stateUpdater = object : EditTodoStateUpdater {},
    )
}
