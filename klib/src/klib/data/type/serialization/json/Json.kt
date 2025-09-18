package klib.data.type.serialization.json

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.*
import kotlinx.serialization.serializer
import kotlin.invoke

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
