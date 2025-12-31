package klib.data.cache

import app.cash.sqldelight.Query
import klib.data.database.KeyValue
import klib.data.database.KeyValueQueries

@Suppress("UNCHECKED_CAST")
public class SqlDelightCoroutineCache<K, V>(
    private val keyEncoder: (K) -> String? = { key -> key as String? },
    private val keyDecoder: (String?) -> K = { key -> key as K },
    private val valueEncoder: (V) -> String? = { value -> value as String? },
    private val valueDecoder: (String?) -> V = { value -> value as V },
    private val queries: KeyValueQueries
) : CoroutineCache<K, V> {

    override suspend fun get(key: K): V? = queries.transactionWithResult {
        queries.selectByKey(keyEncoder(key)).executeAsOneOrNull()?.value_?.let(valueDecoder)
    }

    override suspend fun set(key: K, value: V): Unit = queries.transaction {
        queries.insertUniqueNullKey(keyEncoder(key), valueEncoder(value))
    }

    override suspend fun remove(key: K): Unit = queries.transaction {
        queries.deleteByKey(keyEncoder(key))
    }

    override suspend fun clear(): Unit = queries.transaction {
        queries.deleteAll()
    }

    override suspend fun asMap(): Map<K, V> = queries.transactionWithResult {
        queries.selectAll().executeAsList().associate { (key, value) ->
            keyDecoder(key) to valueDecoder(value)
        }
    }
}

private suspend fun KeyValueQueries.insertUniqueNullKey(key: String?, value: String?): Long =
    transactionWithResult {
        if (key == null) deleteKeyIsNull()
        insertOrReplace(key, value)
    }

private fun KeyValueQueries.selectByKey(key: String?): Query<KeyValue> =
    if (key == null) selectKeyIsNull() else selectByKeyNotNull(key)

private suspend fun KeyValueQueries.deleteByKey(key: String?): Long =
    if (key == null) deleteKeyIsNull() else deleteByKeyNotNull(key)
