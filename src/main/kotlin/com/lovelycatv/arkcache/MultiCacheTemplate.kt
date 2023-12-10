package com.lovelycatv.arkcache

import com.lovelycatv.arkcache.strategy.CacheStorageStrategy

class MultiCacheTemplate<T>(dataSourceProvider: DataSourceProvider<Iterable<T?>>,
                            cacheSourceProvider: CacheSourceProvider<Iterable<T?>>,
                            cacheStorageStrategies: MutableMap<Int, CacheStorageStrategy<Iterable<T?>>>
) : MultiCacheSupported<T>, SimpleCacheTemplate<Iterable<T?>>(dataSourceProvider, cacheSourceProvider, cacheStorageStrategies) {
    override fun getExactlyOne(strategyId: Int, vararg args: Any): T? {
        return getExactlyOne(getStrategyById(strategyId), *args)
    }

    override fun getExactlyOne(strategy: CacheStorageStrategy<Iterable<T?>>, vararg args: Any): T? {
        return super.getOne(strategy, *args)?.iterator()?.next()
    }

    override fun setExactlyOne(strategyId: Int, value: T?) {
        setExactlyOne(getStrategyById(strategyId), value)
    }

    override fun setExactlyOne(strategy: CacheStorageStrategy<Iterable<T?>>, value: T?) {
        super.setOne(strategy, (mutableListOf<T?>() + value) as Iterable<T?>)
    }

}