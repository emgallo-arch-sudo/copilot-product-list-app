package com.example.productlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.productlist.data.network.ProductService
import com.example.productlist.ui.screens.LoginScreen
import com.example.productlist.ui.screens.ProductDetailsScreen
import com.example.productlist.ui.screens.ProductListScreen
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
                        LoginScreen(navController = navController)
                    }
                    composable("productList/{username}") { backStackEntry ->
                        val username = backStackEntry.arguments?.getString("username") ?: ""
                        ProductListScreen(
                            navController = navController,
                            productService = ProductService.create(),
                            username = username,
                            onLogout = {
                                navController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                            },
                        )
                    }
                    composable("productDetails/{productId}") { backStackEntry ->
                        val productId = backStackEntry.arguments?.getString("productId")?.toInt() ?: 0
                        ProductDetailsScreen(productId = productId, productService = ProductService.create(), onBack = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}