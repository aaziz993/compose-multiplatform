@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.yaml

import com.charleskorn.kaml.*
import klib.data.type.asList
import klib.data.type.asMap
import klib.data.type.serialization.copyFrom
import kotlinx.serialization.*

public fun <T> Yaml.encodeToYamlNode(serializer: SerializationStrategy<T>, value: T): YamlNode =
    parseToYamlNode(encodeToString(serializer, value))

@Suppress("UNCHECKED_CAST")
public fun Yaml.encodeAnyToYamlNode(value: Any?): YamlNode =
    encodeAnyToYamlNodeDeepRecursiveFunction(Triple(this, YamlPath(YamlPathSegment.Root), value))

private val encodeAnyToYamlNodeDeepRecursiveFunction =
    DeepRecursiveFunction<Triple<Yaml, YamlPath, Any?>, YamlNode> { (yaml, path, value) ->
        when (value) {
            null -> YamlNull(path)

            is YamlNode -> value

            is List<*> -> buildYamlList(path) {
                addAll(value.mapIndexed { index, element ->
                    callRecursive(Triple(yaml, index.asIndexPath, element))
                })
            }

            is Map<*, *> -> buildYamlMap(path) {
                putAll(value.entries.withIndex().associate { (index, entry) ->
                    entry.key.toString().asKeyScalar(index).let { keyScalar ->
                        keyScalar to callRecursive(
                            Triple(
                                yaml,
                                keyScalar.valuePath,
                                entry.value
                            )
                        )
                    }
                })
            }

            else -> yaml.encodeToYamlNode(value::class.serializer() as KSerializer<Any>, value)
        }
    }

public fun Yaml.decodeAnyFromYamlNode(node: YamlNode): Any? =
    decodeAnyFromYamlNodeDeepRecursiveFunction(this to node)

private val decodeAnyFromYamlNodeDeepRecursiveFunction =
    DeepRecursiveFunction<Pair<Yaml, YamlNode>, Any?> { (yaml, node) ->
        when (node) {
            is YamlNull -> null

            is YamlTaggedNode -> mapOf(node.tag to callRecursive(yaml to node.innerNode))

            is YamlScalar -> node.toBooleanOrNull() ?: node.toLongOrNull() ?: node.toDoubleOrNull()
            ?: node.toCharOrNull() ?: node.content

            is YamlList -> node.items.map { callRecursive(yaml to it) }

            is YamlMap -> node.entries.entries.associate { (key, value) ->
                callRecursive(yaml to key) to callRecursive(yaml to value)
            }
        }
    }

public fun <T> Yaml.encodeToAny(serializer: SerializationStrategy<T>, value: T): Any? =
    decodeAnyFromYamlNode(encodeToYamlNode(serializer, value))

public inline fun <reified T> Yaml.encodeToAny(value: T): Any? = encodeToAny(serializersModule.serializer(), value)

public fun <T> Yaml.decodeFromAny(deserializer: DeserializationStrategy<T>, value: Any?): T =
    decodeFromYamlNode(deserializer, encodeAnyToYamlNode(value))

public inline fun <reified T> Yaml.decodeFromAny(value: Any?): T = decodeFromAny(serializersModule.serializer(), value)

public fun Yaml.encodeAnyToString(value: Any?): String = encodeToString(encodeAnyToYamlNode(value))

public fun Yaml.decodeAnyFromString(value: String): Any? = decodeAnyFromYamlNode(parseToYamlNode(value))

public val String.yamlAny: Any?
    get() = Yaml.default.decodeAnyFromString(this)

public val String.yamlList: List<Any?>
    get() = Yaml.default.decodeAnyFromString(this).asList

public val String.yamlMap: Map<String, Any?>
    get() = Yaml.default.decodeAnyFromString(this).asMap