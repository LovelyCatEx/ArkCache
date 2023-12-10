package com.lovelycatv.arkcache.related

import com.lovelycatv.arkcache.*
import com.lovelycatv.arkcache.strategy.CacheStorageStrategy
import com.lovelycatv.arkcache.strategy.key.FixedCacheKey
import com.lovelycatv.arkcache.strategy.key.MutableCacheKey
import com.lovelycatv.arkcache.strategy.key.MutableCacheKey.MutableKeyProvider
import java.lang.NullPointerException

class BookService {
    private val bookDAO = BookDAO()

    private val container = CacheTemplateContainer()

    init {
        val strategyMulti = CacheStorageStrategy(1, FixedCacheKey<Iterable<Book?>>("book_list"))
        val strategyMulti2 = CacheStorageStrategy(2,
            MutableCacheKey("book-multi:?", object : MutableKeyProvider<Iterable<Book?>> {
                override fun provide(keyFormat: String, vararg data: Any): String {
                    return keyFormat.replace("?", (data[0] as Long).toString())
                }

                override fun provideForSetValue(keyFormat: String, data: Iterable<Book?>): String {
                    with(data.iterator().next()) {
                        if (this != null) {
                            return keyFormat.replace("?", this.id.toString())
                        } else {
                            throw NullPointerException()
                        }
                    }
                }
            })
        )

        container.registerMultiTemplate(Book::class.java) {
            it.apply {
                addStrategy(strategyMulti, strategyMulti2)
                customDataSource(object : DataSourceProvider<Iterable<Book?>> {
                    override fun provide(
                        strategy: CacheStorageStrategy<Iterable<Book?>>,
                        vararg args: Any?
                    ): Iterable<Book?>? {
                        return when (strategy.id) {
                            1 -> bookDAO.list()
                            2 -> bookDAO.byId(args[0] as Long).toList()
                            else -> null
                        }
                    }
                })
            }
        }
    }


    @Suppress("UNCHECKED_CAST")
    fun bookMultiCacheTemplate(): MultiCacheTemplate<Book> = container.getTemplate(Book::class.java) as MultiCacheTemplate<Book>


    fun getBookById(id: Long?): Book? {
        return bookMultiCacheTemplate().getExactlyOne(1, id!!)
    }

    fun getBookByIdInMulti(id: Long?): Book? {
        return bookMultiCacheTemplate().getExactlyOne(2, id!!)
    }

    fun allBooks(): Iterable<Book?>? = bookMultiCacheTemplate().getOne(1)
}
