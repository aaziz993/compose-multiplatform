package klib.data.type.serialization.yaml

import com.charleskorn.kaml.Location
import com.charleskorn.kaml.YamlList
import com.charleskorn.kaml.YamlMap
import com.charleskorn.kaml.YamlNode
import com.charleskorn.kaml.YamlNull
import com.charleskorn.kaml.YamlPath
import com.charleskorn.kaml.YamlPathSegment
import com.charleskorn.kaml.YamlScalar
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * DSL builder for a [YamlMap]. To create an instance of builder, use [buildYamlMap] build function.
 */
public class YamlMapBuilder @PublishedApi internal constructor(val path: YamlPath) {

    private val content: MutableMap<YamlScalar, YamlNode> = linkedMapOf()

    /**
     * Add the given YAML [element] to a resulting YAML object using the given [key].
     *
     * Returns the previous value associated with [key], or `null` if the key was not present.
     */
    public fun put(key: YamlScalar, node: YamlNode): YamlNode? = content.put(key, node)

    /**
     * Adds the given YAML [map] to a resulting YAML map.
     *
     * @return `true` if the list was changed as the result of the operation.
     */
    public fun putAll(map: Map<YamlScalar, YamlNode>): Unit = content.putAll(map)

    /**
     * Add the [YAML object][YamlMap] produced by the [builderAction] function to a resulting YAML object using the given [key].
     *
     * Returns the previous value associated with [key], or `null` if the key was not present.
     */
    public fun putYamlMap(key: String, builderAction: YamlMapBuilder.() -> Unit): YamlNode? =
        key.asKeyScalar().let { keyScalar ->
            put(keyScalar, buildYamlMap(keyScalar.valuePath, builderAction))
        }

    /**
     * Add the [YAML array][YamlList] produced by the [builderAction] function to a resulting YAML object using the given [key].
     *
     * Returns the previous value associated with [key], or `null` if the key was not present.
     */
    public fun putYamlList(key: String, builderAction: YamlListBuilder.() -> Unit): YamlNode? =
        key.asKeyScalar().let { keyScalar ->
            put(keyScalar, buildYamlList(keyScalar.valuePath, builderAction))
        }

    /**
     * Add the given boolean [value] to a resulting YAML object using the given [key].
     *
     * Returns the previous value associated with [key], or `null` if the key was not present.
     */
    public fun put(key: String, value: Boolean?): YamlNode? =
        put(key, value?.toString())

    /**
     * Add the given numeric [value] to a resulting YAML object using the given [key].
     *
     * Returns the previous value associated with [key], or `null` if the key was not present.
     */
    public fun put(key: String, value: Number?): YamlNode? =
        put(key, value?.toString())

    /**
     * Add the given string [value] to a resulting YAML object using the given [key].
     *
     * Returns the previous value associated with [key], or `null` if the key was not present.
     */
    public fun put(key: String, value: String?): YamlNode? =
        key.asKeyScalar().let { keyScalar ->
            put(
                keyScalar,
                value?.let { value -> YamlScalar(value, keyScalar.valuePath) }
                    ?: YamlNull(keyScalar.valuePath)
            )
        }

    /**
     * Add `null` to a resulting YAML object using the given [key].
     *
     * Returns the previous value associated with [key], or `null` if the key was not present.
     */
    @ExperimentalSerializationApi
    @Suppress("UNUSED_PARAMETER") // allows to call `put("key", null)`
    public fun putNull(key: String): YamlNode? = put(key, null as String?)

    public fun String.asKeyScalar(index: Int = content.size) = YamlScalar(
        this, YamlPath(
            path.segments + YamlPathSegment.MapElementKey(
                this,
                path.segments.last().location.let { location ->
                    Location(
                        location.line + index,
                        location.column
                    )
                }
            )
        )
    )

    public val YamlScalar.valuePath
        get() = YamlPath(
            path.segments + YamlPathSegment.MapElementValue(
                path.segments.last().location.let { (line, column) ->
                    Location(line, column + content.length + 3)
                }
            ))

    @PublishedApi
    internal fun build(): YamlMap = YamlMap(content, path)
}


/**
 * Builds [YamlMap] with the given [builderAction] builder.
 * Example of usage:
 * ```
 * val yaml = buildYamlMap {
 *         put("booleanKey", true)
 *         putYamlList("arrayKey") {
 *             for (i in 1..10) add(i)
 *         }
 *
 *         putYamlMap("objectKey") {
 *             put("stringKey", "stringValue")
 *         }
 *     }
 * ```
 */
@OptIn(ExperimentalContracts::class)
public inline fun buildYamlMap(
    path: YamlPath = YamlPath(YamlPathSegment.Root),
    builderAction: YamlMapBuilder.() -> Unit
): YamlMap {
    contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
    val builder = YamlMapBuilder(path)
    builder.builderAction()
    return builder.build()
}

