package klib.data.cache

public class Cache4K<K : Any, V : Any>(
    private val cache: io.github.reactivecircus.cache4k.Cache<K, V>,
) : Cache<K, V> {

    override fun get(key: K): V? = cache.get(key)

    override fun set(key: K, value: V): Unit = cache.put(key, value)

    override fun remove(key: K): Unit = cache.invalidate(key)

    override fun clear(): Unit = cache.invalidateAll()

    override fun asMap(): Map<in K, V> = cache.asMap()
}
