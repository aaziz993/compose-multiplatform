package klib.data.cache

import klib.data.db.KeyValueQueries
import kotlin.reflect.KClass
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

@Suppress("UNCHECKED_CAST")
public class SqlDelightCache<K : Any, V : Any>(
    private val keyEncoder: (K) -> String = { key -> key as String },
    private val keyDecoder: (String) -> K = { key -> key as K },
    private val valueEncoder: (V) -> String = { value -> value as String },
    private val valueDecoder: (String) -> V = { value -> value as V },
    private val queries: KeyValueQueries
) : CoroutineCache<K, V> {

    override suspend fun get(key: K): V? = queries.transactionWithResult {
        queries.select(keyEncoder(key)).executeAsOneOrNull()?.value_?.let(valueDecoder)
    }

    override suspend fun set(key: K, value: V): Unit = queries.transaction {
        queries.insert(keyEncoder(key), valueEncoder(value))
    }

    override suspend fun remove(key: K): Unit = queries.transaction {
        queries.delete(keyEncoder(key))
    }

    override suspend fun clear(): Unit = queries.transaction {
        queries.deleteAll()
    }

    override suspend fun asMap(): Map<K, V> = queries.transactionWithResult {
        queries.selectAll().executeAsList().associate { (key, value) ->
            keyDecoder(key) to value?.let(valueDecoder) as V
        }
    }
}
