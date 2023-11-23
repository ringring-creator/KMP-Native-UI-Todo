import ComposeApp

extension Todo {
    func toTodosItemUiState() -> TodosItemUiState {
        return TodosItemUiState(
            id: id?.int64Value,
            title: title,
            done: done,
            deadline: deadline.toString()
        )
    }
}

extension Kotlinx_datetimeLocalDate {
    func toString() -> String {
        return "\(year)-\(monthNumber)-\(dayOfMonth)"
    }
}
