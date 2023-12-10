package com.lovelycatv.arkcache

import com.lovelycatv.arkcache.strategy.CacheStorageStrategy

interface DataSourceProvider<T> {
    fun provide(strategy: CacheStorageStrategy<T>, vararg args: Any?): T?
}