// Views/ProductListScreen.swift
import SwiftUI

struct ProductListScreen: View {
    @ObservedObject private var viewModel = ProductListViewModel()
    @EnvironmentObject var authViewModel: AuthViewModel
    let username: String
    var onLogout: () -> Void
    
    var body: some View {
        NavigationView {
            VStack {
                Text("Welcome, \(username)!")
                    .font(.largeTitle)
                    .padding()

                if let errorMessage = viewModel.errorMessage {
                    Text(errorMessage)
                        .foregroundColor(.red)
                        .padding()
                } else {
                    List(viewModel.products) { product in
                        NavigationLink(destination: ProductDetailsScreen(product: product)) {
                            ProductCard(product: product) { _ in }
                        }
                    }
                }

                Button("Logout") {
                    onLogout()
                }
                .padding()
            }
            .onAppear {
                viewModel.fetchProducts()
            }
        }
        .navigationBarBackButtonHidden()
    }
}

struct ProductListScreen_Previews: PreviewProvider {
    static var previews: some View {
        ProductListScreen(username: "User", onLogout: {
            
        })
            .environmentObject(AuthViewModel())
    }
}
