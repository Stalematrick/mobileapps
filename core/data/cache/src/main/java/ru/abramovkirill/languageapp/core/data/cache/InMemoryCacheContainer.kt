package ru.abramovkirill.languageapp.core.data.cache

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

/**
 * Utility class for caching data. Use this class in repositories to prevent
 * a lot of requests to server or in other use-cases
 */
class InMemoryCacheContainer<T>(
    /**
     * The life time of cache
     */
    private val cacheLifeTime: Long = CACHE_LIFE_TIME
) {

    /**
     * Cached data
     */
    private val cache: MutableStateFlow<T?> = MutableStateFlow(null)

    /**
     * The last cache write time
     */
    private var lastUpdateTime = 0L

    /**
     * Returns true, if the cache has been expired.
     */
    val isExpired get() =
        System.currentTimeMillis() - lastUpdateTime >= cacheLifeTime || cache.value == null

    /**
     * Returns true, if the cache has not been expired.
     */
    val isNotExpired get() = !isExpired

    var value: T?
        get() {
            if (isExpired) cache.value = null
            return cache.value
        }
        set(value) {
            lastUpdateTime = System.currentTimeMillis()
            cache.value = value
        }

    val valueFlow = cache.asStateFlow()

    suspend fun getValueOrSave(valueProvider: suspend () -> T): T {
        return this.value ?: valueProvider().also { newValue -> this.value = newValue }
    }

    fun valueFlow(defaultDataProvider: suspend () -> T): Flow<T> {
        return valueFlow
            .map { value -> value ?: defaultDataProvider() }
    }

    fun clear() {
        cache.value = null
    }

    companion object {
        private const val CACHE_LIFE_TIME = 15000L
    }
}
