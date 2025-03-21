package gradle.plugins.java

import gradle.api.tasks.test.TestFrameworkOptions
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.tasks.testing.junitplatform.JUnitPlatformOptions

/**
 * The JUnit platform specific test options.
 *
 * @see [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide)
 *
 * @since 4.6
 */
@Serializable
internal class JUnitPlatformOptions(
    /**
     * The set of engines to run with.
     *
     * @see [Test Engine](https://junit.org/junit5/docs/current/user-guide/.launcher-api-engines-custom)
     */
    val includeEngines: Set<String>? = null,
    val setIncludeEngines: Set<String>? = null,
    /**
     * The set of engines to exclude.
     *
     * @see [Test Engine](https://junit.org/junit5/docs/current/user-guide/.launcher-api-engines-custom)
     */
    val excludeEngines: Set<String>? = null,
    val setExcludeEngines: Set<String>? = null,
    /**
     * The set of tags to run with.
     *
     * @see [Tagging and Filtering](https://junit.org/junit5/docs/current/user-guide/.writing-tests-tagging-and-filtering)
     */
    val includeTags: Set<String>? = null,
    val setIncludeTags: Set<String>? = null,
    /**
     * The set of tags to exclude.
     *
     * @see [Tagging and Filtering](https://junit.org/junit5/docs/current/user-guide/.writing-tests-tagging-and-filtering)
     */
    val excludeTags: Set<String>? = null,
    val setExcludeTags: Set<String>? = null,
) : TestFrameworkOptions() {

    context(Project)
    override fun applyTo(recipient: org.gradle.api.tasks.testing.TestFrameworkOptions) {
        options as JUnitPlatformOptions

        includeEngines?.toTypedArray()?.let(options::includeEngines)

        setIncludeEngines?.let(options::setIncludeEngines)

        excludeEngines?.toTypedArray()?.let(options::excludeEngines)

        setExcludeEngines?.let(options::setExcludeEngines)

        includeTags?.toTypedArray()?.let(options::includeTags)

        setIncludeTags?.let(options::setIncludeTags)

        excludeTags?.toTypedArray()?.let(options::excludeTags)

        setExcludeTags?.let(options::setExcludeTags)
    }
}
