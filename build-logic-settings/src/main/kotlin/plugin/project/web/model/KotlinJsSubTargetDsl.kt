package plugin.project.web.model

import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsSubTargetDsl

internal interface KotlinJsSubTargetDsl {
    val distribution:Distribution?

    val testTask: KotlinJsTest?

    @OptIn(ExperimentalDistributionDsl::class)
    fun applyTo(target: KotlinJsSubTargetDsl){
        distribution?.let { distribution ->
            target.distribution(distribution::applyTo)
        }

        testTask?.let { testTask ->
            target.testTask {
                testTask.applyTo(this)
            }
        }
    }
}
