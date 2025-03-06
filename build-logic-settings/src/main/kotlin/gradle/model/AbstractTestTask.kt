package gradle.model

import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.api.tasks.testing.AbstractTestTask

internal interface AbstractTestTask : Task{
    val filter: DefaultTestFilter?
    val ignoreFailures: Boolean?

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as AbstractTestTask

        ignoreFailures?.let(named::setIgnoreFailures)
        filter?.applyTo(named.filter)
    }
}
