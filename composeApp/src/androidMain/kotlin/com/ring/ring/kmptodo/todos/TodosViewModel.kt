package com.ring.ring.kmptodo.todos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodosViewModel(
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {
    private val _todosUiState = MutableStateFlow(TodosUiState(todos = emptyList()))
    val todosUiState = _todosUiState.asStateFlow()

    init {
        viewModelScope.launch {
            withContext(defaultDispatcher) {
                _todosUiState.update {
                    TodosUiState(
                        todos = createTodosItemUiStates()
                    )
                }
            }
        }
    }

    private fun createTodosItemUiStates() = listOf(
        TodosItemUiState(0L, "implement UI", false, {}, "2023-04-01"),
        TodosItemUiState(0L, "implement ViewModel. ".repeat(5), true, {}, "2023-05-01"),
        TodosItemUiState(0L, "implement Repository", false, {}, "2023-12-01"),
    )
}