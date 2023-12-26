# ArkCache

A library that provides a universal way to store and fetch cache.

By using the CacheTemplate, you can easily access to the cache only by calling a method.

All things you need to do is customize your cache source and data source and add strategies to templates.

# Principle

Now let me sketch the principle of getting cache from the cache or data source.

Call `CacheTemplate<T>.get(strategyId: Int, varage args)` directly in your service methods

**Condition1 : Cache Hit**

Return the cache object from cache source directly

**Condition2 : Cache Misses**

1. By calling `DataSourceProvider.provide(strategy: CacheStorageStrategy, varargs args)` to get the object from data source

2. Write the object to the cache source

3. Return

# Instruction

## 0. Import ArkCache

Maven
```xml
<dependency>
    <groupId>com.lovelycatv</groupId>
    <artifactId>ark-cache</artifactId>
    <version>1.0.1-RELEASE</version>
</dependency>
```

Gradle
```groovy
implementation 'com.lovelycatv:ark-cache:1.0.1-RELEASE'
```

To start with, here are the objects may be used in the following examples

```kotlin
data class Book(id: Long)
```

```kotlin
interface BookDAO {
    fun byId(id: Long): Book?
}
```

## 1. Create CacheTemplate

There two methods to create a new CacheTemplate, here I will introduce how to use CacheTemplateFactory to create a CacheTemplate

### 1. Builder

The library provides two types builder of CacheTemplate

+ Builder
+ MultiBuilder

Call `CacheTemplateFactory.getBuilder(Class<T> clazzOfCacheObject)` to get a builder

Call `CacheTemplateFactory.getMultiBuilder(Class<T> clazzOfCacheObject)` to get a multi-builder (Equivalent to `CacheTemplateFactory.getBuilder(Iterable<T>)::class.java`)

Here we use the normal builder as the example

### 2. Strategy

Strategy determines what key should be used for storing cache objects

+ FixedCacheKey (When you need to store an object with a fixed key, you could try it)
+ MutableCacheKey (Framework will provides the reified cache object or arguments from the `CacheTemplate<T>.get()` so that you could determine the key of cache)

Use the constructor `CacheStorageStrategy(strategyId: Int, cacheKey: CacheKey<T>)` to create a strategy

These are two realistic examples

```kotlin
val fixedKeyStoreStrategy = CacheStorageStrategy(1, FixedCacheKey<Iterable<Book?>>("book_list"))
val mutableKeyStoreStrategy = CacheStorageStrategy(2,
    MutableCacheKey("book:?", object : MutableKeyProvider<Book> {
        override fun provide(keyFormat: String, vararg args: Any): String 
            = keyFormat.replace("?", (args[0] as Long).toString())
        
        override fun provideForSetValue(keyFormat: String, data: Book): String 
            = keyFormat.replace("?", data.toString())
    })
)
```

Add strategies to the cache template

```kotlin
val builder = CacheTemplateFactory.getBuilder(Book::class.java).apply {
    addStrategy(mutableKeyStoreStrategy)
}
```

Because the CacheTemplate built by this builder can only store Book instead of Iterable<Book?>, so we can only add the `mutableKeyStoreStrategy` to the CacheTemplate.

If you do want to store the Iterable data, try `MultiCacheTemplate`

### 3. CacheSource

CacheSource determines how cache object will be stored, you could implement it with your cache source such as Redis or other key-value based database

If you not set the cache source, the CacheTemplate will use inner InMemoryCacheSource

```kotlin
val builder = CacheTemplateFactory.getBuilder(Book::class.java).apply {
    customCacheSource(object: CacheSourceProvider<Book> {
        override fun get(key: String): Book? {
            TODO("Not yet implemented")
        }
        override fun setAny(key: String, value: Any?) {
            TODO("Not yet implemented")
        }
        override fun remove(key: String) {
            TODO("Not yet implemented")
        }
    })
}
```

