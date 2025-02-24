// Models/Product.swift
import Foundation

struct Product: Identifiable, Codable {
    let id: Int
    let title: String
    let description: String
    let price: Double
    let thumbnail: String
}
