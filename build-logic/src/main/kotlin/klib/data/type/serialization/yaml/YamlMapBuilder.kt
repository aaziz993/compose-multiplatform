package klib.data.type.serialization.yaml

import com.charleskorn.kaml.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * DSL builder for a [YamlMap]. To create an instance of builder, use [buildYamlMap] build function.
 */
public class YamlMapBuilder @PublishedApi internal constructor(
    public val path: YamlPath,
    public val encodingIndentationSize: Int,
    public val sequenceBlockIndent: Int,
) : LinkedHashMap<YamlScalar, YamlNode>() {

    /**
     * Add the [YAML object][YamlMap] produced by the [builderAction] function to a resulting YAML object using the given [key].
     *
     * Returns the previous value associated with [key], or `null` if the key was not present.
     */
    public fun putYamlMap(key: String, builderAction: YamlMapBuilder.() -> Unit): YamlNode? =
        key.asKeyScalar().let { keyScalar ->
            put(
                keyScalar,
                buildYamlMap(
                    keyScalar.path.withMapElementValue(),
                    encodingIndentationSize,
                    sequenceBlockIndent,
                    builderAction
                )
            )
        }

    /**
     * Add the [YAML array][YamlList] produced by the [builderAction] function to a resulting YAML object using the given [key].
     *
     * Returns the previous value associated with [key], or `null` if the key was not present.
     */
    public fun putYamlList(key: String, builderAction: YamlListBuilder.() -> Unit): YamlNode? =
        key.asKeyScalar().let { keyScalar ->
            put(
                keyScalar,
                buildYamlList(
                    keyScalar.path.withMapElementValue(column = 0),
                    sequenceBlockIndent,
                    encodingIndentationSize,
                    builderAction
                )
            )
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
            val valuePath = keyScalar.path.withMapElementValue(0, key.length + 2)
            put(
                keyScalar,
                value?.let { value -> YamlScalar(value, valuePath) } ?: YamlNull(valuePath),
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

    public fun withMapElementKey(
        key: String,
        line: Int = endLine + 1
    ): YamlPath = path.withMapElementKey(
        key,
        path.endLocation.copy(line),
    )

    public fun String.asKeyScalar(line: Int = endLine + 1): YamlScalar =
        YamlScalar(
            this,
            withMapElementKey(this, line),
        )

    private val endLine
        get() = lastEntry()?.value?.location?.line ?: (path.endLocation.line - 1)

    @PublishedApi
    internal fun build(): YamlMap = YamlMap(this, path)
}

public fun YamlPath.withMapElementValue(line: Int = 1, column: Int = Yaml.ENCODING_INDENTATION_SIZE): YamlPath =
    withMapElementValue(Location(endLocation.line + line, endLocation.column + column))

public fun YamlPath.withMapElementValue(key: String, value: Any?): YamlPath =
    if (value is List<*> || value is Map<*, *>) withMapElementValue() else withMapElementValue(0, key.length + 2)

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
    encodingIndentationSize: Int = Yaml.ENCODING_INDENTATION_SIZE,
    sequenceBlockIndent: Int = Yaml.SEQUENCE_BLOCK_INDENT,
    builderAction: YamlMapBuilder.() -> Unit
): YamlMap {
    contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
    val builder = YamlMapBuilder(path, encodingIndentationSize, sequenceBlockIndent)
    builder.builderAction()
    return builder.build()
}

