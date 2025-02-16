package plugin.project.gradle.kover.model

import kotlinx.serialization.Serializable

/**
 * Limit the classes that will be included in the reports.
 * These settings do not affect the instrumentation of classes.
 *
 * The settings specified here affect all reports in any projects that use the current project depending on.
 * However, these settings should be used to regulate classes specific only to the project in which this setting is specified.
 *
 * Example:
 * ```
 *  sources {
 *     // exclude classes compiled by Java compiler from all reports
 *     excludeJava = true
 *
 *     // exclude source classes of specified source sets from all reports
 *     excludedSourceSets.addAll(excludedSourceSet)
 * ```
 */
@Serializable
internal data class KoverVariantSources(
    /**
     * Exclude classes compiled by Java compiler from all reports
     */
    val excludeJava: Boolean? = null,
    /**
     * Exclude source classes of specified source sets from all reports
     */
    val excludedSourceSets: Set<String>? = null
)
