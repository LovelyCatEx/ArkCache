package com.lovelycatv.arkcache

infix fun <T> MutableList<T>.plus(value: T): MutableList<T> {
    this.add(value)
    return this
}

fun <T> T.toList(): MutableList<T> = mutableListOf<T>().plus(this)


fun main(args: Array<String>) {
    println("Ark Cache")
}