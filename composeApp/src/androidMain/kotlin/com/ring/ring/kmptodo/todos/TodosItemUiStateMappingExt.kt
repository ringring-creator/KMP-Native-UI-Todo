package com.ring.ring.kmptodo.todos

import data.Todo


fun Todo.toTodosItemUiState() = id?.let {
    TodosItemUiState(
        id = it,
        title = title,
        done = done,
        deadline = deadline.toString(),
    )
}