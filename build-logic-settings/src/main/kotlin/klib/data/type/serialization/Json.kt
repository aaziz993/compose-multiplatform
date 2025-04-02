package klib.data.type.serialization

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.longOrNull
import kotlinx.serialization.serializer

@Suppress("UNCHECKED_CAST")
public fun Json.encodeAnyToJsonElement(value: Any?): JsonElement =
    encodeAnyToJsonElementDeepRecursiveFunction(this to value)

private val encodeAnyToJsonElementDeepRecursiveFunction =
    DeepRecursiveFunction<Pair<Json, Any?>, JsonElement> { (json, value) ->
        when (value) {
            null -> JsonNull

            is JsonElement -> value

            is List<*> -> JsonArray(value.map { callRecursive(json to it) })

            is Map<*, *> -> JsonObject(
                value.entries.associate { it.key.toString() to callRecursive(json to it.value) },
            )

            else -> json.encodeToJsonElement(value::class.serializer() as KSerializer<Any>, value)
        }
    }

public fun Json.decodeAnyFromJsonElement(element: JsonElement): Any? =
    decodeAnyFromJsonElementDeepRecursiveFunction(this to element)

private val decodeAnyFromJsonElementDeepRecursiveFunction =
    DeepRecursiveFunction<Pair<Json, JsonElement>, Any?> { (json, element) ->
        when (element) {
            JsonNull -> null

            is JsonPrimitive -> if (element.isString) element.content
            else (element.booleanOrNull ?: element.longOrNull ?: element.doubleOrNull)!!

            is JsonArray -> element.map { callRecursive(json to it) }

            is JsonObject -> element.mapValues { callRecursive(json to it.value) }
        }
    }

@Suppress("UNCHECKED_CAST")
public fun <E> Json.decodeListFromJsonElement(element: JsonElement): List<Any?> =
    decodeAnyFromJsonElement(element) as List<E>

@Suppress("UNCHECKED_CAST")
public fun <V> Json.decodeMapFromJsonElement(element: JsonElement): Map<String, V> =
    decodeAnyFromJsonElement(element) as Map<String, V>

public fun <T> Json.encodeToAny(serializer: SerializationStrategy<T>, value: T): Any? = decodeAnyFromJsonElement(encodeToJsonElement(serializer, value))

public inline fun <reified T> Json.encodeToAny(value: T): Any? = encodeToAny(serializersModule.serializer(), value)

@Suppress("UNCHECKED_CAST")
public fun <T, E> Json.encodeToList(serializer: SerializationStrategy<T>, value: T): List<Any?> =
    encodeToAny(serializer, value) as List<E>

@Suppress("UNCHECKED_CAST")
public inline fun <reified T, E> Json.encodeToList(value: T): List<E> =
    encodeToAny(value) as List<E>

@Suppress("UNCHECKED_CAST")
public fun <T, V> Json.encodeToMap(serializer: SerializationStrategy<T>, value: T): Map<String, V> =
    encodeToAny(serializer, value) as Map<String, V>

@Suppress("UNCHECKED_CAST")
public inline fun <reified T, V> Json.encodeToMap(value: T): Map<String, V> =
    encodeToAny(value) as Map<String, V>

public fun <T> Json.decodeFromAny(deserializer: DeserializationStrategy<T>, value: Any?): T =
    decodeFromJsonElement(deserializer, encodeAnyToJsonElement(value))

public inline fun <reified T> Json.decodeFromAny(value: Any?): T = decodeFromAny(serializersModule.serializer(), value)

public fun Json.encodeAnyToString(value: Any?): String = encodeToString(encodeAnyToJsonElement(value))

public fun Json.decodeAnyFromString(deserializer: DeserializationStrategy<JsonElement>, value: String): Any? =
    decodeAnyFromJsonElement(decodeFromString(deserializer, value))

public fun String.jsonAny(deserializer: DeserializationStrategy<JsonElement>): Any? =
    Json.Default.decodeAnyFromString(deserializer, this)

public fun Json.decodeAnyFromString(value: String): Any? = decodeAnyFromString(JsonElement::class.serializer(), value)

public val String.jsonAny: Any?
    get() = Json.Default.decodeAnyFromString(this)

@Suppress("UNCHECKED_CAST")
public fun <E> Json.decodeListFromString(value: String): List<E> =
    decodeAnyFromJsonElement(decodeFromString(JsonArray::class.serializer(), value)) as List<E>

public fun <E> String.jsonList(): List<E> = Json.Default.decodeListFromString(this)

@Suppress("UNCHECKED_CAST")
public fun <V> Json.decodeMapFromString(value: String): Map<String, V> =
    decodeAnyFromJsonElement(decodeFromString(JsonObject::class.serializer(), value)) as Map<String, V>

public fun <V> String.jsonMap(): Map<String, V> = Json.Default.decodeMapFromString(this)

// Make deep copy of an object
@Suppress("UNCHECKED_CAST")
public fun <T : Any> Json.decodeFrom(serializer: KSerializer<T>, value: T, block: (Map<String, Any?>) -> Map<String, Any?> = { it }): T =
    decodeFromAny(serializer, block(encodeToAny(serializer, value) as Map<String, Any?>))

public inline fun <reified T : Any> Json.decodeFrom(value: T, noinline block: (Map<String, Any?>) -> Map<String, Any?> = { it }): T =
    decodeFrom(serializersModule.serializer(), value, block)
