// CacheManager.kt
package com.example.productlist.data.cache

import android.util.LruCache
import java.util.concurrent.TimeUnit

data class CacheEntry<T>(val value: T, val timestamp: Long)

object CacheManager {
    private val cache = LruCache<String, CacheEntry<Any>>(100)
    private val ttl = TimeUnit.HOURS.toMillis(1)

    fun <T : Any> get(key: String, loader: () -> T): T {
        val entry = cache.get(key) as CacheEntry<T>?
        return if (entry != null && System.currentTimeMillis() - entry.timestamp < ttl) {
            entry.value
        } else {
            loader().also { cache.put(key, CacheEntry(it, System.currentTimeMillis())) }
        }
    }
}