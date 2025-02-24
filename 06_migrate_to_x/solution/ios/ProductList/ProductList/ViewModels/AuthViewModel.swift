// AuthViewModel.swift
import SwiftUI

class AuthViewModel: ObservableObject {
    @Published var user: User?
    @Published var errorMessage: String?
    @Published var isLoggedIn: Bool = false
    @Published var isLoading: Bool = false

    func login(username: String, password: String) {
        isLoading = true
        AuthService.shared.login(username: username, password: password) { result in
            DispatchQueue.main.async {
                self.isLoading = false
                switch result {
                case .success(let user):
                    self.user = user
                    self.isLoggedIn = true
                case .failure(let error):
                    self.errorMessage = error.localizedDescription
                }
            }
        }
    }

    func logout() {
        user = nil
        isLoggedIn = false
    }
}
