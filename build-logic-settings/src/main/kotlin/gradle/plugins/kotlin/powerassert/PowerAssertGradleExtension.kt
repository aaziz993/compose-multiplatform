package gradle.plugins.kotlin.powerassert

import gradle.accessors.powerAssert
import gradle.api.provider.tryAddAll
import gradle.api.provider.tryAssign
import kotlinx.serialization.Serializable
import org.gradle.api.Project

@Serializable
internal data class PowerAssertGradleExtension(
    /**
     * Defines the fully-qualified path of functions which should be transformed by the Power-Assert compiler plugin.
     * If nothing is defined, defaults to [`kotlin.assert`][assert].
     */
    val functions: Set<String>? = null,
    val setFunctions: Set<String>? = null,
    /**
     * Defines the Kotlin SourceSets by name which will be transformed by the Power-Assert compiler plugin.
     * When the provider returns `null` - which is the default - all test SourceSets will be transformed.
     */
    val includedSourceSets: Set<String>? = null,
    val setIncludedSourceSets: Set<String>? = null,
) {

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin("org.jetbrains.kotlin.plugin.power-assert") {
            project.powerAssert.functions tryAddAll functions
            project.powerAssert.functions tryAssign setFunctions
            project.powerAssert.includedSourceSets tryAddAll includedSourceSets
            project.powerAssert.includedSourceSets tryAssign setIncludedSourceSets
        }
}
