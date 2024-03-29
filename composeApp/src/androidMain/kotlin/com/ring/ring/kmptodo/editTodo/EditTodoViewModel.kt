package com.ring.ring.kmptodo.editTodo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ring.ring.kmptodo.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import data.Todo
import data.TodoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
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
    private val _backEvent = Channel<Unit>()
    val backEvent = _backEvent.receiveAsFlow()

    init {
        val savedId: String? = savedStateHandle[EditTodoScreenNav.ID]
        id = savedId?.toLongOrNull()
        viewModelScope.launch {
            withContext(dispatcher) {
                id?.let {
                    val todo = todoRepository.get(it)
                    _title.value = todo.title
                    _description.value = todo.description
                    _done.value = todo.done
                    _deadline.value = todo.deadline.toDeadline()
                }
            }
        }
    }

    override fun setTitle(title: String) {
        _title.value = title
    }

    override fun setDescription(description: String) {
        _description.value = description
    }

    override fun setDone(done: Boolean) {
        _done.value = done
    }

    override fun setDeadline(year: Int, month: Int, day: Int) {
        _deadline.value = EditTodoUiState.Deadline(
            year = year,
            month = month,
            day = day,
        )
    }

    override fun showDatePicker() {
        _showDatePickerEvent.trySend(true)
    }

    override fun save() {
        viewModelScope.launch {
            withContext(dispatcher) {
                todoRepository.save(
                    Todo(
                        id = id,
                        title = _title.value,
                        description = _description.value,
                        done = _done.value,
                        deadline = _deadline.value.toLocalDate(),
                    )
                )
            }
        }
        onBack()
    }

    override fun delete() {
        viewModelScope.launch {
            withContext(dispatcher) {
                id?.let { todoRepository.delete(id = it) }
            }
        }
        onBack()
    }

    override fun onBack() {
        _backEvent.trySend(Unit)
    }
}