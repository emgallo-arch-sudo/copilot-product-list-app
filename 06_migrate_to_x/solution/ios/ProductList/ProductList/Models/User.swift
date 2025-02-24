// Models/User.swift
import Foundation

struct User: Codable {
    let id: Int
    let username: String
    let email: String
    let firstName: String
    let lastName: String
    let gender: String
    let image: String
    let accessToken: String
    let refreshToken: String
}
