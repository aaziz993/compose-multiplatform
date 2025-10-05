package klib.data.cache

import klib.data.db.KeyValueQueries
import kotlin.reflect.KClass
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

@Suppress("UNCHECKED_CAST")
public class SqlDelightCache<K : Any, V : Any>(
    private val keyKClass: KClass<K>,
    private val valueKClass: KClass<V>,
    private val queries: KeyValueQueries
) : CoroutineCache<K, V> {

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

    private val toMap: suspend () -> Map<in K, V> =
        if (valueKClass == Any::class) {
            {
                queries.transactionWithResult {
                    queries.selectAll().executeAsList().associate { outKey(it.key) to it.value_ as V }
                }
            }
        }
        else {
            {
                queries.transactionWithResult {
                    queries.selectAll().executeAsList().associate {
                        outKey(it.key) to
                            it.value_?.let { value ->
                                Json.Default.decodeFromString(valueKClass.serializer(), value)
                            } as V
                    }
                }
            }
        }

    override suspend fun get(key: K): V? = queries.transactionWithResult {
        queries.select(inKey(key)).executeAsOneOrNull()?.value_?.let { Json.Default.decodeFromString(valueKClass.serializer(), it) }
    }

    override suspend fun set(key: K, value: V): Unit = queries.transaction {
        queries.insert(inKey(key), Json.Default.encodeToString(valueKClass.serializer(), value))
    }

    override suspend fun remove(key: K): Unit = queries.transaction {
        queries.delete(inKey(key))
    }

    override suspend fun clear(): Unit = queries.transaction {
        queries.deleteAll()
    }

    override suspend fun asMap(): Map<in K, V> = toMap()
}
