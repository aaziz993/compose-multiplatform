package klib.data.cache

public interface Cache<K, V> {

    public operator fun get(key: K): V?

    public fun getOrPut(key: K, defaultValue: (key: K) -> V): V = get(key) ?: defaultValue(key).also { value ->
        set(key, value)
    }

    public operator fun set(key: K, value: V)

    public fun remove(key: K)

    public fun clear()

    public fun asMap(): Map<K, V>
}

private object NoCache : Cache<Nothing, Nothing> {

    override fun get(key: Nothing): Nothing? = null

    override fun set(key: Nothing, value: Nothing): Unit = Unit

    override fun remove(key: Nothing): Unit = Unit

    override fun clear(): Unit = Unit

    override fun asMap(): Map<Nothing, Nothing> = emptyMap()
}

public fun <K, V> emptyCache(): Cache<K, V> = @Suppress("UNCHECKED_CAST") (NoCache as Cache<K, V>)
