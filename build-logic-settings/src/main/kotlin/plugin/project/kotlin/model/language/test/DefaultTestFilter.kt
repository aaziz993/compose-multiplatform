package plugin.project.kotlin.model.language.test

import kotlinx.serialization.Serializable

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
) : TestFilter

internal fun org.gradle.api.internal.tasks.testing.filter.DefaultTestFilter.configureFrom(config: DefaultTestFilter) {
    config.includeTestsMatchings?.forEach(::includeTestsMatching)
    config.excludeTestsMatchings?.forEach(::excludeTestsMatching)
    config.includePatterns?.let(includePatterns::addAll)
    config.excludePatterns?.let(excludePatterns::addAll)
    config.includeTests?.forEach { (className, methodName) -> includeTest(className, methodName) }
    config.excludeTests?.forEach { (className, methodName) -> excludeTest(className, methodName) }
    config.failOnNoMatchingTests?.let(::setFailOnNoMatchingTests)
    config.commandLineIncludePatterns?.let(::setCommandLineIncludePatterns)
}
