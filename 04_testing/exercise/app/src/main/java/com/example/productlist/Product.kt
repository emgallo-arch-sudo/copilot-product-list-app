// Product.kt
package com.example.productlist

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: String,
    val thumbnail: String,
    val images: List<String>,
    val rating: Double,
    val stock: Int,
    val reviews: List<Review>
)

data class Review(
    val id: Int,
    val body: String?,
    val rating: Int,
    val user: String?
)