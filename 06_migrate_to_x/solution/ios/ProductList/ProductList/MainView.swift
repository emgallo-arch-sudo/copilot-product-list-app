// Views/MainView.swift
import SwiftUI

struct MainView: View {
    @ObservedObject private var authViewModel = AuthViewModel()

    var body: some View {
        NavigationView {
            if let user = authViewModel.user {
                ProductListScreen(username: user.username, onLogout: {
                    authViewModel.logout()
                })
                    .environmentObject(authViewModel)
            } else {
                LoginScreen()
                    .environmentObject(authViewModel)
            }
        }
    }
}

struct MainView_Previews: PreviewProvider {
    static var previews: some View {
        MainView()
    }
}
