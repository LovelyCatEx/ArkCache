package com.lovelycatv.arkcache.strategy

import com.lovelycatv.arkcache.strategy.key.CacheKey
import com.lovelycatv.arkcache.strategy.key.FixedCacheKey
import com.lovelycatv.arkcache.strategy.key.MutableCacheKey

data class CacheStorageStrategy<T>(
    val id: Int,
    val key: CacheKey<T>
) {


    fun getKeyToFixed(): FixedCacheKey<T>
        = if (key is FixedCacheKey) key else throw IllegalStateException("The cache key object is not a FixedCacheKey")

    fun getKeyToMutable(): MutableCacheKey<T>
            = if (key is MutableCacheKey) key else throw IllegalStateException("The cache key object is not a MutableCacheKey")
}