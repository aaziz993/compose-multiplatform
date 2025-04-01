package gradle.plugins.kotlin.targets.web

import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalDistributionDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalMainFunctionArgumentsDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsNodeDsl

@Serializable
internal data class KotlinJsNodeDsl(
    override val distribution: Distribution? = null,
    override val testTask: KotlinJsTest? = null,
    val runTask: NodeJsExec? = null,
    val passProcessArgvToMainFunction: Boolean? = null,
) : KotlinJsSubTargetDsl {

    context(Project)
    @OptIn(ExperimentalDistributionDsl::class, ExperimentalMainFunctionArgumentsDsl::class)
    fun applyTo(node: KotlinJsNodeDsl, distributionName: String) {
        distribution?.let { distribution ->
            node.distribution {
                distribution.applyTo(this, distributionName)
            }
        }

        testTask?.let { testTask ->
            node.testTask {
                testTask.applyTo(this)
            }
        }

        runTask?.let { runTask ->
            node.runTask {
                runTask.applyTo(this)
            }
        }

        node::passProcessArgvToMainFunction trySet passProcessArgvToMainFunction
    }
}
