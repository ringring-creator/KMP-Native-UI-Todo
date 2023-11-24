import Foundation
import ComposeApp

class TodosViewModel: ObservableObject {
    @Published var uiState = TodosUiState(todos: [])
    private let todoRepository:TodoRepository

    init(
        todoRepository: TodoRepository = DataModules.Factory().createTodoRepository()
    ) {
        self.todoRepository = todoRepository
    }
    
    func refresh() {
        Task {
            do {
                let todos = try await todoRepository.list()
                uiState = TodosUiState(
                    todos: todos.map { todo in
                        todo.toTodosItemUiState()
                    }
                )
            } catch {
                print("fail to todoRepository.list")
            }
        }
    }
    
    func setDone(id: Int64, done: Bool) {
        Task {
            do {
                try await todoRepository.updateDone(id: id, done: done)
                refresh()
            }
        }
    }
}
