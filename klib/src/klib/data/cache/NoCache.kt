package klib.data.cache

public class NoCache<K, V> : Cache<K, V> {
    override fun get(key: K): V? = null

    override fun set(key: K, value: V): Unit = Unit

    override fun remove(key: K): Unit = Unit

    override fun clear(): Unit = Unit

    override fun asMap(): Map<K, V> = emptyMap()
}