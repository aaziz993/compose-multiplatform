package gradle.plugins.java.test

import gradle.api.tasks.test.TestFrameworkOptions
import gradle.reflect.trySet
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
    override fun applyTo(receiver: JUnitOptions) {
        receiver::includeCategories trySet includeCategories
        receiver::setIncludeCategories trySet setIncludeCategories
        receiver::excludeCategories trySet excludeCategories
        receiver::setExcludeCategories trySet setExcludeCategories
    }
}
