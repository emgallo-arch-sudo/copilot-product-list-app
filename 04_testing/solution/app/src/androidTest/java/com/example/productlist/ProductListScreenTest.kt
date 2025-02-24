package com.example.productlist

import androidx.compose.material3.Text
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class ProductListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockProductService = mockk<ProductService>(relaxed = true)

    private val mockedProducts = listOf(
        Product(
            id = 1,
            title = "Product 1",
            description = "Description 1",
            price = "10.0",
            thumbnail = "https://example.com/product1.jpg",
            images = listOf("https://example.com/product1_1.jpg", "https://example.com/product1_2.jpg"),
            rating = 4.5,
            stock = 100,
            reviews = listOf(
                Review(id = 1, body = "Great product!", rating = 5, user = "User1"),
                Review(id = 2, body = "Good value for money.", rating = 4, user = "User2")
            )
        ),
        Product(
            id = 2,
            title = "Product 2",
            description = "Description 2",
            price = "20.0",
            thumbnail = "https://example.com/product2.jpg",
            images = listOf("https://example.com/product2_1.jpg", "https://example.com/product2_2.jpg"),
            rating = 4.0,
            stock = 50,
            reviews = listOf(
                Review(id = 3, body = "Satisfactory.", rating = 3, user = "User3"),
                Review(id = 4, body = "Could be better.", rating = 2, user = "User4")
            )
        ),
        Product(
            id = 3,
            title = "Product 3",
            description = "Description 3",
            price = "30.0",
            thumbnail = "https://example.com/product3.jpg",
            images = listOf("https://example.com/product3_1.jpg", "https://example.com/product3_2.jpg"),
            rating = 5.0,
            stock = 200,
            reviews = listOf(
                Review(id = 5, body = "Excellent!", rating = 5, user = "User5"),
                Review(id = 6, body = "Highly recommend.", rating = 5, user = "User6")
            )
        )
    )

    @Test
    fun testProductListDisplay(): Unit = runBlocking {
        // Mock the product list response
        coEvery { mockProductService.getProducts() } returns ProductResponse(
            products = mockedProducts
        )

        // Set the content
        composeTestRule.setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "productlist") {
                composable("productlist") { ProductListScreen(navController = navController, productService = mockProductService, username = "User", onLogout = {}) }
                composable("productdetails/{productId}") { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId")?.toInt() ?: 0
                    Text(text = "Product Details for ID: $productId")
                }
            }
        }

        // Verify the products are displayed
        composeTestRule.onNodeWithText("Product 1").assertExists()
        composeTestRule.onNodeWithText("Product 2").assertExists()
    }

    @Test
    fun testProductClickNavigation(): Unit = runBlocking {
        // Mock the product list response
        coEvery { mockProductService.getProducts() } returns ProductResponse(
                products = mockedProducts
                )

        // Set the content
        composeTestRule.setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "productlist") {
                composable("productlist") { ProductListScreen(navController = navController, productService = mockProductService, username = "User", onLogout = {} ) }
                composable("productdetails/{productId}") { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId")?.toInt() ?: 0
                    Text(text = "Product Details for ID: $productId")
                }
            }
        }

        // Click on the first product
        composeTestRule.onNodeWithText("Product 1").performClick()

        // Verify the navigation to the product details screen
        composeTestRule.onNodeWithText("Product Details for ID: 1").assertExists()
    }

    @Test
    fun testLogoutNavigation(): Unit = runBlocking {
        // Mock the product list response
        coEvery { mockProductService.getProducts() } returns ProductResponse(
            products = mockedProducts
        )

        // Set the content
        composeTestRule.setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "productlist") {
                composable("productlist") { ProductListScreen(navController = navController, productService = mockProductService, username = "User", onLogout = { navController.navigate("login") }) }
                composable("login") { Text(text = "Login Screen") }
            }
        }

        // Click the logout button
        composeTestRule.onNodeWithText("Logout").performClick()

        // Verify the navigation to the login screen
        composeTestRule.onNodeWithText("Login Screen").assertExists()
    }
}