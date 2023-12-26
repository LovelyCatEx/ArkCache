package com.lovelycatv.arkcache

import com.lovelycatv.arkcache.related.Book
import com.lovelycatv.arkcache.related.BookDAO
import com.lovelycatv.arkcache.related.BookService
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder

@TestMethodOrder(OrderAnnotation::class)
class MultiTemplateTest {
    companion object {
        private val bookService = BookService()
        private val bookDAO = BookDAO()
    }
    @Order(1)
    @Test
    fun insertOne() {
        println("===== Inserting cache")
        bookService.bookMultiCacheTemplate().setExactlyOne(
            2, Book(10L,"Kotlin入门","Kotlin从入门到如土", arrayOf("Aaron"),"2023-10-21",4.6f)
        )
    }

    @Order(2)
    @Test
    fun readOne() {
        println("===== Reading cache")
        println(bookService.bookMultiCacheTemplate().getExactlyOne(2, 10L))
    }

    @Order(3)
    @Test
    fun removeOne() {
        println("===== Remove cache")
        bookService.bookMultiCacheTemplate().removeCache(2, 10L)
    }

    @Order(4)
    @Test
    fun readList() {
        println("===== Read iterable cache")
        bookService.allBooks()?.forEach {
            println(it)
        }
    }

    @Order(5)
    @Test
    fun clearAllCache() {
        println("===== Reading cache 1")
        var s = System.currentTimeMillis()
        println(bookService.bookMultiCacheTemplate().getExactlyOne(2, 1L)).also { println(System.currentTimeMillis() - s) }

        println("===== Remove all cache")
        bookService.bookMultiCacheTemplate().keys().forEach {
            println("key: $it")
        }
        bookService.bookMultiCacheTemplate().removeAllCache()

        println("===== Reading cache 1 Again")
        s = System.currentTimeMillis()
        println(bookService.bookMultiCacheTemplate().getExactlyOne(2, 1L)).also { println(System.currentTimeMillis() - s) }

        println("===== Reading cache 1 Again...")
        s = System.currentTimeMillis()
        println(bookService.bookMultiCacheTemplate().getExactlyOne(2, 1L)).also { println(System.currentTimeMillis() - s) }

    }
}
