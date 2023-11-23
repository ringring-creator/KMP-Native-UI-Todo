package com.ring.ring.kmptodo.editTodo

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.flow.Flow

@Composable
fun DatePicker(
    initialYear: Int,
    initialMonth: Int,
    initialDay: Int,
    setDate: (year: Int, month: Int, day: Int) -> Unit,
    showDatePickerEvent: Flow<Boolean>,
) {
    val context = LocalContext.current

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            setDate(year, month + 1, day)

        },
        initialYear, initialMonth, initialDay
    )

    LaunchedEffect(Unit) {
        showDatePickerEvent.collect {
            if (it) {
                datePickerDialog.show()
            } else {
                datePickerDialog.dismiss()
            }
        }
    }
}
