package com.ring.ring.kmptodo.todos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ring.ring.kmptodo.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import data.TodoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TodosViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _todosUiState = MutableStateFlow(TodosUiState(todos = emptyList()))
    val todosUiState = _todosUiState.asStateFlow()

    init {
        viewModelScope.launch {
            withContext(dispatcher) {
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