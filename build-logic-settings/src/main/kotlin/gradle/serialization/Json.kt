package gradle.serialization

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
public fun Json.encodeAnyToJsonElement(value: Any?): JsonElement = when (value) {
    null -> JsonNull

    is JsonElement -> value

    is List<*> -> JsonArray(value.map(::encodeAnyToJsonElement))

    is Map<*, *> -> JsonObject(
        value.entries.associate { it.key.toString() to encodeAnyToJsonElement(it.value) },
    )

    else -> encodeToJsonElement(value::class.serializer() as KSerializer<Any>, value)
}

public fun Json.decodeAnyFromJsonElement(element: JsonElement): Any? = with(element) {
    when (this) {
        JsonNull -> null

        is JsonPrimitive -> if (isString) {
            content
        }
        else {
            (booleanOrNull ?: longOrNull ?: doubleOrNull)!!
        }

        is JsonArray -> map(::decodeAnyFromJsonElement)

        is JsonObject -> mapValues { (_, v) -> decodeAnyFromJsonElement(v) }
    }
}

public fun <T> Json.encodeToAny(serializer: SerializationStrategy<T>, value: T): Any? = decodeAnyFromJsonElement(encodeToJsonElement(serializer, value))

public inline fun <reified T> Json.encodeToAny(value: T): Any? = encodeToAny(serializersModule.serializer(), value)

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

public fun Json.decodeListFromString(value: String): List<Any?> =
    decodeAnyFromJsonElement(decodeFromString(JsonArray::class.serializer(), value)) as List<Any?>

public val String.jsonList: List<Any?>
    get() = Json.Default.decodeListFromString(this)

@Suppress("UNCHECKED_CAST")
public fun Json.decodeMapFromString(value: String): Map<String, Any?> =
    decodeAnyFromJsonElement(decodeFromString(JsonObject::class.serializer(), value)) as Map<String, Any?>

public val String.jsonMap: Map<String, Any?>
    get() = Json.Default.decodeMapFromString(this)

// Make deep copy of an object
@Suppress("UNCHECKED_CAST")
public fun <T : Any> Json.decodeFrom(serializer: KSerializer<T>, value: T, block: (Map<String, Any?>) -> Map<String, Any?> = { it }): T =
    decodeFromAny(serializer, block(encodeToAny(serializer, value) as Map<String, Any?>))

public inline fun <reified T : Any> Json.decodeFrom(value: T, noinline block: (Map<String, Any?>) -> Map<String, Any?> = { it }): T =
    decodeFrom(serializersModule.serializer(), value, block)
