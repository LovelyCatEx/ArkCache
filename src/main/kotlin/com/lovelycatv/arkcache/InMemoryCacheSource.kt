package com.lovelycatv.arkcache

class InMemoryCacheSource<T>: CacheSourceProvider<T> {

    companion object {
        @JvmStatic
        private val cache = mutableMapOf<String, Any?>()
    }

    @Suppress("UNCHECKED_CAST")
    override fun get(key: String): T? = cache[key].run { if (this != null) this as T? else null }

    override fun setAny(key: String, value: Any?) {
        cache[key] = value
    }

    override fun remove(key: String) {
        cache.remove(key)
    }
}