import Foundation
import ComposeApp

class TodosViewModel: ObservableObject {
    @Published var uiState = TodosUiState(todos: [])
    private let todoRepository:TodoRepository

    init(
        uiState: TodosUiState = TodosUiState(todos: []),
        todoRepository:TodoRepository = DataModules.Factory().createTodoRepository()
    ) {
        self.uiState = uiState
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
}
