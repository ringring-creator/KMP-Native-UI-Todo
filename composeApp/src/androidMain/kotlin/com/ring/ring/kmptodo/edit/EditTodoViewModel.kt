package com.ring.ring.kmptodo.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ring.ring.kmptodo.Screen.Companion.EDIT_ID_ARGS
import com.ring.ring.kmptodo.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import data.Todo
import data.TodoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditTodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel(), EditTodoStateUpdater {
    private var id: Long? = null

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()
    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()
    private val _done = MutableStateFlow(false)
    val done = _done.asStateFlow()
    private val _deadline = MutableStateFlow(EditTodoUiState.Deadline.createCurrentDate())
    val deadline = _deadline.asStateFlow()
    private val _showDatePickerEvent = Channel<Boolean>()
    val showDatePickerEvent = _showDatePickerEvent.receiveAsFlow()

    init {
        val savedId: String? = savedStateHandle[EDIT_ID_ARGS]
        id = savedId?.toLongOrNull()
        viewModelScope.launch {
            withContext(dispatcher) {
                id?.let {
                    val todo = todoRepository.get(it)
                    _title.update { todo.title }
                    _description.update { todo.description }
                    _done.update { todo.done }
                    _deadline.update { todo.deadline.toDeadline() }
                }
            }
        }
    }

    override fun setTitle(title: String) {
        _title.update { title }
    }

    override fun setDescription(description: String) {
        _description.update { description }
    }

    override fun setDone(done: Boolean) {
        _done.update { done }
    }

    override fun setDeadline(year: Int, month: Int, day: Int) {
        _deadline.update {
            EditTodoUiState.Deadline(
                year = year,
                month = month,
                day = day,
            )
        }
    }

    override fun showDatePicker() {
        _showDatePickerEvent.trySend(true)
    }

    override fun save() {
        viewModelScope.launch {
            withContext(dispatcher) {
                val id = todoRepository.save(
                    Todo(
                        id = id,
                        title = _title.value,
                        description = _description.value,
                        done = _done.value,
                        deadline = _deadline.value.toLocalDate(),
                    )
                )
                id?.let {
                    this@EditTodoViewModel.id = it
                }
            }
        }
    }

    override fun delete() {
        viewModelScope.launch {
            withContext(dispatcher) {
                id?.let { todoRepository.delete(id = it) }
            }
        }
    }
}