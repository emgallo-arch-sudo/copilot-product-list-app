// ProductDetailsScreen.kt
package com.example.productlist

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
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
                product = productService.getProductDetails(productId)
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
        // Show a loading indicator or placeholder
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun Carousel(images: List<String>) {
    // Implement a simple carousel for images
    LazyRow {
        items(images) { image ->
            Image(
                painter = rememberAsyncImagePainter(model = image),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .padding(4.dp)
            )
        }
    }
}

@Composable
fun ReviewCard(review: Review) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = review.user ?: "Anonymous",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = review.body ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Rating: ${review.rating}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
        }
    }
}