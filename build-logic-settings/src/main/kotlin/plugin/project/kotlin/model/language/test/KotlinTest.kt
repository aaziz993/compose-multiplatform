package plugin.project.kotlin.model.language.test

import gradle.trySet
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.tasks.KotlinTest

internal interface KotlinTest : AbstractTestTask {

    val targetName: String?
    val ignoreRunFailures: Boolean?

    fun applyTo(test: KotlinTest) {
        ignoreFailures?.let(test::setIgnoreFailures)
        filter?.applyTo(test.filter)
        test::targetName trySet targetName
        test::ignoreRunFailures trySet ignoreRunFailures
    }
}
