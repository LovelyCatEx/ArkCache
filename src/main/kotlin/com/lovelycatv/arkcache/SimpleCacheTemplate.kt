package com.lovelycatv.arkcache

import com.lovelycatv.arkcache.strategy.CacheStorageStrategy

fun <T> Array<CacheStorageStrategy<T>>.toMap(): Map<Int, CacheStorageStrategy<T>>
    = with(mutableMapOf<Int, CacheStorageStrategy<T>>()) {
        this@toMap.forEach {
            this[it.id] = it
        }
        this
    }

open class SimpleCacheTemplate<T>(
     private var dataSourceProvider: DataSourceProvider<T>,
     private var cacheSourceProvider: CacheSourceProvider<T>,
     private var cacheStorageStrategies: MutableMap<Int, CacheStorageStrategy<T>>
) : CacheTemplate<T>() {
    fun getStrategyById(strategyId: Int) = this.cacheStorageStrategies[strategyId] ?: throwWhenStrategyIsNull(strategyId)

    override fun getOne(strategyId: Int, vararg args: Any): T?
        = getOne(getStrategyById(strategyId), *args)

    override fun getOne(strategy: CacheStorageStrategy<T>, vararg args: Any): T?
        = (cacheSourceProvider.recursiveGet(strategy.key.getKey(*args)) ?: dataSourceProvider.provide(strategy, *args)?.also { setOne(strategy, it) })
        ?: throw NullPointerException("Cannot found cache or real-object through strategy [$strategy] with args: [$args] in CacheSource and DataSource")

    override fun setOne(strategyId: Int, value: T?) {
        setOne(getStrategyById(strategyId), value)
    }

    override fun setOne(strategy: CacheStorageStrategy<T>, value: T?) {
        with(strategy.key.getKeyForSetValue(value ?: throw NullPointerException("Cache going to be set cannot be null"))) {
            cacheSourceProvider.set(this, value)
            super.keys.add(this)
        }
    }

    override fun removeCache(strategyId: Int, vararg args: Any) {
        removeCache(getStrategyById(strategyId), *args)
    }

    override fun removeCache(strategy: CacheStorageStrategy<T>, vararg args: Any) {
        with(strategy.key.getKey(*args)) {
            cacheSourceProvider.remove(this)
            super.keys.remove(this)
        }
    }

    override fun getDataSourceProvider(): DataSourceProvider<T>
            = dataSourceProvider

    override fun getCacheSourceProvider(): CacheSourceProvider<T>
            = cacheSourceProvider

    private fun throwWhenStrategyIsNull(strategyId: Int): Nothing {
        throw NullPointerException("Cannot find strategy $strategyId")
    }
}