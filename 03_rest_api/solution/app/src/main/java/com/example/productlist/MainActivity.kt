package com.example.productlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.productlist.ui.theme.ProductListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProductListTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(onLoginSuccess = { username ->
                            navController.navigate("productList/$username")
                        })
                    }
                    composable("productList/{username}") { backStackEntry ->
                        val username = backStackEntry.arguments?.getString("username") ?: ""
                        ProductListScreen(
                            username = username,
                            onLogout = {
                                navController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                            onProductClick = { productId ->
                                navController.navigate("productDetails/$productId")
                            }
                        )
                    }
                    composable("productDetails/{productId}") { backStackEntry ->
                        val productId = backStackEntry.arguments?.getString("productId")?.toInt() ?: 0
                        ProductDetailsScreen(productId = productId, onBack = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}