package klib.data.cache

import io.github.xxfast.kstore.KStore

public open class KStoreCache<K, V>(
    private val store: KStore<Map<K, V>>,
) : CoroutineCache<K, V> {

    override suspend fun get(key: K): V? = store.get()!![key]

    override suspend fun set(key: K, value: V): Unit = store.set(store.get()!! + (key to value))

    override suspend fun remove(key: K): Unit = store.set(store.get()?.filterKeys { it == key })

    override suspend fun clear(): Unit = store.delete()

    override suspend fun asMap(): Map<K, V> = store.get()!!
}
