package gradle.plugins.kover.currentproject

import kotlinx.serialization.Serializable

/**
 * Common config for Kover report variants.
 */
internal interface KoverVariantConfig<T : kotlinx.kover.gradle.plugin.dsl.KoverVariantConfig> {

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
    val sources: KoverVariantSources?

    fun applyTo(receiver: T) {
        sources?.applyTo(receiver.sources)
    }
}

@Serializable
internal data class KoverVariantConfigImpl(
    override val sources: KoverVariantSources? = null,
) : KoverVariantConfig<kotlinx.kover.gradle.plugin.dsl.KoverVariantConfig>
