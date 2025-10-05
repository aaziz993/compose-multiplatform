package klib.data.cache.room

import klib.data.cache.CoroutineCache
import klib.data.cache.room.model.Cache
import kotlin.reflect.KClass
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

@Suppress("UNCHECKED_CAST")
public class RoomCache<K : Any, V : Any>(
    private val keyKClass: KClass<K>,
    private val valueKClass: KClass<V>,
    database: CacheDatabase
) : CoroutineCache<K, V> {

    private val dao = database.getDao()

    private val inKey: (key: K) -> String = if (keyKClass == String::class) {
        { it as String }
    }
    else {
        { Json.Default.encodeToString(keyKClass.serializer(), it) }
    }

    private val outKey: (key: String) -> K = if (keyKClass == String::class) {
        { it as K }
    }
    else {
        { Json.Default.decodeFromString(keyKClass.serializer(), it) }
    }

    private val toMap: suspend () -> Map<K, V> = if (valueKClass == Any::class) {
        { dao.selectAll().listIterator().asSequence().associate { outKey(it.key) to it.value as V } }
    }
    else {
        { dao.selectAll().listIterator().asSequence().associate { outKey(it.key) to Json.Default.decodeFromString(valueKClass.serializer(), it.value) } }
    }

    override suspend fun get(key: K): V? =
        dao.select(inKey(key))?.value?.let { Json.Default.decodeFromString(valueKClass.serializer(), it) }

    override suspend fun set(key: K, value: V): Unit =
        dao.insert(Cache(inKey(key), Json.Default.encodeToString(valueKClass.serializer(), value)))

    override suspend fun remove(key: K): Unit = dao.delete(inKey(key))

    override suspend fun clear(): Unit = dao.deleteAll()

    override suspend fun asMap(): Map<K, V> = toMap()
}
