package com.lovelycatv.arkcache.related

import com.lovelycatv.arkcache.*
import com.lovelycatv.arkcache.strategy.CacheStorageStrategy

class CacheTemplateFactory {

    companion object {
        @JvmStatic
        fun <T> getBuilder(clazz: Class<T>): Builder<T> = Builder()

        @JvmStatic
        fun <T> getMultiBuilder(clazz: Class<T>): MultiBuilder<T> = MultiBuilder()
    }

    open class Builder<T> {
        protected var dataSourceProvider: DataSourceProvider<T>? = null
        protected var cacheSourceProvider: CacheSourceProvider<T>? = null
        protected var cacheStorageStrategies: MutableMap<Int, CacheStorageStrategy<T>> = mutableMapOf()

        init {
            inMemoryCacheSource()
        }

        fun inMemoryCacheSource() {
            this.cacheSourceProvider = InMemoryCacheSource()
        }

        fun customDataSource(dataSourceProvider: DataSourceProvider<T>) {
            this.dataSourceProvider = dataSourceProvider
        }

        fun customCacheSource(cacheSourceProvider: CacheSourceProvider<T>) {
            this.cacheSourceProvider = cacheSourceProvider
        }

        fun addStrategy(vararg cacheStorageStrategy: CacheStorageStrategy<T>) {
            cacheStorageStrategy.forEach {
                cacheStorageStrategies[it.id] = it
            }
        }

        fun build(): SimpleCacheTemplate<T> = SimpleCacheTemplate(this.dataSourceProvider!!, this.cacheSourceProvider!!, this.cacheStorageStrategies)
    }

    class MultiBuilder<T> : Builder<Iterable<T?>>() {
        fun buildToMulti() = MultiCacheTemplate(this.dataSourceProvider!!, this.cacheSourceProvider!!, this.cacheStorageStrategies)
    }
}