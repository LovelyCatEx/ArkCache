package com.lovelycatv.arkcache.strategy.key

class FixedCacheKey<T>(private val fixedKey: String) : CacheKey<T>() {
    override fun getKey(vararg args: Any): String = fixedKey
    override fun getKeyForSetValue(arg: T): String = fixedKey
}