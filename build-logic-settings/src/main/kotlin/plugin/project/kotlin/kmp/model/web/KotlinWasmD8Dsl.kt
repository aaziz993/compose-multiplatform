import org.jetbrains.kotlin.gradle.targets.js.d8.D8Exec
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsSubTargetDsl

interface KotlinWasmD8Dsl : KotlinJsSubTargetDsl {
    fun runTask(body: D8Exec.() -> Unit)
}
