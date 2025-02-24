// ProductService.kt
package com.example.productlist.data.network

import com.example.productlist.data.cache.CacheManager
import com.example.productlist.data.models.LoginRequest
import com.example.productlist.data.models.LoginResponse
import com.example.productlist.data.models.Product
import com.example.productlist.data.models.ProductResponse
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProductService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse

    @GET("products")
    suspend fun getProducts(): ProductResponse

    @GET("products/{id}")
    suspend fun getProductDetails(@Path("id") id: Int): Product

    companion object {
        private const val BASE_URL = "https://dummyjson.com/"

        fun create(): ProductService {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ProductService::class.java)
        }
    }
}

fun ProductService.getCachedProductDetails(id: Int): Product {
    return CacheManager.get("product_$id") {
        runBlocking { getProductDetails(id) }
    }
}

fun ProductService.getCachedProducts(): ProductResponse {
    return CacheManager.get("products") {
        runBlocking { getProducts() }
    }
}