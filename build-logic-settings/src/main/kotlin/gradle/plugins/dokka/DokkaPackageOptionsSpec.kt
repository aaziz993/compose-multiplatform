package gradle.plugins.dokka

import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.jetbrains.dokka.gradle.engine.parameters.DokkaPackageOptionsSpec
import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier

/**
 * Configuration builder that allows setting some options for specific packages
 * matched by [matchingRegex].
 *
 * Example in Gradle Kotlin DSL:
 *
 * ```kotlin
 * tasks.dokkaHtml {
 *     dokkaSourceSets.configureEach {
 *         // create a new perPackageOption
 *         perPackageOption {
 *             matchingRegex.set(".*internal.*")
 *             suppress.set(true)
 *         }
 *     }
 * }
 * ```
 */
@Serializable
internal data class DokkaPackageOptionsSpec(
    /**
     * Regular expression that is used to match the package.
     *
     * Default is any string: `.*`.
     */
    val matchingRegex: String? = null,
    /**
     * Whether this package should be skipped when generating documentation.
     *
     * Default is `false`.
     */
    val suppress: Boolean? = null,
    /**
     * Set of visibility modifiers that should be documented.
     *
     * This can be used if you want to document protected/internal/private declarations within a
     * specific package, as well as if you want to exclude public declarations and only document internal API.
     *
     * Can be configured for a whole source set, see [DokkaSourceSetSpec.documentedVisibilities].
     *
     * Default is [VisibilityModifier.Public].
     */
    val documentedVisibilities: Set<VisibilityModifier>? = null,
    /**
     * Whether to document declarations annotated with [Deprecated].
     *
     * Can be overridden on source set level by setting [DokkaSourceSetSpec.skipDeprecated].
     *
     * Default is `false`.
     */
    val skipDeprecated: Boolean? = null,

    /**
     * Whether to emit warnings about visible undocumented declarations, that is declarations from
     * this package and without KDocs, after they have been filtered by [documentedVisibilities].
     *
     *
     * Can be overridden on source set level by setting [DokkaSourceSetSpec.reportUndocumented].
     *
     * Default is `false`.
     */
    val reportUndocumented: Boolean? = null,
) {

    fun applyTo(recipient: DokkaPackageOptionsSpec) {
        recipient.matchingRegex tryAssign matchingRegex
        recipient.suppress tryAssign suppress
        recipient.documentedVisibilities tryAssign documentedVisibilities
        recipient.skipDeprecated tryAssign skipDeprecated
        recipient.reportUndocumented tryAssign reportUndocumented
    }
}
