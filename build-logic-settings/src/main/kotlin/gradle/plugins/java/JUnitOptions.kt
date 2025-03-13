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
    override fun applyTo(options: org.gradle.api.tasks.testing.TestFrameworkOptions) {
        options as JUnitOptions

        includeCategories?.let { includeCategories ->
            options.includeCategories(*includeCategories.toTypedArray())
        }

        setIncludeCategories?.let(options::setIncludeCategories)

        excludeCategories?.let { excludeCategories ->
            options.excludeCategories(*excludeCategories.toTypedArray())
        }

        setExcludeCategories?.let(options::setExcludeCategories)
    }
}
