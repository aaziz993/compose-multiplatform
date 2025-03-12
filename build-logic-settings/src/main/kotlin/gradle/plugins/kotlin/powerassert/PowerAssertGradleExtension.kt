package gradle.plugins.kotlin.powerassert

import gradle.powerAssert
import gradle.tryAssign
import org.gradle.api.Project

internal interface PowerAssertGradleExtension {

    /**
     * Defines the fully-qualified path of functions which should be transformed by the Power-Assert compiler plugin.
     * If nothing is defined, defaults to [`kotlin.assert`][assert].
     */
    val functions: Set<String>?

    /**
     * Defines the Kotlin SourceSets by name which will be transformed by the Power-Assert compiler plugin.
     * When the provider returns `null` - which is the default - all test SourceSets will be transformed.
     */
    val includedSourceSets: Set<String>?

    context(Project)
    fun applyTo() {
        powerAssert.functions tryAssign functions
        powerAssert.includedSourceSets tryAssign includedSourceSets
    }
}
