package com.ring.ring.kmptodo.edit

import androidx.lifecycle.ViewModel
import com.ring.ring.kmptodo.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import data.TodoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class EditTodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : ViewModel(), EditTodoChangeState {
    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()
    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()
    private val _done = MutableStateFlow(false)
    val done = _done.asStateFlow()
    private val _deadline = MutableStateFlow(EditTodoUiState.Deadline())
    val deadline = _deadline.asStateFlow()
    private val _showDatePicker = MutableStateFlow(false)
    val showDatePicker = _showDatePicker.asStateFlow()

    override fun setTitle(title: String) {}

    override fun setDescription(description: String) {}

    override fun setDone(done: Boolean) {}

    override fun setDate(year: Int, month: Int, day: Int) {}

    override fun save() {}

    override fun delete() {}
}