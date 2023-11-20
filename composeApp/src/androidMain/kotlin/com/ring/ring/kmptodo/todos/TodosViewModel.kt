package com.ring.ring.kmptodo.todos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ring.ring.kmptodo.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import data.Todo
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
) : ViewModel(), TodosStateUpdater {
    private val _todosUiState = MutableStateFlow(TodosUiState(todos = emptyList()))
    val todosUiState = _todosUiState.asStateFlow()

    init {
        viewModelScope.launch {
            withContext(dispatcher) {
                refresh()
            }
        }
    }

    override fun setDone(id: Long, done: Boolean) {
        viewModelScope.launch {
            withContext(dispatcher) {
                todoRepository.updateDone(id, done)
                refresh()
            }
        }
    }

    private suspend fun refresh() {
        _todosUiState.update {
            TodosUiState(
                todos = todoRepository.list().mapNotNull(Todo::toTodosItemUiState)
            )
        }
    }
}