// FakeProducts.kt
package com.example.productlist

fun getFakeProducts(): List<Product> {
    return List(20) { index ->
        Product(
            title = "Product $index",
            description = "Description for product $index",
            price = "$${(10..100).random()}"
        )
    }
}