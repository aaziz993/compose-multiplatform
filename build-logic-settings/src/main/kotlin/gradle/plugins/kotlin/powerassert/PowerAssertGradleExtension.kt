package gradle.plugins.kotlin.powerassert

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.powerAssert
import gradle.accessors.settings
import gradle.api.tryAddAll
import gradle.api.tryAssign
import org.gradle.api.Project

internal interface PowerAssertGradleExtension {

    /**
     * Defines the fully-qualified path of functions which should be transformed by the Power-Assert compiler plugin.
     * If nothing is defined, defaults to [`kotlin.assert`][assert].
     */
    val functions: Set<String>?
    val setFunctions: Set<String>?

    /**
     * Defines the Kotlin SourceSets by name which will be transformed by the Power-Assert compiler plugin.
     * When the provider returns `null` - which is the default - all test SourceSets will be transformed.
     */
    val includedSourceSets: Set<String>?
    val setIncludedSourceSets: Set<String>?

    context(Project)
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("powerAssert").id) {
            project.powerAssert.functions tryAddAll functions
            project.powerAssert.functions tryAssign setFunctions
            project.powerAssert.includedSourceSets tryAddAll includedSourceSets
            project.powerAssert.includedSourceSets tryAssign setIncludedSourceSets
        }
}
