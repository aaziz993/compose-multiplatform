package klib.data.cache

public interface CoroutineCache<K, V> {

    public suspend operator fun get(key: K): V?

    public suspend fun getOrPut(key: K, defaultValue: (key: K) -> V): V =
        get(key) ?: defaultValue(key).also { value ->
            set(key, value)
        }

    public suspend operator fun set(key: K, value: V)

    public suspend fun remove(key: K)

    public suspend fun clear()

    public suspend fun asMap(): Map<K, V>
}

private object NoCoroutineCache : CoroutineCache<Nothing, Nothing> {

    override suspend fun get(key: Nothing): Nothing? = null

    override suspend fun set(key: Nothing, value: Nothing): Unit = Unit

    override suspend fun remove(key: Nothing): Unit = Unit

    override suspend fun clear(): Unit = Unit

    override suspend fun asMap(): Map<Nothing, Nothing> = emptyMap()
}

public fun <K, V> emptyCoroutineCache(): CoroutineCache<K, V> =
    @Suppress("UNCHECKED_CAST") (NoCoroutineCache as CoroutineCache<K, V>)
