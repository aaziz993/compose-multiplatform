package gradle.plugins.dokka

import gradle.api.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.dokka.gradle.engine.parameters.DokkaGeneratorParametersSpec

/**
 * Parameters used to run [org.jetbrains.dokka.DokkaGenerator] to produce either
 * a Dokka Publication or a Dokka Module.
 *
 * This class is a bridge between configurable options and [org.jetbrains.dokka.DokkaConfiguration],
 * and should only be used internally.
 *
 * Dokka users should use [org.jetbrains.dokka.gradle.DokkaExtension] to configure
 * [DokkaSourceSetSpec] and [org.jetbrains.dokka.gradle.formats.DokkaPublication].
 */
@Serializable
internal data class DokkaGeneratorParametersSpec(
    /** @see org.jetbrains.dokka.gradle.formats.DokkaPublication.moduleName */
    val moduleName: String? = null,
    /** @see org.jetbrains.dokka.gradle.formats.DokkaPublication.moduleVersion */
    val moduleVersion: String? = null,
    /** @see org.jetbrains.dokka.gradle.formats.DokkaPublication.failOnWarning */
    val failOnWarning: Boolean? = null,
    /** @see org.jetbrains.dokka.gradle.formats.DokkaPublication.offlineMode */
    val offlineMode: Boolean? = null,
    /** @see org.jetbrains.dokka.gradle.formats.DokkaPublication.suppressObviousFunctions */
    val suppressObviousFunctions: Boolean? = null,
    /** @see org.jetbrains.dokka.gradle.formats.DokkaPublication.suppressInheritedMembers */
    val suppressInheritedMembers: Boolean? = null,
    /** @see org.jetbrains.dokka.gradle.formats.DokkaPublication.includes */
    val includes: Set<String>? = null,
    val setIncludes: Set<String>? = null,
    /**
     * Classpath that contains the Dokka Generator Plugins used to modify this publication.
     *
     * The plugins should be configured in [org.jetbrains.dokka.gradle.formats.DokkaPublication.pluginsConfiguration].
     */
    val pluginsClasspath: Set<String>? = null,
    val setPluginsClasspath: Set<String>? = null,
    /**
     * Source sets used to generate a Dokka Module.
     *
     * The values are not used directly in this task, but they are required to be registered as a
     * task input for up-to-date checks
     */
    val dokkaSourceSets: Set<@Serializable(with = DokkaSourceSetSpecTransformingSerializer::class) DokkaSourceSetSpec>? = null,
    /** @see org.jetbrains.dokka.gradle.formats.DokkaPublication.finalizeCoroutines */
    val finalizeCoroutines: Boolean? = null,
) {

    context(Project)
    fun applyTo(recipient: DokkaGeneratorParametersSpec) {
        recipient.moduleName tryAssign moduleName
        recipient.moduleVersion tryAssign moduleVersion
        recipient.failOnWarning tryAssign failOnWarning
        recipient.offlineMode tryAssign offlineMode
        recipient.suppressObviousFunctions tryAssign suppressObviousFunctions
        recipient.suppressInheritedMembers tryAssign suppressInheritedMembers
        includes?.toTypedArray()?.let(recipient.includes::from)
        setIncludes?.let(recipient.includes::setFrom)
        pluginsClasspath?.toTypedArray()?.let(recipient.pluginsClasspath::from)
        setPluginsClasspath?.let(recipient.pluginsClasspath::setFrom)

        dokkaSourceSets?.forEach { dokkaSourceSet ->
            dokkaSourceSet.applyTo(recipient.dokkaSourceSets)
        }

        recipient.finalizeCoroutines tryAssign finalizeCoroutines
    }
}
