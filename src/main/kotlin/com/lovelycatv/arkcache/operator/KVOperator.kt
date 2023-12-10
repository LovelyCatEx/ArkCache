package com.lovelycatv.arkcache.operator

interface KVOperator<T> {
    fun get(key: String): T?

    fun set(key: String, value: T?) {
        setAny(key, value)
    }

    fun setAny(key: String, value: Any?)

    fun remove(key: String)
}