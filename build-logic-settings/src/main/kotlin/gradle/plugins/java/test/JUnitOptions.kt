package gradle.plugins.java.test

import gradle.api.tasks.test.TestFrameworkOptions
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.tasks.testing.junit.JUnitOptions

/**
 * The JUnit specific test options.
 */
@Serializable
internal data class JUnitOptions(
    /**
     * The set of categories to run.
     */
    val includeCategories: Set<String>? = null,
    val setIncludeCategories: Set<String>? = null,
    /**
     * The set of categories to exclude.
     */
    val excludeCategories: Set<String>? = null,
    val setExcludeCategories: Set<String>? = null,
) : TestFrameworkOptions<JUnitOptions>() {

    context(project: Project)
    override fun applyTo(receiver: JUnitOptions) {
        includeCategories?.toTypedArray()?.let(receiver::includeCategories)
        setIncludeCategories?.let(receiver::setIncludeCategories)
        excludeCategories?.toTypedArray()?.let(receiver::excludeCategories)
        setExcludeCategories?.let(receiver::setExcludeCategories)
    }
}
