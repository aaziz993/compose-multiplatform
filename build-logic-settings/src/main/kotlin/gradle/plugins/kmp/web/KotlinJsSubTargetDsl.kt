package gradle.plugins.kmp.web

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsSubTargetDsl

internal interface KotlinJsSubTargetDsl {

    val distribution: Distribution?

    val testTask: KotlinJsTest?

    context(project: Project)
    @OptIn(ExperimentalDistributionDsl::class)
    fun applyTo(target: KotlinJsSubTargetDsl, distributionName: String) {
        distribution?.let { distribution ->
            target.distribution {
                distribution.applyTo(this, distributionName)
            }
        }

        testTask?.let { testTask ->
            target.testTask {
                testTask.applyTo(this)
            }
        }
    }
}
