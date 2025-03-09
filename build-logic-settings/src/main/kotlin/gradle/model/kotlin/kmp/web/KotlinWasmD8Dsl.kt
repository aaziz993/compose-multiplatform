package gradle.model.kotlin.kmp.web

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
    override fun applyTo(target: org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsSubTargetDsl, moduleName: String) {
        super.applyTo(target, moduleName)

        target as KotlinWasmD8Dsl

        runTask?.let { runTask ->
            target.runTask {
                runTask.applyTo(this)
            }
        }
    }
}
