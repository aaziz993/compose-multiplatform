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
     * Add the [YAML object][YamlMap] produced by the [builderAction] function to a resulting YAML object using the given [key].
     *
     * Returns the previous value associated with [key], or `null` if the key was not present.
     */
    public fun putYamlMap(key: String, builderAction: YamlMapBuilder.() -> Unit): YamlNode? =
        put(key.asKeyScalar, buildYamlMap(valuePath, builderAction))

    /**
     * Add the [YAML array][YamlList] produced by the [builderAction] function to a resulting YAML object using the given [key].
     *
     * Returns the previous value associated with [key], or `null` if the key was not present.
     */
    public fun putYamlList(key: String, builderAction: YamlListBuilder.() -> Unit): YamlNode? =
        put(key.asKeyScalar, buildYamlList(valuePath, builderAction))

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
        put(
            key.asKeyScalar,
            value?.let { value -> YamlScalar(value, valuePath) } ?: YamlNull(valuePath))

    /**
     * Add `null` to a resulting YAML object using the given [key].
     *
     * Returns the previous value associated with [key], or `null` if the key was not present.
     */
    @ExperimentalSerializationApi
    @Suppress("UNUSED_PARAMETER") // allows to call `put("key", null)`
    public fun put(key: String, value: Nothing?): YamlNode? =
        put(key.asKeyScalar, YamlNull(valuePath))

    private val String.asKeyScalar
        get() = YamlScalar(this, YamlPath(path.segments + YamlPathSegment.MapElementKey(this, Location(0, 0))))

    private val valuePath
        get() = YamlPath(path.segments + YamlPathSegment.MapElementValue(Location(0, 0)))

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

