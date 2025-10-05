package klib.data.cache

import klib.data.cache.kache.ObjectKache

public class KCache<K : Any, V : Any>(
    private val cache: ObjectKache<K, V>,
) : CoroutineCache<K, V> {

    override suspend fun get(key: K): V? = cache.get(key)

    override suspend fun set(key: K, value: V) {
        cache.put(key, value)
    }

    override suspend fun remove(key: K) {
        cache.remove(key)
    }

    override suspend fun clear(): Unit = cache.clear()

    override suspend fun asMap(): Map<K, V> =
        cache.getKeys().associateWith { key -> cache.get(key)!! }
}
