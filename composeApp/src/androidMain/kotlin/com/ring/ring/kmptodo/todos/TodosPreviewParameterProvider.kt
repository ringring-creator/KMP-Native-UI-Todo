package com.ring.ring.kmptodo.todos

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class TodosPreviewParameterProvider : PreviewParameterProvider<TodosUiState> {
    override val values: Sequence<TodosUiState>
        get() = sequenceOf(
            TodosUiState(
                todos = listOf(
                    TodosItemUiState(0L, "implement UI", false, {},"2023-04-01"),
                    TodosItemUiState(0L, "implement ViewModel. ".repeat(5), true, {},"2023-05-01"),
                    TodosItemUiState(0L, "implement Repository", false, {},"2023-12-01"),
                )
            ),
        )
}