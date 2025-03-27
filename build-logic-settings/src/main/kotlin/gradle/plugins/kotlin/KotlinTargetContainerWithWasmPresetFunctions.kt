package gradle.plugins.kotlin

import gradle.plugins.kmp.web.KotlinWasmJsTargetDsl
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinTargetContainerWithWasmPresetFunctions

internal interface KotlinTargetContainerWithWasmPresetFunctions<T : KotlinTargetContainerWithWasmPresetFunctions> {

    val wasmJs: LinkedHashSet<KotlinWasmJsTargetDsl>?

    context(Project)
    fun applyTo(receiver: T) {
        wasmJs?.forEach { wasmJs ->
            receiver.wasmJs(wasmJs.name) {
                wasmJs.applyTo(this)
            }
        }
    }
}
