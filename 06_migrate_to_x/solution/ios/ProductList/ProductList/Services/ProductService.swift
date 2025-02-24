import Alamofire

class ProductService {
    static let shared = ProductService()
    private let baseURL = "https://dummyjson.com/products"

    func fetchProducts(completion: @escaping (Result<[Product], Error>) -> Void) {
        AF.request(baseURL).responseJSON { response in
            switch response.result {
            case .success(let value):
                do {
                    let data = try JSONSerialization.data(withJSONObject: value, options: [])
                    let partialResponse = try JSONDecoder().decode(ProductsResponse.self, from: data)
                    completion(.success(partialResponse.products))
                } catch {
                    completion(.failure(error))
                }
            case .failure(let error):
                completion(.failure(error))
            }
        }
    }
}
