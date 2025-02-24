// LoginScreenTest.kt
package com.example.productlist

import androidx.compose.material3.Text
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.productlist.data.models.LoginResponse
import com.example.productlist.data.network.ProductService
import com.example.productlist.ui.screens.LoginScreen
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockProductService = mockk<ProductService>(relaxed = true)

    @Test
    fun testLoginSuccess(): Unit = runBlocking {
        // Mock the login response
        coEvery { mockProductService.login(any()) } returns LoginResponse(1, "testuser", "John", "Doe", "token")

        // Set the content
        composeTestRule.setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "login") {
                composable("login") { LoginScreen(navController = navController, productService = mockProductService) }
                composable("productlist/{username}") { backStackEntry ->
                    val userName = backStackEntry.arguments?.getString("username")
                    Text(text = userName ?: "Unknown")
                }
            }
        }

        // Enter username and password
        composeTestRule.onNodeWithText("Username").performTextInput("testuser")
        composeTestRule.onNodeWithText("Password").performTextInput("password")

        // Verify the Submit button does not show the loading indicator initially
        composeTestRule.onNodeWithText("Submit").assertExists()
        composeTestRule.onNodeWithText("Submit").assertIsEnabled()

        // Click the submit button
        composeTestRule.onNodeWithText("Submit").performClick()

        // Verify the login success
        composeTestRule.onNodeWithText("John Doe").assertExists()
    }

    @Test
    fun testLoginFailure(): Unit = runBlocking {
        // Mock the login response to throw an exception
        coEvery { mockProductService.login(any()) } throws Exception("Invalid credentials")

        // Set the content
        composeTestRule.setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "login") {
                composable("login") { LoginScreen(navController = navController, productService = mockProductService) }
            }
        }

        // Enter username and password
        composeTestRule.onNodeWithText("Username").performTextInput("testuser")
        composeTestRule.onNodeWithText("Password").performTextInput("wrongpassword")

        // Click the submit button
        composeTestRule.onNodeWithText("Submit").performClick()

        // Verify the error message is displayed
        composeTestRule.onNodeWithText("Invalid username or password. Please try again.").assertExists()
    }
}