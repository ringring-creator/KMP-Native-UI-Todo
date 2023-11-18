package com.ring.ring.kmptodo.edit

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun DatePicker(
    initialYear: Int,
    initialMonth: Int,
    initialDay: Int,
    setDate: (year: Int, month: Int, day: Int) -> Unit,
    showDatePickerEvent: Boolean,
) {
    val context = LocalContext.current

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, day: Int ->
            setDate(year, month + 1, day)
        },
        initialYear, initialMonth, initialDay
    )

    DisposableEffect(showDatePickerEvent) {
        if (showDatePickerEvent) {
            datePickerDialog.show()
        } else {
            datePickerDialog.dismiss()
        }
        onDispose {}
    }
}
