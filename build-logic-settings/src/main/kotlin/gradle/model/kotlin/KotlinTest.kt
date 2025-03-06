package gradle.model.kotlin

import gradle.model.AbstractTestTask
import gradle.trySet
import org.gradle.api.Named
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.tasks.KotlinTest

internal interface KotlinTest : AbstractTestTask {
    val ignoreRunFailures: Boolean?

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as KotlinTest

        named::ignoreRunFailures trySet ignoreRunFailures
    }
}
