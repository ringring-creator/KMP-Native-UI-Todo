package com.ring.ring.kmptodo.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class EditTodoViewModel(
    private val id: Long?,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel(), EditTodoChangeState {
    data class Deadline(
        val initialYear: Int = 0,
        val initialMonth: Int = 0,
        val initialDay: Int = 0,
    ) {
        override fun toString(): String = "${initialYear}-${initialMonth}-${initialDay}"
    }

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()
    private val _description = MutableStateFlow("")
    val description = _description.asStateFlow()
    private val _done = MutableStateFlow(false)
    val done = _done.asStateFlow()
    private val _deadline = MutableStateFlow(Deadline())
    val deadline = _deadline.asStateFlow()
    private val _showDatePicker = MutableStateFlow(false)
    val showDatePicker = _showDatePicker.asStateFlow()

    override fun setTitle(title: String) {}

    override fun setDescription(description: String) {}

    override fun setDone(done: Boolean) {}

    override fun setDate(year: Int, month: Int, day: Int) {}

    override fun save() {}

    override fun delete() {}

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val id = extras[ID_Key]
                return EditTodoViewModel(id = id) as T
            }
        }
        val ID_Key = object : CreationExtras.Key<Long?> {}
    }

}