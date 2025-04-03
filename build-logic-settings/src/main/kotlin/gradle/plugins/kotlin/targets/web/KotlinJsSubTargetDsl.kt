package gradle.plugins.kotlin.targets.web

import gradle.plugins.kotlin.targets.web.tasks.KotlinJsTest
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsSubTargetDsl

internal interface KotlinJsSubTargetDsl {

    val distribution: Distribution?

    val testTask: KotlinJsTest?

    context(Project)
    @OptIn(ExperimentalDistributionDsl::class)
    fun applyTo(receiver: KotlinJsSubTargetDsl, distributionName: String) {
        distribution?.let { distribution ->
            receiver.distribution {
                distribution.applyTo(this, distributionName)
            }
        }

        testTask?.let { testTask ->
            receiver.testTask {
                testTask.applyTo(this)
            }
        }
    }
}
