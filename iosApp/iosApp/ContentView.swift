import UIKit
import SwiftUI
import ComposeApp

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        TodosScreen(viewModel: TodosViewModel(uiState:TodosUiState(todos: [
            TodosItemUiState(id: 1, title: "Todo 1", done: false, deadline: "2023-11-25"),
            TodosItemUiState(id: 2, title: "Todo 2", done: true, deadline: "2023-11-26")
        ]
    )))
//        ComposeView()
//                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
    }
}



