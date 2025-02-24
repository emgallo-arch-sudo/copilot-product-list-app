import SwiftUI

struct LoginScreen: View {
    @ObservedObject private var viewModel = AuthViewModel()
    @State private var username = ""
    @State private var password = ""

    var body: some View {
        NavigationView {
            VStack {
                TextField("Username", text: $username)
                    .autocapitalization(.none)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .padding()

                SecureField("Password", text: $password)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .padding()

                if let errorMessage = viewModel.errorMessage {
                    Text(errorMessage)
                        .foregroundColor(.red)
                        .padding()
                }

                Button(action: {
                    viewModel.login(username: username, password: password)
                }) {
                    if viewModel.isLoading {
                        ProgressView()
                            .progressViewStyle(CircularProgressViewStyle())
                    } else {
                        Text("Submit")
                    }
                }
                .padding()

                NavigationLink(
                    destination: ProductListScreen(username: username, onLogout: {
                        viewModel.logout()
                        username = ""
                        password = ""
                    })
                    .environmentObject(viewModel),
                    isActive: $viewModel.isLoggedIn
                ) {
                    EmptyView()
                }
            }
            .padding()
        }
    }
}

struct LoginScreen_Previews: PreviewProvider {
    static var previews: some View {
        LoginScreen()
    }
}
