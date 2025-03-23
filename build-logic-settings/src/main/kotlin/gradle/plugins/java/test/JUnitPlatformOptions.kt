package gradle.plugins.java.test

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
) : TestFrameworkOptions<JUnitPlatformOptions>() {

    context(Project)
    override fun applyTo(receiver: JUnitPlatformOptions) {
        includeEngines?.toTypedArray()?.let(receiver::includeEngines)
        setIncludeEngines?.let(receiver::setIncludeEngines)
        excludeEngines?.toTypedArray()?.let(receiver::excludeEngines)
        setExcludeEngines?.let(receiver::setExcludeEngines)
        includeTags?.toTypedArray()?.let(receiver::includeTags)
        setIncludeTags?.let(receiver::setIncludeTags)
        excludeTags?.toTypedArray()?.let(receiver::excludeTags)
        setExcludeTags?.let(receiver::setExcludeTags)
    }
}
