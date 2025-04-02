package klib.data.type.collection

import klib.data.type.act
import klib.data.type.serialization.serializer.AnySerializer
import klib.data.type.serialization.serializer.OptionalAnySerializer
import kotlinx.serialization.Serializable
import kotlin.collections.get
import kotlin.invoke

public typealias SerializableAnyMap = Map<String, @Serializable(with = AnySerializer::class) Any>

public typealias SerializableOptionalAnyMap = Map<String, @Serializable(with = OptionalAnySerializer::class) Any?>

public infix fun <K, V> MutableMap<K, V>.tryPutAll(value: Map<K, V>?): Unit? =
    value?.let(::putAll)

public infix fun <K, V> MutableMap<K, V>.trySet(value: Map<K, V>?): Unit? =
    tryPutAll(value?.act(::clear))

public fun <K, V> Map<K, V>.slice(keys: Iterable<K>): Map<K, V> =
    buildMap {
        keys.forEach { key ->
            if (containsKey(key))
                put(key, this@slice[key]!!)
        }
    }

@Suppress("UNCHECKED_CAST")
public infix fun Map<String, Any?>.deepMerge(source: Map<String, Any?>): Map<String, Any?> {
    val resultMap = toMutableMap()
    for (key in source.keys) {
        //recursive merge for nested maps
        when {
            source[key] is Map<*, *> && (resultMap[key] is Map<*, *> || resultMap[key] == null) -> {
                val originalChild = (resultMap[key] as? Map<String, Any?>) ?: mutableMapOf()
                val newChild = source[key] as Map<String, Any?>
                resultMap[key] = originalChild deepMerge newChild
            }

            source[key] is Collection<*> && (resultMap[key] is Collection<*> || resultMap[key] == null) -> {
                val originalChild = (resultMap[key] as? Collection<*>) ?: mutableListOf<Any?>()
                val newChild = source[key] as Collection<*>

                if (!originalChild.containsAll(newChild)) {
                    resultMap[key] = originalChild + newChild
                }
            }

            else -> if (source[key] != null) {
                resultMap[key] = source[key]
            }
        }
    }
    return resultMap
}
