package com.ring.ring.kmptodo.edit

import kotlinx.datetime.LocalDate

fun LocalDate.toDeadline(): EditTodoUiState.Deadline = EditTodoUiState.Deadline(
    year = year,
    month = monthNumber,
    day = dayOfMonth,
)

fun EditTodoUiState.Deadline.toLocalDate(): LocalDate = LocalDate(
    year = year,
    monthNumber = month,
    dayOfMonth = day,
)



