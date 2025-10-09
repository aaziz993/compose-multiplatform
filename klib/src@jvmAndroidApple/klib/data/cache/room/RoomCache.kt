package klib.data.cache.room

import klib.data.cache.CoroutineCache
import klib.data.cache.room.model.Cache

@Suppress("UNCHECKED_CAST")
public class RoomCache<K : Any, V : Any>(
    private val keyEncoder: (K) -> String = { key -> key as String },
    private val keyDecoder: (String) -> K = { key -> key as K },
    private val valueEncoder: (V) -> String = { value -> value as String },
    private val valueDecoder: (String) -> V = { value -> value as V },
    database: CacheDatabase
) : CoroutineCache<K, V> {

    private val dao = database.getDao()

    override suspend fun get(key: K): V? =
        dao.select(keyEncoder(key))?.value?.let(valueDecoder)

    override suspend fun set(key: K, value: V): Unit =
        dao.insert(Cache(key = keyEncoder(key), value = valueEncoder(value)))

    override suspend fun remove(key: K): Unit = dao.delete(keyEncoder(key))

    override suspend fun clear(): Unit = dao.deleteAll()

    override suspend fun asMap(): Map<K, V> =
        dao.selectAll().listIterator().asSequence().associate { (_, key, value) ->
            keyDecoder(key) to valueDecoder(value)
        }
}
