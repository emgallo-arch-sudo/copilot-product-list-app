# Chat Transcription

### Me
Refactor the code by moving @Composable in separate packages following Android best practices

### Copilot
To follow Android best practices, we can refactor the code by moving the `@Composable` functions into separate packages. This will help in organizing the code better and making it more maintainable.

1. Create a new package `ui.screens` for the screen composables.
2. Create a new package `ui.components` for the reusable components.

Here is the refactored code:

**`app/src/main/java/com/example/productlist/ui/screens/ProductDetailsScreen.kt`**:
```kotlin
package com.example.productlist.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.productlist.ProductService
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
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
```

**`app/src/main/java/com/example/productlist/ui/screens/LoginScreen.kt`**:
```kotlin
package com.example.productlist.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.productlist.ProductService
import com.example.productlist.ui.theme.ProductListTheme
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController, productService: ProductService = ProductService.create()) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = username,
            onValueChange = {
                username = it
                usernameError = null
            },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            isError = usernameError != null,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            )
        )
        if (usernameError != null) {
            Text(
                text = usernameError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = null
            },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            isError = passwordError != null,
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = description)
                }
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            )
        )
        if (passwordError != null) {
            Text(
                text = passwordError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                var valid = true
                if (username.isBlank()) {
                    usernameError = "Username cannot be empty"
                    valid = false
                }
                if (password.isBlank()) {
                    passwordError = "Password cannot be empty"
                    valid = false
                }
                if (valid) {
                    isLoading = true
                    coroutineScope.launch {
                        try {
                            val response = productService.login(LoginRequest(username, password))
                            navController.navigate("productlist/${response.firstName} ${response.lastName}")

                        } catch (e: Exception) {
                            Log.e("LoginScreen", "Error logging in", e)
                            errorMessage = "Invalid username or password. Please try again."
                        } finally {
                            isLoading = false
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Submit")
            }
        }
        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }

    SnackbarHost(hostState = snackbarHostState)
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    ProductListTheme {
        LoginScreen(navController = rememberNavController())
    }
}
```

**`app/src/main/java/com/example/productlist/ui/components/Carousel.kt`**:
```kotlin
package com.example.productlist.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun Carousel(images: List<String>) {
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
```

**`app/src/main/java/com/example/productlist/ui/components/ReviewCard.kt`**:
```kotlin
package com.example.productlist.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.productlist.Review

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
```

This refactoring organizes the code into separate packages for screens and components, following Android best practices.

### Me
Refactor by moving data classes in a separate package

### Copilot
To follow best practices, we can refactor the code by moving the data classes into a separate package. This will help in organizing the code better and making it more maintainable.

1. Create a new package `data.models` for the data classes.

Here is the refactored code:

**`app/src/main/java/com/example/productlist/data/models/LoginRequest.kt`**:
```kotlin
package com.example.productlist.data.models

data class LoginRequest(
    val username: String,
    val password: String,
    val expiresInMins: Int? = 60
)
```

**`app/src/main/java/com/example/productlist/data/models/Product.kt`**:
```kotlin
package com.example.productlist.data.models

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: String,
    val thumbnail: String,
    val images: List<String>,
    val rating: Float,
    val stock: Int,
    val reviews: List<Review>
)
```

**`app/src/main/java/com/example/productlist/data/models/Review.kt`**:
```kotlin
package com.example.productlist.data.models

data class Review(
    val user: String?,
    val body: String?,
    val rating: Float
)
```

Make sure to update the imports in your existing files to reflect the new package structure. For example, in `ProductDetailsScreen.kt`:

```kotlin
import com.example.productlist.data.models.Product
import com.example.productlist.data.models.Review
```

And in `LoginScreen.kt`:

```kotlin
import com.example.productlist.data.models.LoginRequest
```

### Me
Can you help me with moving the network service classes to a separate package?

### Copilot
To follow best practices, we can refactor the code by moving the network service classes into a separate package. This will help in organizing the code better and making it more maintainable.

1. Create a new package `data.network` for the network service classes.

Here is the refactored code:

**`app/src/main/java/com/example/productlist/data/network/ProductService.kt`**:
```kotlin
package com.example.productlist.data.network

import com.example.productlist.data.models.LoginRequest
import com.example.productlist.data.models.LoginResponse
import com.example.productlist.data.models.Product
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

data class ProductResponse(
    val products: List<Product>
)
```

Make sure to update the imports in your existing files to reflect the new package structure. For example, in `ProductDetailsScreen.kt`:

```kotlin
import com.example.productlist.data.network.ProductService
```

And in `LoginScreen.kt`:

```kotlin
import com.example.productlist.data.network.ProductService
```

### Me
Optimize API calls using a caching mechanism with a TTL of 1 hour

### Copilot
To optimize API calls using a caching mechanism with a TTL (Time-To-Live) of 1 hour, you can use a caching library like `Caffeine`. Here is how you can integrate it into your project:

1. Add the `Caffeine` dependency to your `build.gradle` file:
    ```groovy
    implementation 'com.github.ben-manes.caffeine:caffeine:3.0.4'
    ```

