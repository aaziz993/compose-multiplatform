package klib.data.cache.room

import data.cache.AbstractCoroutineCache
import klib.data.cache.room.model.Cache
import kotlin.reflect.KClass
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

@Suppress("UNCHECKED_CAST")
public class RoomCache<in K : Any, V : Any>(
    private val keyKClass: KClass<K>,
    private val valueKClass: KClass<V>,
    database: CacheDatabase
) : AbstractCoroutineCache<K, V>() {

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

    private val toMap: suspend () -> Map<in K, V> = if (valueKClass == Any::class) {
        { dao.selectAll().listIterator().asSequence().associate { outKey(it.key) to it.value as V } }
    }
    else {
        { dao.selectAll().listIterator().asSequence().associate { outKey(it.key) to Json.Default.decodeFromString(valueKClass.serializer(), it.value) } }
    }

    override suspend fun _get(key: K): V? =
        dao.select(inKey(key))?.value?.let { Json.Default.decodeFromString(valueKClass.serializer(), it) }

    override suspend fun _put(key: K, value: V): Unit =
        dao.insert(Cache(key = inKey(key), value = Json.Default.encodeToString(valueKClass.serializer(), value)))

    override suspend fun _remove(key: K): Unit = dao.delete(inKey(key))

    override suspend fun _clear(): Unit = dao.deleteAll()

    override suspend fun _asMap(): Map<in K, V> = toMap()
}
