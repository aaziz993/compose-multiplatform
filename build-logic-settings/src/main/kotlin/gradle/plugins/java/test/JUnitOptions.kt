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

    context(Project)
    override fun applyTo(recipient: JUnitOptions) {
        includeCategories?.toTypedArray()?.let(recipient::includeCategories)
        setIncludeCategories?.let(recipient::setIncludeCategories)
        excludeCategories?.toTypedArray()?.let(recipient::excludeCategories)
        setExcludeCategories?.let(recipient::setExcludeCategories)
    }
}
