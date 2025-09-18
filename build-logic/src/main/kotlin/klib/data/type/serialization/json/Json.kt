package klib.data.type.serialization.json

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
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.longOrNull
import kotlinx.serialization.serializer

internal fun Json.encodeAnyToJsonElement(value: Any?): JsonElement =
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

internal fun Json.decodeAnyFromJsonElement(element: JsonElement): Any? =
    decodeAnyFromJsonElementDeepRecursiveFunction(this to element)

private val decodeAnyFromJsonElementDeepRecursiveFunction =
    DeepRecursiveFunction<Pair<Json, JsonElement>, Any?> { (json, element) ->
        when (element) {
            JsonNull -> null

            is JsonPrimitive -> if (element.isString) element.content
            else (element.booleanOrNull
                ?: element.intOrNull
                ?: element.longOrNull
                ?: element.floatOrNull
                ?: element.doubleOrNull)!!

            is JsonArray -> element.map { callRecursive(json to it) }

            is JsonObject -> element.mapValues { callRecursive(json to it.value) }
        }
    }

public fun <T> Json.encodeToAny(serializer: SerializationStrategy<T>, value: T): Any? =
    decodeAnyFromJsonElement(encodeToJsonElement(serializer, value))

public inline fun <reified T> Json.encodeToAny(value: T): Any? = encodeToAny(serializersModule.serializer(), value)

public fun <T> Json.decodeFromAny(deserializer: DeserializationStrategy<T>, value: Any?): T =
    decodeFromJsonElement(deserializer, encodeAnyToJsonElement(value))

public inline fun <reified T> Json.decodeFromAny(value: Any?): T = decodeFromAny(serializersModule.serializer(), value)

public fun Json.encodeAnyToString(value: Any?): String = encodeToString(encodeAnyToJsonElement(value))

public fun Json.decodeAnyFromString(value: String): Any? = decodeAnyFromJsonElement(parseToJsonElement(value))

public fun Any?.encodeJson(format: Json = Json.Default): String = format.encodeAnyToString(this)

public fun String.decodeJson(format: Json = Json.Default): Any? = format.decodeAnyFromString(this)
