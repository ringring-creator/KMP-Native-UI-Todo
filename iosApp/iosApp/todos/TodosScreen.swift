import Foundation
import ComposeApp
import SwiftUI

struct TodosUiState {
    var todos: [TodosItemUiState]
}

struct TodosItemUiState: Identifiable {
    let id: Int64?
    let title: String
    var done: Bool
    let deadline: String
}

struct TodosScreen: View {
    @ObservedObject private(set) var viewModel: TodosViewModel
    
    init(viewModel: TodosViewModel = TodosViewModel()) {
        self.viewModel = viewModel
    }
    
    var body: some View {
        NavigationView {
            ZStack(alignment: .bottomTrailing) {
                List(viewModel.uiState.todos) { todo in
                    NavigationLink(destination: EditTodoScreen(viewModel: EditTodoViewModel(), id: todo.id)) {
                        TodosItem(todo: todo,viewModel: viewModel)
                    }
                }.frame(maxWidth: .infinity,maxHeight: .infinity)
                NavigationLink(destination: EditTodoScreen(viewModel: EditTodoViewModel(), id: nil)) {
                    Image(systemName: "plus")
                        .resizable()
                        .padding(12)
                        .frame(width: 48, height: 48)
                        .background(Color.green)
                        .clipShape(Circle())
                        .foregroundColor(.white)
                        .padding(18)
                }
            }
            .onAppear { self.viewModel.refresh() }
        }
    }
}

private struct TodosItem: View {
    var todo: TodosItemUiState
    private(set) var viewModel: TodosViewModel
    
    var body: some View{
        NavigationLink(destination: EditTodoScreen(viewModel: EditTodoViewModel(), id: todo.id)) {
            VStack(alignment: .leading, spacing: 0) {
                HStack(spacing: 5) {
                    Image(systemName: todo.done ? "checkmark.square" : "square")
                        .onTapGesture {
                            viewModel.setDone(id: todo.id ?? 0, done: !todo.done)
                        }
                    Text(todo.title)
                }
                HStack(spacing: 5) {
                    Spacer()
                    Image(systemName: "calendar")
                    Text(todo.deadline)
                }
            }
        }
    }
}
