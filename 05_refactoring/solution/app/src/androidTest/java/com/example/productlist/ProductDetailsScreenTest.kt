package com.example.productlist

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.productlist.data.models.Product
import com.example.productlist.data.models.Review
import com.example.productlist.data.network.ProductService
import com.example.productlist.ui.screens.ProductDetailsScreen
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class ProductDetailsScreenTest {

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
        )
    )

    @Test
    fun testProductDetailsDisplay(): Unit = runBlocking {
        // Mock the product details response
        coEvery { mockProductService.getProductDetails(1) } returns mockedProducts[0]

        // Set the content
        composeTestRule.setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "productdetails/{productId}") {
                composable("productdetails/{productId}") { backStackEntry ->
                    val productId = backStackEntry.arguments?.getString("productId")?.toInt() ?: 1
                    ProductDetailsScreen(productId = productId, productService = mockProductService, onBack = { navController.popBackStack() })
                }
            }
        }

        // Verify the product details are displayed
        composeTestRule.onNodeWithText("Product 1").assertExists()
        composeTestRule.onNodeWithText("Rating: 4.5").assertExists()
        composeTestRule.onNodeWithText("Stock: 100").assertExists()
    }
}