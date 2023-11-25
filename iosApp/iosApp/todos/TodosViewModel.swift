import Foundation
import ComposeApp

class TodosViewModel: ObservableObject {
    @Published var uiState = TodosUiState(todos: [])
    @Published private(set) var isDarkMode: Bool = false
    private let todoRepository:TodoRepository
    private let screenSettingsRepository: ScreenSettingsRepository

    init(
        todoRepository: TodoRepository = DataModules.Factory().createTodoRepository(),
        screenSettingsRepository: ScreenSettingsRepository = DataModules.Factory().createScreenSettingsRepository()
    ) {
        self.todoRepository = todoRepository
        self.screenSettingsRepository = screenSettingsRepository
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
    
    func toggleDarkMode() {
        screenSettingsRepository.set(
            screenSettings: ScreenSettings(isDarkMode: !isDarkMode)
        )
        updateIsDarkMode()
    }
    
    private func updateIsDarkMode() {
        isDarkMode = screenSettingsRepository.get().isDarkMode
    }
}
