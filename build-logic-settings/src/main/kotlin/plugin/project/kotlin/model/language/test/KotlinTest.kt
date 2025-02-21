package plugin.project.kotlin.model.language.test

import gradle.trySet
import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.tasks.KotlinTest

@Serializable
internal data class KotlinTest(
    override val ignoreFailures: Boolean? = null,
    override val filter: DefaultTestFilter? = null,
    val targetName: String? = null,
    val ignoreRunFailures: Boolean? = null,
) : AbstractTestTask {

    fun applyTo(test: KotlinTest) {
        ignoreFailures?.let(test::setIgnoreFailures)
        filter?.applyTo(test.filter)
        test::targetName trySet targetName
        test::ignoreRunFailures trySet ignoreRunFailures
    }
}
