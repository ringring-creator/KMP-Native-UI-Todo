package com.ring.ring.kmptodo.edit

import androidx.compose.ui.tooling.preview.PreviewParameterProvider


class EditTodoPreviewParameterProvider : PreviewParameterProvider<EditTodoUiState> {
    override val values: Sequence<EditTodoUiState>
        get() = sequenceOf(
            EditTodoUiState(
                id = null,
                title = "implement UI",
                description = "implement UI".repeat(10),
                done = false,
                deadline = DeadlineUiState(
                    initialYear = 2023,
                    initialMonth = 1,
                    initialDay = 1,
                    setDate = { _, _, _ -> },
                    showDatePickerEvent = false,
                ),
                editTitle = {},
                editDescription = {},
                editDone = {},
                save = {},
                delete = {},
            ),
        )
}