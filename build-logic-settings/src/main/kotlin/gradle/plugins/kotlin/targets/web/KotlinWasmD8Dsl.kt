package gradle.plugins.kotlin.targets.web

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmD8Dsl

@Serializable
internal data class KotlinWasmD8Dsl(
    override val distribution: Distribution? = null,
    override val testTask: KotlinJsTest? = null,
    val runTask: D8Exec? = null,
) : KotlinJsSubTargetDsl {

    context(Project)
    override fun applyTo(receiver: org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsSubTargetDsl, distributionName: String) {
        super.applyTo(receiver, distributionName)

        receiver as KotlinWasmD8Dsl

        runTask?.let { runTask ->
            receiver.runTask {
                runTask.applyTo(this)
            }
        }
    }
}
