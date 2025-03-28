package gradle.plugins.kotlin

import gradle.api.applyTo
import org.gradle.kotlin.dsl.withType
import gradle.plugins.kotlin.targets.web.KotlinWasmJsTargetDsl
import gradle.plugins.kotlin.targets.web.KotlinWasmWasiTargetDsl
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinTargetContainerWithWasmPresetFunctions

internal interface KotlinTargetContainerWithWasmPresetFunctions<T : KotlinTargetContainerWithWasmPresetFunctions> {

    val wasmJs: LinkedHashSet<KotlinWasmJsTargetDsl>?

    val wasmWasi: LinkedHashSet<KotlinWasmWasiTargetDsl>?

    context(Project)
    fun applyTo(receiver: T) {
        wasmJs?.forEach { wasmJs ->
            wasmJs.applyTo(receiver.targets.withType<org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmJsTargetDsl>()) { name, action ->
                receiver.wasmJs(name, action::execute)
            }
        }

        wasmWasi?.forEach { wasmWasi ->
            wasmWasi.applyTo(receiver.targets.withType<org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmWasiTargetDsl>()) { name, action ->
                receiver.wasmWasi(name, action::execute)
            }
        }
    }
}
