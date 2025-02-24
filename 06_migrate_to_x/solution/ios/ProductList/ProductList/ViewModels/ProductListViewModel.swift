// ViewModels/ProductListViewModel.swift
import Foundation
import Combine

class ProductListViewModel: ObservableObject {
    @Published var products: [Product] = []
    @Published var errorMessage: String?

    private var cancellables = Set<AnyCancellable>()

    func fetchProducts() {
        ProductService.shared.fetchProducts { [weak self] result in
            switch result {
            case .success(let products):
                self?.products = products
            case .failure(let error):
                self?.errorMessage = error.localizedDescription
            }
        }
    }
}
