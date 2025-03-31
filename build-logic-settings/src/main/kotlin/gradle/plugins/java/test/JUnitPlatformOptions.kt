package gradle.plugins.java.test

import gradle.api.tasks.test.TestFrameworkOptions
import gradle.reflect.trySet
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
        receiver::includeEngines trySet includeEngines
        receiver::setIncludeEngines trySet setIncludeEngines
        receiver::excludeEngines trySet excludeEngines
        receiver::setExcludeEngines trySet setExcludeEngines
        receiver::includeTags trySet includeTags
        receiver::setIncludeTags trySet setIncludeTags
        receiver::excludeTags trySet excludeTags
        receiver::setExcludeTags trySet setExcludeTags
    }
}
