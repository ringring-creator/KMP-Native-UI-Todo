import Foundation
import ComposeApp
import Combine

@MainActor
class EditTodoViewModel: ObservableObject {
    var id: Int64? = nil
    @Published var title: String = ""
    @Published var description: String = ""
    @Published var done: Bool = false
    @Published var deadline: Date = Date()
    
    let dismissEvent = PassthroughSubject<Void, Never>()

    private let todoRepository: TodoRepository

    init(
        todoRepository: TodoRepository = DataModules.Factory().createTodoRepository()
    ) {
        self.todoRepository = todoRepository
    }
    
    func loadTodo(id: Int64?) {
        Task {
            do {
                if let id {
                    let todo = try await todoRepository.get(id: id)
                    self.id = id
                    title = todo.title
                    description = todo.description_
                    done = todo.done
                    deadline = todo.deadline.toDate()
                }
            } catch {
                print("fail to todoRepository.list")
            }
        }
    }
    
    func save() {
        Task {
            do {
                try await todoRepository.save(
                    todo: Todo(
                        id: id?.toKotlinLong(),
                        title: title,
                        description: description,
                        done: done,
                        deadline: deadline.toLocalDate()
                    )
                )
                dismissEvent.send()
            } catch {
                print("fail to todoRepository.save")
            }
        }
    }
    
    func delete() {
        Task {
            if let id {
                do {
                   try await todoRepository.delete(id: id)
                } catch {
                    print("fail to todoRepository.delete")
                }
            }
            dismissEvent.send()
        }
    }
}
