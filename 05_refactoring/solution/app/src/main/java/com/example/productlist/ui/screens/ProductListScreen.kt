// ProductListScreen.kt
package com.example.productlist.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.productlist.data.models.Product
import com.example.productlist.data.network.ProductService
import com.example.productlist.data.network.getCachedProducts
import com.example.productlist.ui.components.ProductCard
import com.example.productlist.ui.theme.ProductListTheme
import kotlinx.coroutines.launch

@Composable
fun ProductListScreen(
    navController: NavController,
    productService: ProductService,
    username: String,
    onLogout: () -> Unit,
) {
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val response = productService.getCachedProducts()
                products = response.products
            } catch (e: Exception) {
                Log.e("ProductListScreen", "Error fetching products", e)
                errorMessage = "Failed to load products. Please try again."
            }
        }
    }

    if (errorMessage != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp, bottom = 64.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = "Welcome, $username!",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(products) { product ->
                    ProductCard(product, onProductClick = { productId -> navController.navigate("productDetails/$productId")})
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductListScreenPreview() {
    ProductListTheme {
        ProductListScreen(navController = rememberNavController(), productService = ProductService.create(), username = "User", onLogout = {})
    }
}