package com.example.productlist.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.productlist.data.models.Product
import com.example.productlist.data.network.ProductService
import com.example.productlist.data.network.getCachedProductDetails
import com.example.productlist.ui.components.Carousel
import com.example.productlist.ui.components.ReviewCard
import kotlinx.coroutines.launch

@Composable
fun ProductDetailsScreen(productId: Int, productService: ProductService, onBack: () -> Unit) {
    var product by remember { mutableStateOf<Product?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(productId) {
        Log.d("ProductDetailsScreen", "Fetching product details for productId: $productId")
        coroutineScope.launch {
            try {
                product = productService.getCachedProductDetails(productId)
                Log.d("ProductDetailsScreen", "Fetched product: $product")
            } catch (e: Exception) {
                Log.e("ProductDetailsScreen", "Error fetching product details", e)
                errorMessage = "Failed to load product details. Please try again."
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
    } else if (product != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Button(onClick = onBack) {
                Text("Back")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = product!!.title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Carousel(images = product!!.images)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Rating: ${product!!.rating}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Stock: ${product!!.stock}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Reviews",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.Black
            )
            LazyColumn {
                items(product!!.reviews) { review ->
                    ReviewCard(review)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}