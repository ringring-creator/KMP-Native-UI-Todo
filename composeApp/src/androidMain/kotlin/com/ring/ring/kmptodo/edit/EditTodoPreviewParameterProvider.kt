package com.ring.ring.kmptodo.edit

import androidx.compose.ui.tooling.preview.PreviewParameterProvider


class EditTodoPreviewParameterProvider : PreviewParameterProvider<EditTodoStateHolder.UiState> {
    override val values: Sequence<EditTodoStateHolder.UiState>
        get() = sequenceOf(
            EditTodoStateHolder.UiState(
                title = "implement UI",
                description = "implement UI".repeat(10),
                done = false,
                deadline = EditTodoViewModel.Deadline(
                    initialYear = 2023,
                    initialMonth = 1,
                    initialDay = 1,
                ),
                showDatePicker = false,
            ),
        )
}