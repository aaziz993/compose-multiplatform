package gradle.plugins.java

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
) : TestFrameworkOptions() {

    context(Project)
    override fun applyTo(recipient: org.gradle.api.tasks.testing.TestFrameworkOptions) {
        options as JUnitOptions

        includeCategories?.toTypedArray()?.let(options::includeCategories)

        setIncludeCategories?.let(options::setIncludeCategories)

        excludeCategories?.toTypedArray()?.let(options::excludeCategories)

        setExcludeCategories?.let(options::setExcludeCategories)
    }
}
