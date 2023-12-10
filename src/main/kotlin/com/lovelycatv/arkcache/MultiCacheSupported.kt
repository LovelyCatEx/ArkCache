package com.lovelycatv.arkcache

import com.lovelycatv.arkcache.strategy.CacheStorageStrategy

interface MultiCacheSupported<T> {
    fun getExactlyOne(strategyId: Int, vararg args: Any): T?

    fun getExactlyOne(strategy: CacheStorageStrategy<Iterable<T?>>, vararg args: Any): T?

    fun setExactlyOne(strategyId: Int, value: T?)

    fun setExactlyOne(strategy: CacheStorageStrategy<Iterable<T?>>, value: T?)
}