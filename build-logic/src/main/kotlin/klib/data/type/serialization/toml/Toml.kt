package klib.data.type.serialization.toml

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.encodeToString
import kotlinx.serialization.serializer
import net.peanuuutz.tomlkt.Toml
import net.peanuuutz.tomlkt.TomlArray
import net.peanuuutz.tomlkt.TomlElement
import net.peanuuutz.tomlkt.TomlLiteral
import net.peanuuutz.tomlkt.TomlNull
import net.peanuuutz.tomlkt.TomlTable
import net.peanuuutz.tomlkt.toBoolean
import net.peanuuutz.tomlkt.toByteOrNull
import net.peanuuutz.tomlkt.toDoubleOrNull
import net.peanuuutz.tomlkt.toFloatOrNull
import net.peanuuutz.tomlkt.toIntOrNull
import net.peanuuutz.tomlkt.toLocalDate
import net.peanuuutz.tomlkt.toLocalDateTime
import net.peanuuutz.tomlkt.toLocalTime
import net.peanuuutz.tomlkt.toLongOrNull
import net.peanuuutz.tomlkt.toOffsetDateTime
import net.peanuuutz.tomlkt.toShortOrNull
import net.peanuuutz.tomlkt.toUByteOrNull
import net.peanuuutz.tomlkt.toUIntOrNull
import net.peanuuutz.tomlkt.toULongOrNull
import net.peanuuutz.tomlkt.toUShortOrNull

internal fun Toml.encodeAnyToTomlElement(value: Any?): TomlElement =
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

internal fun Toml.decodeAnyFromTomlElement(element: TomlElement): Any? =
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

public fun Toml.decodeAnyFromString(value: String): Any? =
    decodeAnyFromTomlElement(parseToTomlTable(value))

public fun Any?.encodeToml(format: Toml = Toml.Default): String = format.encodeAnyToString(this)

public fun String.decodeToml(format: Toml = Toml.Default): Any? = format.decodeAnyFromString(this)
