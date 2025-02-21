package plugin.project.kotlin.model.language.test

import kotlinx.serialization.Serializable
import org.gradle.api.internal.tasks.testing.filter.DefaultTestFilter

@Serializable
internal data class DefaultTestFilter(
    override val includeTestsMatchings: Set<String>? = null,
    override val excludeTestsMatchings: Set<String>? = null,
    override val includePatterns: Set<String>? = null,
    override val excludePatterns: Set<String>? = null,
    override val includeTests: Set<Test>? = null,
    override val excludeTests: Set<Test>? = null,
    override val failOnNoMatchingTests: Boolean? = null,
    val commandLineIncludePatterns: Set<String>? = null,
) : TestFilter {

    fun applyTo(defaultTestFilter: DefaultTestFilter) {
        (this as TestFilter).applyTo(defaultTestFilter)
        commandLineIncludePatterns?.let(defaultTestFilter::setCommandLineIncludePatterns)
    }
}
