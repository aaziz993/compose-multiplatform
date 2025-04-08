package klib.data.type.serialization.yaml

import com.charleskorn.kaml.*
import kotlinx.serialization.json.JsonObject
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * DSL builder for a [YamlList]. To create an instance of builder, use [buildYamlList] build function.
 */
public class YamlListBuilder @PublishedApi internal constructor(val path: YamlPath) {

    private val content: MutableList<YamlNode> = mutableListOf()

    /**
     * Adds the given YAML [node] to a resulting YAML array.
     *
     * Always returns `true` similarly to [ArrayList] specification.
     */
    public fun add(node: YamlNode): Boolean {
        content += node
        return true
    }

    /**
     * Adds the given YAML [nodes] to a resulting YAML array.
     *
     * @return `true` if the list was changed as the result of the operation.
     */

    public fun addAll(nodes: Collection<YamlNode>): Boolean = content.addAll(nodes)

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
        add(value?.let { value -> YamlScalar(value, lastIndexPath) } ?: YamlNull(lastIndexPath))

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
        add(buildYamlMap(lastIndexPath, builderAction))

    /**
     * Adds the [YAML array][YamlList] produced by the [builderAction] function to a resulting YAML array.
     *
     * Always returns `true` similarly to [ArrayList] specification.
     */
    public fun addYamlList(builderAction: YamlListBuilder.() -> Unit): Boolean =
        add(buildYamlList(lastIndexPath, builderAction))

    /**
     * Adds the given string [values] to a resulting YAML array.
     *
     * @return `true` if the list was changed as the result of the operation.
     */
    @JvmName("addAllStrings")
    public fun addAll(values: Collection<String?>): Boolean =
        addAll(values.mapIndexed { index, value ->
            val indexPath = (content.size + index).asIndexPath
            value?.let { value -> YamlScalar(value, indexPath) } ?: YamlNull(indexPath)
        })

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

    public val Int.asIndexPath
        get() = YamlPath(
            path.segments + YamlPathSegment.ListEntry(
                this,
                path.segments.last().location.let { location ->
                    Location(location.line + this, location.column + 2)
                }
            )
        )

    public val lastIndexPath
        get() = content.size.asIndexPath

    @PublishedApi
    internal fun build(): YamlList = YamlList(content, path)
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
@OptIn(ExperimentalContracts::class)
public inline fun buildYamlList(path: YamlPath = YamlPath(), builderAction: YamlListBuilder.() -> Unit): YamlList {
    contract { callsInPlace(builderAction, InvocationKind.EXACTLY_ONCE) }
    val builder = YamlListBuilder(path)
    builder.builderAction()
    return builder.build()
}