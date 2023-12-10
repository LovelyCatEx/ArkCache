package com.lovelycatv.arkcache

import com.lovelycatv.arkcache.strategy.CacheStorageStrategy

interface CacheTemplate<T> {
    fun getOne(strategyId: Int, vararg args: Any): T?

    fun getOne(strategy: CacheStorageStrategy<T>, vararg args: Any): T?

    fun setOne(strategyId: Int, value: T?)

    fun setOne(strategy: CacheStorageStrategy<T>, value: T?)

}