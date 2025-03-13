package gradle.api.tasks.test

import org.gradle.api.Project
import org.gradle.api.tasks.testing.TestFrameworkOptions

/**
 * The base class for any test framework specific options.
 */
internal abstract class TestFrameworkOptions {

    context(Project)
    abstract fun applyTo(options: TestFrameworkOptions)
}
