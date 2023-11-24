import Foundation
import ComposeApp
import SwiftUI

extension Kotlinx_datetimeLocalDate {
    func toDate() -> Date {
        var dateComponents = DateComponents()
        dateComponents.year = Int(self.year)
        dateComponents.month = Int(self.monthNumber)
        dateComponents.day = Int(self.dayOfMonth)
        
        let calendar = Calendar.current
        return calendar.date(from: dateComponents) ?? Date()
    }
}

extension Int64 {
    func toKotlinLong() -> KotlinLong {
        return KotlinLong(value: self)
    }
}

extension Date {
    func toLocalDate() -> Kotlinx_datetimeLocalDate {
        let calendar = Calendar.current
        let components = calendar.dateComponents([.year, .month, .day], from: self)
        
        let year = Int32(components.year ?? 0)
        let month = Int32(components.month ?? 0)
        let day = Int32(components.day ?? 0)
        
        return Kotlinx_datetimeLocalDate(year: year, monthNumber: month, dayOfMonth: day)
    }
}

@MainActor
class EditTodoViewModel: ObservableObject {
    var id: Int64? = nil
    @Published var title: String = ""
    @Published var description: String = ""
    @Published var done: Bool = false
    @Published var deadline: Date = Date()

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
        }
    }
}


struct EditTodoScreen: View {
    @ObservedObject private(set) var viewModel: EditTodoViewModel
    @Environment(\.presentationMode) var presentationMode
    private let id:Int64?
    
    init(viewModel: EditTodoViewModel, id: Int64?) {
        self.viewModel = viewModel
        self.id = id
    }
    
    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack(alignment: .center, spacing: 8) {
                Image(systemName: viewModel.done ? "checkmark.square" : "square")
                    .onTapGesture {
                        viewModel.done = !viewModel.done
                    }
                TextField("Title", text: $viewModel.title)
                    .onChange(of: viewModel.title) { newValue in
                        viewModel.title = newValue
                    }
            }
            .padding(16)
            
            TextField("Description", text: $viewModel.description)
                .onChange(of: viewModel.description) { newValue in
                    viewModel.description = newValue
                }
                .padding(16)
            
            DatePicker(
                "Select Date",
                selection: $viewModel.deadline,
                displayedComponents: [.date]
            )
            .padding(16)
            
            HStack(alignment: .center, spacing: 8) {
                Spacer()
                Image(systemName: "pencil")
                    .resizable()
                    .padding(12)
                    .frame(width: 48, height: 48)
                    .background(Color.green)
                    .clipShape(Circle())
                    .foregroundColor(.white)
                    .onTapGesture(perform: {
                        viewModel.save()
                        presentationMode.wrappedValue.dismiss()
                    })
                Image(systemName: "trash")
                    .resizable()
                    .padding(12)
                    .frame(width: 48, height: 48)
                    .background(Color.green)
                    .clipShape(Circle())
                    .foregroundColor(.white)
                    .onTapGesture(perform: {
                        viewModel.delete()
                        presentationMode.wrappedValue.dismiss()
                    })
            }
            .padding(16)
            Spacer()
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .onAppear {
            viewModel.loadTodo(id: id)
        }
    }
}
