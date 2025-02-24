// AuthService.swift
import Foundation
import Alamofire

class AuthService {
    static let shared = AuthService()
    private let baseURL = "https://dummyjson.com/auth/login"

    func login(username: String, password: String, completion: @escaping (Result<User, Error>) -> Void) {
        let parameters: [String: Any] = [
            "username": username,
            "password": password
        ]

        AF.request(baseURL, method: .post, parameters: parameters, encoding: JSONEncoding.default).responseJSON { response in
            switch response.result {
            case .success(let value):
                if let httpResponse = response.response, httpResponse.statusCode == 200 {
                    do {
                        let data = try JSONSerialization.data(withJSONObject: value, options: [])
                        let user = try JSONDecoder().decode(User.self, from: data)
                        completion(.success(user))
                    } catch {
                        completion(.failure(error))
                    }
                } else {
                    if let json = value as? [String: Any], let message = json["message"] as? String {
                        let error = NSError(domain: "", code: response.response?.statusCode ?? 0, userInfo: [NSLocalizedDescriptionKey: message])
                        completion(.failure(error))
                    } else {
                        let error = NSError(domain: "", code: response.response?.statusCode ?? 0, userInfo: [NSLocalizedDescriptionKey: "Unknown error"])
                        completion(.failure(error))
                    }
                }
            case .failure(let error):
                completion(.failure(error))
            }
        }
    }
}
