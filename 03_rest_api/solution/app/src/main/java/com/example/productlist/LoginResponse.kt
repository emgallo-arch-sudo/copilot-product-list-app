// LoginResponse.kt
package com.example.productlist

data class LoginResponse(
    val id: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val accessToken: String
)