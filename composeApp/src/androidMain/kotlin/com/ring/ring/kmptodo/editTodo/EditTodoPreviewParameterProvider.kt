package com.ring.ring.kmptodo.editTodo

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.coroutines.flow.flowOf


class EditTodoPreviewParameterProvider : PreviewParameterProvider<EditTodoUiState> {
    override val values: Sequence<EditTodoUiState>
        get() = sequenceOf(
            EditTodoUiState(
                title = "implement UI",
                description = "implement UI".repeat(10),
                done = false,
                deadline = EditTodoUiState.Deadline(
                    year = 2023,
                    month = 1,
                    day = 1,
                ),
                showDatePickerEvent = flowOf(false),
            ),
        )
}