2. Create a `CacheManager` class to handle caching:
    ```kotlin
    // CacheManager.kt
    package com.example.productlist.data.cache

    import com.github.benmanes.caffeine.cache.Caffeine
    import java.util.concurrent.TimeUnit

    object CacheManager {
        private val cache = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build<String, Any>()

        fun <T> get(key: String, loader: () -> T): T {
            return cache.get(key) { loader() } as T
        }

        fun put(key: String, value: Any) {
            cache.put(key, value)
        }

        fun invalidate(key: String) {
            cache.invalidate(key)
        }
    }
    ```

3. Update the `ProductService` to use the cache:
    ```kotlin
    // ProductService.kt
    package com.example.productlist.data.network

    import com.example.productlist.data.cache.CacheManager
    import com.example.productlist.data.models.LoginRequest
    import com.example.productlist.data.models.LoginResponse
    import com.example.productlist.data.models.Product
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

    suspend fun ProductService.getCachedProductDetails(id: Int): Product {
        return CacheManager.get("product_$id") {
            getProductDetails(id)
        }
    }

    suspend fun ProductService.getCachedProducts(): ProductResponse {
        return CacheManager.get("products") {
            getProducts()
        }
    }
    ```

4. Update the `ProductDetailsScreen` to use the cached API call:
    ```kotlin
    // ProductDetailsScreen.kt
    package com.example.productlist.ui.screens

    import android.util.Log
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.material3.*
    import androidx.compose.runtime.*
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.unit.dp
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
    ```

This setup uses `Caffeine` to cache API responses for 1 hour, reducing the number of network calls and improving performance.

### Me
Use a older caffeine version that supports Android 21 or use alternative caching method

### Copilot
To use a caching mechanism compatible with Android API level 21, you can use the `LruCache` class from the Android framework instead of `Caffeine`. Here is how you can implement it:

1. Create a `CacheManager` class using `LruCache`:

```kotlin
// CacheManager.kt
package com.example.productlist.data.cache

import android.util.LruCache

object CacheManager {
    private val cache = LruCache<String, Any>(100)

    fun <T> get(key: String, loader: () -> T): T {
        @Suppress("UNCHECKED_CAST")
        return cache.get(key) as T? ?: loader().also { cache.put(key, it) }
    }

    fun put(key: String, value: Any) {
        cache.put(key, value)
    }

    fun invalidate(key: String) {
        cache.remove(key)
    }
}
```

2. Update the `ProductService` to use the new `CacheManager`:

```kotlin
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
```

This setup uses `LruCache` to cache API responses, which is compatible with Android API level 21.

### Me
Can you implement the caching mechanism to have a TTL of 1 hour?

### Copilot
To implement a caching mechanism with a TTL (Time-To-Live) of 1 hour using `LruCache`, you can extend the `CacheManager` to include a timestamp for each cache entry and check the TTL before returning the cached value.

Here is the updated `CacheManager` class:

```kotlin
// CacheManager.kt
package com.example.productlist.data.cache

import android.util.LruCache
import java.util.concurrent.TimeUnit

data class CacheEntry<T>(val value: T, val timestamp: Long)

object CacheManager {
    private val cache = LruCache<String, CacheEntry<Any>>(100)
    private val ttl = TimeUnit.HOURS.toMillis(1)

    fun <T> get(key: String, loader: () -> T): T {
        val entry = cache.get(key) as CacheEntry<T>?
        return if (entry != null && System.currentTimeMillis() - entry.timestamp < ttl) {
            entry.value
        } else {
            loader().also { cache.put(key, CacheEntry(it, System.currentTimeMillis())) }
        }
    }

    fun put(key: String, value: Any) {
        cache.put(key, CacheEntry(value, System.currentTimeMillis()))
    }

    fun invalidate(key: String) {
        cache.remove(key)
    }
}
```

This implementation uses a `CacheEntry` data class to store the value and the timestamp of when it was cached. The `get` method checks if the cached entry is still valid based on the TTL before returning it. If the entry is expired or not present, it loads a new value and caches it with the current timestamp.

### Me
How can I optimize the app's code?

### Copilot
To optimize the app's code, consider the following improvements:

1. **Use `suspend` functions directly in `LaunchedEffect`**:
   Avoid using `rememberCoroutineScope` and `launch` inside `LaunchedEffect`. Instead, call the `suspend` function directly.

2. **Cache API responses**:
   Use the caching mechanism in `ProductListScreen` to avoid unnecessary network calls.

3. **Improve error handling**:
   Handle errors more gracefully and provide user feedback.

Here is the optimized `ProductListScreen`:

```kotlin
// ProductListScreen.kt
package com.example.productlist.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.productlist.data.models.Product
import com.example.productlist.data.network.ProductService
import com.example.productlist.data.network.getCachedProducts
import com.example.productlist.ui.theme.ProductListTheme

@Composable
fun ProductListScreen(
    navController: NavController,
    productService: ProductService,
    username: String,
    onLogout: () -> Unit,
) {
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val response = productService.getCachedProducts()
            products = response.products
        } catch (e: Exception) {
            Log.e("ProductListScreen", "Error fetching products", e)
            errorMessage = "Failed to load products. Please try again."
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
                    ProductCard(product, onProductClick = { productId -> navController.navigate("productDetails/$productId") })
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

@Composable
fun ProductCard(product: Product, onProductClick: (Int) -> Unit) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick(product.id) }
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = product.thumbnail),
                contentDescription = product.title,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = product.price,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
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
```

This code optimizes the `ProductListScreen` by directly using `suspend` functions in `LaunchedEffect`, caching API responses, and improving error handling.