package klib.data.type.serialization.yaml

import com.charleskorn.kaml.*
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlinx.serialization.json.JsonObject

/**
 * DSL builder for a [YamlList]. To create an instance of builder, use [buildYamlList] build function.
 */
public class YamlListBuilder @PublishedApi internal constructor(
    public val path: YamlPath,
    public val sequenceBlockIndent: Int,
    public val encodingIndentationSize: Int,
) : ArrayList<YamlNode>() {

    /**
     * Adds the given boolean [value] to a resulting YAML array.
     *
     * Always returns `true` similarly to [ArrayList] specification.
     */
    public fun add(value: Boolean?): Boolean = add(value?.toString())

    /**
     * Adds the given numeric [value] to a resulting YAML array.
     *
     * Always returns `true` similarly to [ArrayList] specification.
     */
    public fun add(value: Number?): Boolean = add(value?.toString())

    /**
     * Adds the given string [value] to a resulting YAML array.
     *
     * Always returns `true` similarly to [ArrayList] specification.
     */
    public fun add(value: String?): Boolean =
        add(value?.let { value -> YamlScalar(value, withListEntry()) } ?: YamlNull(withListEntry()))

    /**
     * Adds `null` to a resulting YAML array.
     *
     * Always returns `true` similarly to [ArrayList] specification.
     */

    @Suppress("UNUSED_PARAMETER") // allows to call `add(null)`
    public fun addNull(): Boolean = add(null as String?)

    /**
     * Adds the [YAML object][JsonObject] produced by the [builderAction] function to a resulting YAML array.
     *
     * Always returns `true` similarly to [ArrayList] specification.
     */
    public fun addYamlMap(builderAction: YamlMapBuilder.() -> Unit): Boolean =
        add(
            buildYamlMap(
                withListEntry(),
                encodingIndentationSize,
                sequenceBlockIndent,
                builderAction
            )
        )

    /**
     * Adds the [YAML array][YamlList] produced by the [builderAction] function to a resulting YAML array.
     *
     * Always returns `true` similarly to [ArrayList] specification.
     */
    public fun addYamlList(builderAction: YamlListBuilder.() -> Unit): Boolean =
        add(
            buildYamlList(
                withListEntry(),
                sequenceBlockIndent,
                encodingIndentationSize,
                builderAction
            )
        )

    /**
     * Adds the given string [values] to a resulting YAML array.
     *
     * @return `true` if the list was changed as the result of the operation.
     */
    @JvmName("addAllStrings")
    public fun addAll(values: Collection<String?>): Boolean =
        addAll(
            values.mapIndexed { index, value ->
                val indexPath = withListEntry(index)
                value?.let { value -> YamlScalar(value, indexPath) } ?: YamlNull(indexPath)
            },
        )

    /**
     * Adds the given boolean [values] to a resulting YAML array.
     *
     * @return `true` if the list was changed as the result of the operation.
     */
    @JvmName("addAllBooleans")
    public fun addAll(values: Collection<Boolean?>): Boolean =
        addAll(values.map { value -> value?.toString() })

    /**
     * Adds the given numeric [values] to a resulting YAML array.
     *
     * @return `true` if the list was changed as the result of the operation.
     */
    @JvmName("addAllNumbers")
    public fun addAll(values: Collection<Number?>): Boolean =
        addAll(values.map { value -> value?.toString() })

    public fun withListEntry(index: Int = size, line: Int = endLine + 1): YamlPath =
        path.withListEntry(
            index,
            Location(line, path.endLocation.column + Yaml.ENCODING_INDENTATION_SIZE),
        )

    private val endLine
        get() = lastOrNull()?.location?.line ?: (path.endLocation.line - 1)

    @PublishedApi
    internal fun build(): YamlList = YamlList(this, path)
}

/**
 * Builds [YamlList] with the given [builderAction] builder.
 * Example of usage:
 * ```
 * val yaml = buildYamlList {
 *              add(true)
 *              addYamlList {
 *                  for (i in 1..10) add(i)
 *              }
 *              addYamlMap {
 *                  put("stringKey", "stringValue")
 *              }
 *          }
 * ```
 */
public inline fun buildYamlList(
    path: YamlPath = YamlPath(),
    sequenceBlockIndent: Int = Yaml.SEQUENCE_BLOCK_INDENT,
    encodingIndentationSize: Int = Yaml.ENCODING_INDENTATION_SIZE,
    builderAction: YamlListBuilder.() -> Unit
): YamlList {
    contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
    val builder = YamlListBuilder(path, sequenceBlockIndent, encodingIndentationSize)
    builder.builderAction()
    return builder.build()
}
