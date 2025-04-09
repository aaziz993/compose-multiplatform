package klib.data.type.serialization

import klib.data.type.asMap
import kotlinx.serialization.*
import net.peanuuutz.tomlkt.*
import kotlin.invoke
import kotlin.ranges.contains

@Suppress("UNCHECKED_CAST")
public fun Toml.encodeAnyToTomlElement(value: Any?): TomlElement =
    encodeAnyToTomlElementDeepRecursiveFunction(this to value)

private val encodeAnyToTomlElementDeepRecursiveFunction =
    DeepRecursiveFunction<Pair<Toml, Any?>, TomlElement> { (toml, value) ->
        when (value) {
            null -> TomlNull

            is TomlElement -> value

            is List<*> -> TomlArray(value.map { callRecursive(toml to it) })

            is Map<*, *> -> TomlTable(
                value.entries.associate { it.key.toString() to callRecursive(toml to it.value) },
            )

            else -> toml.encodeToTomlElement(value::class.serializer() as KSerializer<Any>, value)
        }
    }

public fun Toml.decodeAnyFromTomlElement(element: TomlElement): Any? =
    decodeAnyFromTomlElementDeepRecursiveFunction(this to element)

private val decodeAnyFromTomlElementDeepRecursiveFunction =
    DeepRecursiveFunction<Pair<Toml, TomlElement>, Any?> { (json, element) ->
        when (element) {
            TomlNull -> null

            is TomlLiteral -> when (element.type) {
                TomlLiteral.Type.Boolean -> element.toBoolean()
                TomlLiteral.Type.Integer ->
                    (element.toByteOrNull()
                        ?: element.toShortOrNull()
                        ?: element.toIntOrNull()
                        ?: element.toLongOrNull()
                        ?: element.toUByteOrNull()
                        ?: element.toUShortOrNull()
                        ?: element.toUIntOrNull()
                        ?: element.toULongOrNull())!!

                TomlLiteral.Type.Float -> (element.toFloatOrNull() ?: element.toDoubleOrNull())!!
                TomlLiteral.Type.String -> element.content
                TomlLiteral.Type.LocalDateTime -> element.toLocalDateTime()
                TomlLiteral.Type.OffsetDateTime -> element.toOffsetDateTime()
                TomlLiteral.Type.LocalDate -> element.toLocalDate()
                TomlLiteral.Type.LocalTime -> element.toLocalTime()
            }

            is TomlArray -> element.map { callRecursive(json to it) }

            is TomlTable -> element.mapValues { callRecursive(json to it.value) }
        }
    }

public fun <T> Toml.encodeToAny(serializer: SerializationStrategy<T>, value: T): Any? =
    decodeAnyFromTomlElement(encodeToTomlElement(serializer, value))

public inline fun <reified T> Toml.encodeToAny(value: T): Any? = encodeToAny(serializersModule.serializer(), value)

public fun <T> Toml.decodeFromAny(deserializer: DeserializationStrategy<T>, value: Any?): T =
    decodeFromTomlElement(deserializer, encodeAnyToTomlElement(value))

public inline fun <reified T> Toml.decodeFromAny(value: Any?): T = decodeFromAny(serializersModule.serializer(), value)

public fun Toml.encodeAnyToString(value: Any?): String = encodeToString(encodeAnyToTomlElement(value))

public fun Toml.decodeMapFromString(value: String): Map<String, Any?> =
    decodeAnyFromTomlElement(parseToTomlTable(value)).asMap

public val String.tomlMap: Map<String, Any?>
    get() = Toml.Default.decodeMapFromString(this)
