package com.lovelycatv.arkcache

import com.lovelycatv.arkcache.strategy.CacheStorageStrategy

abstract class CacheTemplate<T> {
    protected val keys: MutableList<String> = mutableListOf()

    abstract fun getOne(strategyId: Int, vararg args: Any): T?

    abstract fun getOne(strategy: CacheStorageStrategy<T>, vararg args: Any): T?

    abstract fun setOne(strategyId: Int, value: T?)

    abstract fun setOne(strategy: CacheStorageStrategy<T>, value: T?)

    abstract fun removeCache(strategyId: Int, vararg args: Any)

    abstract fun removeCache(strategy: CacheStorageStrategy<T>, vararg args: Any)

    abstract fun getDataSourceProvider(): DataSourceProvider<T>

    abstract fun getCacheSourceProvider(): CacheSourceProvider<T>

    fun removeAllCache() {
        keys.forEach {
            getCacheSourceProvider().remove(it)
        }
    }

    fun keys() = keys

}