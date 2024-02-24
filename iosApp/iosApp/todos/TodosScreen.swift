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
                        TodosItem(todo: todo,setDone: viewModel.setDone)
                    }
                }.frame(maxWidth: .infinity,maxHeight: .infinity)
                NavigationLink(destination: EditTodoScreen(viewModel: EditTodoViewModel(), id: nil)) {
                    TodosFloatingButton()
                }
            }
            .navigationBarTitle("Todos", displayMode: .inline)
            .navigationBarItems(trailing:
                                    Button(action: {
                viewModel.toggleDarkMode()
            }) {
                Image(systemName: "moon")
                    .imageScale(.large)
                    .foregroundColor(self.viewModel.isDarkMode ? .white : .black)
            })
            .onAppear { self.viewModel.refresh() }
        }
        .preferredColorScheme(self.viewModel.isDarkMode ? .dark : .light)
    }
}

private struct TodosFloatingButton: View {
    var body: some View {
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

private struct TodosItem: View {
    private let todo: TodosItemUiState
    private let setDone: (Int64, Bool) -> Void
    
    init(
        todo: TodosItemUiState,
        setDone: @escaping (Int64, Bool) -> Void
    ) {
        self.todo = todo
        self.setDone = setDone
    }
    
    var body: some View{
        VStack(alignment: .leading, spacing: 0) {
            HStack(spacing: 5) {
                Image(systemName: todo.done ? "checkmark.square" : "square")
                    .onTapGesture {
                        setDone(todo.id ?? 0, !todo.done)
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