This is a implementation of InMemoryCacheSource

```kotlin
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
```

### 4. DataSource

When cache is missing, the CacheTemplate will try to find object from your data source.


```kotlin
val builder = CacheTemplateFactory.getBuilder(Book::class.java).apply {
    customDataSource(object: DataSourceProvider<Book> {
        override fun provide(strategy: CacheStorageStrategy<Book>, vararg args: Any?): Book? {
            TODO("Not yet implemented")
        }
    })
}
```

Now with the example in description in strategy, I will show you how to define a DataSourceProvider

```kotlin
val builder = CacheTemplateFactory.getBuilder(Book::class.java).apply {
    customDataSource(object: DataSourceProvider<Book> {
        override fun provide(strategy: CacheStorageStrategy<Book>, vararg args: Any?): Book? {
            return when(strategy.id) {
                2 -> bookDAO.byId(args[0] as Long)
                else -> throw IllegalArgumentException("Unknown strategy")
            }
        }
    })
}
```

---

Now we have an understanding of the entire process of creating a CacheTemplate.

Continuing with the example above, here are the complete example

```kotlin
val cacheTemplate = CacheTemplateFactory.getBuilder(Book::class.java).apply {
    val mutableKeyStoreStrategy = CacheStorageStrategy(2,
        MutableCacheKey("book:?", object : MutableKeyProvider<Book> {
            override fun provide(keyFormat: String, vararg data: Any): String
                    = keyFormat.replace("?", (data[0] as Long).toString())
            override fun provideForSetValue(keyFormat: String, data: Book): String
                    = keyFormat.replace("?", data.toString())
        })
    )
    
    addStrategy(mutableKeyStoreStrategy)
    customDataSource(object: DataSourceProvider<Book> {
        override fun provide(strategy: CacheStorageStrategy<Book>, vararg args: Any?): Book?
            = when(strategy.id) {
                2 -> bookDAO.byId(args[0] as Long)
                else -> throw IllegalArgumentException("Unknown strategy: ${strategy.id}")
            }
    })
}.build()
```
## 2. Use CacheTemplate

Assuming book with id 1L is exists in the data source but not in the cache source.

Now try to get a book from the cache source

```kotlin
println(cacheTemplate.getOne(2, 1L))
```

So all you need to do is set the cache storage strategy and use it to get cache.

## 3. Try CacheTemplateContainer

We may use the CacheTemplate in many different places, the library also provides a simple container to store the CacheTemplate.

```kotlin
val container = CacheTemplateContainer()
container.registerTemplate(Book::class.java) {
    it.apply {
        // it is CacheTemplateFactory.Builder<Book>
    }
}
```

So the example above could be transformed to below

```kotlin
val container = CacheTemplateContainer()
container.registerTemplate(Book::class.java) {
    val mutableKeyStoreStrategy = CacheStorageStrategy(2,
        MutableCacheKey("book:?", object : MutableKeyProvider<Book> {
            override fun provide(keyFormat: String, vararg data: Any): String
                    = keyFormat.replace("?", (data[0] as Long).toString())
            override fun provideForSetValue(keyFormat: String, data: Book): String
                    = keyFormat.replace("?", data.toString())
        })
    )
    it.apply {
        // it is CacheTemplateFactory.Builder<Book>
        addStrategy(mutableKeyStoreStrategy)
        customDataSource(object: DataSourceProvider<Book> {
            override fun provide(strategy: CacheStorageStrategy<Book>, vararg args: Any?): Book?
                    = when(strategy.id) {
                2 -> bookDAO.byId(args[0] as Long)
                else -> throw IllegalArgumentException("Unknown strategy: ${strategy.id}")
            }
        })
    }
}

// Get the CacheTemplate from the container
@Suppress("UNCHECKED_CAST")
val cacheTemplate = container.getTemplate(Book::class.java) as CacheTemplate<Book>
println(cacheTemplate.getOne(2, 1L))
```