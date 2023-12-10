package com.lovelycatv.arkcache.strategy.key

const val CACHE_KEY_FIXED = 0
const val CACHE_KEY_MUTABLE = 1
abstract class CacheKey<T> {
    abstract fun getKey(vararg args: Any): String

    abstract fun getKeyForSetValue(arg: T): String

    fun typeOfKey(): Int
        = when(this) {
            is FixedCacheKey<*> -> CACHE_KEY_FIXED
            is MutableCacheKey<*> -> CACHE_KEY_MUTABLE
            else -> throw IllegalStateException("Unrecognized key type of ${this.javaClass.name}")
        }

}