package com.lovelycatv.arkcache.strategy.key

class MutableCacheKey<T>(
    private val keyFormat: String,
    private val mutableKeyProvider: MutableKeyProvider<T>
) : CacheKey<T>() {
    override fun getKey(vararg args: Any): String = this.mutableKeyProvider.provide(keyFormat, *args)
    override fun getKeyForSetValue(arg: T): String = this.mutableKeyProvider.provideForSetValue(keyFormat, arg)

    interface MutableKeyProvider<T> {
        fun provide(keyFormat: String, vararg args: Any): String

        fun provideForSetValue(keyFormat: String, data: T): String
    }
}