@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.yaml

import com.charleskorn.kaml.*
import klib.data.type.exceptionToNull
import kotlinx.serialization.*

public val Yaml.Companion.ENCODING_INDENTATION_SIZE: Int
    get() = 2

public val Yaml.Companion.SEQUENCE_BLOCK_INDENT: Int
    get() = 2

public fun <T> Yaml.encodeToYamlNode(serializer: SerializationStrategy<T>, value: T): YamlNode =
    parseToYamlNode(encodeToString(serializer, value))

public inline fun <reified T> Yaml.encodeToYamlNode(value: T): YamlNode =
    encodeToYamlNode(serializersModule.serializer<T>(), value)

internal fun Yaml.encodeAnyToYamlNode(value: Any?): YamlNode =
    encodeAnyToYamlNodeDeepRecursiveFunction(Triple(this, YamlPath(YamlPathSegment.Root), value))

private val encodeAnyToYamlNodeDeepRecursiveFunction =
    DeepRecursiveFunction<Triple<Yaml, YamlPath, Any?>, YamlNode> { (yaml, path, value) ->
        when (value) {
            null -> YamlNull(path)

            is YamlNode -> value

            is List<*> -> buildYamlList(path) {
                var line = path.endLocation.line - 1
                addAll(
                    value.mapIndexed { index, element ->
                        callRecursive(Triple(yaml, withListEntry(size + index, line + 1), element)).also { node ->
                            line = node.location.line
                        }
                    },
                )
            }

            is Map<*, *> -> buildYamlMap(path) {
                var line = path.endLocation.line - 1
                putAll(
                    value.entries.associate { (key, value) ->
                        key.toString().asKeyScalar(line + 1).let { keyScalar ->
                            keyScalar to callRecursive(
                                Triple(
                                    yaml,
                                    keyScalar.path.withMapElementValue(key.toString(), value),
                                    value,
                                ),
                            ).also { node ->
                                line = node.location.line
                            }
                        }
                    },
                )
            }

            else -> yaml.encodeToYamlNode(value::class.serializer() as KSerializer<Any>, value).withPath(path)
        }
    }

internal fun Yaml.decodeAnyFromYamlNode(node: YamlNode): Any? =
    decodeAnyFromYamlNodeDeepRecursiveFunction(this to node)

private val decodeAnyFromYamlNodeDeepRecursiveFunction =
    DeepRecursiveFunction<Pair<Yaml, YamlNode>, Any?> { (yaml, node) ->
        when (node) {
            is YamlNull -> null

            is YamlTaggedNode -> mapOf(node.tag to callRecursive(yaml to node.innerNode))

            is YamlScalar -> node.toBooleanOrNull()
                ?: node::toByte.exceptionToNull()
                ?: node::toShort.exceptionToNull()
                ?: node::toInt.exceptionToNull()
                ?: node.toLongOrNull()
                ?: node::toFloat.exceptionToNull()
                ?: node.toDoubleOrNull()
                ?: node.toCharOrNull()
                ?: node.content

            is YamlList -> node.items.map { callRecursive(yaml to it) }

            is YamlMap -> node.entries.entries.associate { (key, value) ->
                callRecursive(yaml to key) to callRecursive(yaml to value)
            }
        }
    }

public fun <T> Yaml.encodeToAny(serializer: SerializationStrategy<T>, value: T): Any? =
    decodeAnyFromYamlNode(encodeToYamlNode(serializer, value))

public inline fun <reified T> Yaml.encodeToAny(value: T): Any? =
    encodeToAny(serializersModule.serializer(), value)

public fun <T> Yaml.decodeFromAny(deserializer: DeserializationStrategy<T>, value: Any?): T =
    decodeFromYamlNode(deserializer, encodeAnyToYamlNode(value))

public inline fun <reified T> Yaml.decodeFromAny(value: Any?): T =
    decodeFromAny(serializersModule.serializer(), value)

public fun Yaml.encodeAnyToString(value: Any?): String = encodeToString(encodeAnyToYamlNode(value))

public fun Yaml.decodeAnyFromString(value: String): Any? = decodeAnyFromYamlNode(parseToYamlNode(value))

public fun Any?.encodeYaml(format: Yaml = Yaml.default): String = format.encodeAnyToString(this)

public fun String.decodeYaml(format: Yaml = Yaml.default): Any? = format.decodeAnyFromString(this)
