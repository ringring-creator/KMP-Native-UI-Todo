package com.ring.ring.kmptodo.edit

import androidx.compose.ui.tooling.preview.PreviewParameterProvider


class EditTodoPreviewParameterProvider : PreviewParameterProvider<EditTodoUiState> {
    override val values: Sequence<EditTodoUiState>
        get() = sequenceOf(
            EditTodoUiState(
                title = "implement UI",
                description = "implement UI".repeat(10),
                done = false,
                deadline = EditTodoUiState.Deadline(
                    initialYear = 2023,
                    initialMonth = 1,
                    initialDay = 1,
                ),
                showDatePicker = false,
            ),
        )
}