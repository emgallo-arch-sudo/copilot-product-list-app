// LoginRequest.kt
package com.example.productlist

data class LoginRequest(
    val username: String,
    val password: String,
    val expiresInMins: Int? = 60
)