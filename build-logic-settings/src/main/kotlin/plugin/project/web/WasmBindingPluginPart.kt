package plugin.project.web

import gradle.moduleProperties
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmJsTargetDsl
import org.gradle.api.Plugin
import plugin.project.model.target.TargetType
import plugin.project.model.target.add
import plugin.project.model.target.contains

internal class WasmBindingPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            moduleProperties.targets?.let { targets ->

                if (TargetType.WASM !in targets) {
                    return@with
                }

                targets
                    .filter { target -> target.type.isDescendantOf(TargetType.WASM) }
                    .forEach { target -> target.add() }
            }

            configureKotlinJsTarget<KotlinWasmJsTargetDsl>()
            configureJsTestTasks<KotlinWasmJsTargetDsl>()
        }
    }
}

