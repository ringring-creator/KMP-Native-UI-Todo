package com.ring.ring.kmptodo.todos

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class TodosPreviewParameterProvider : PreviewParameterProvider<TodosUiState> {
    private val default = TodosUiState(
        todos = listOf(
            TodosItemUiState(0L, "implement UI", false, "2023-04-01"),
            TodosItemUiState(1L, "implement ViewModel. ".repeat(5), true, "2023-05-01"),
            TodosItemUiState(2L, "implement Repository", false, "2023-12-01"),
        ),
    )

    override val values: Sequence<TodosUiState>
        get() = sequenceOf(
            default,
        )
}