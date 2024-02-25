import Foundation
import ComposeApp
import SwiftUI
import Combine

struct EditTodoScreen: View {
    @ObservedObject private(set) var viewModel: EditTodoViewModel
    @Environment(\.presentationMode) var presentationMode
    private let id:Int64?
    @State private var cancellables: Set<AnyCancellable> = Set<AnyCancellable>()
    
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
                    })
            }
            .padding(16)
            Spacer()
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .onAppear {
            viewModel.loadTodo(id: id)
            viewModel.dismissEvent.sink {
                presentationMode.wrappedValue.dismiss()
            }.store(in: &cancellables)
        }
        .onDisappear {
            cancellables.forEach{ $0.cancel() }
        }
    }
}
