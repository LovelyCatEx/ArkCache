package com.lovelycatv.arkcache

import com.lovelycatv.arkcache.operator.KVOperator

interface CacheSourceProvider<T> : KVOperator<T> {
    fun recursiveGet(key: String): T? {
        var cached = get(key)
        if (cached is String) {
            cached = get(cached)
        }
        return cached
    }
}