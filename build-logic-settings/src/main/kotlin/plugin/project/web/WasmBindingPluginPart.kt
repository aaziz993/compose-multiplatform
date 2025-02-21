package plugin.project.web

import gradle.moduleProperties
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmJsTargetDsl
import org.gradle.api.Plugin
import plugin.project.model.target.TargetType
import plugin.project.model.target.applyTo
import plugin.project.model.target.contains

internal class WasmBindingPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (TargetType.WASM !in moduleProperties.targets) {
                return@with
            }

            moduleProperties.targets
                .filter { target -> target.type.isDescendantOf(TargetType.WASM) }
                .forEach { target -> target.applyTo() }

            configureKotlinJsTarget<KotlinWasmJsTargetDsl>()
            configureJsTestTasks<KotlinWasmJsTargetDsl>()
        }
    }
}

