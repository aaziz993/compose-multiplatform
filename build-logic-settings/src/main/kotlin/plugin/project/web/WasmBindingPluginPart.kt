package plugin.project.web

import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmJsTargetDsl
import plugin.project.kotlin.model.target.TargetType
import plugin.project.kotlin.model.target.applyTo
import plugin.project.kotlin.model.target.contains

internal class WasmBindingPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (TargetType.WASM !in settings.projectProperties.kotlin.targets) {
                return@with
            }

           settings.projectProperties.kotlin.targets
                .filter { target -> target.type.isDescendantOf(TargetType.WASM) }
                .forEach { target -> target.applyTo() }

            configureKotlinJsTarget<KotlinWasmJsTargetDsl>()
            configureJsTestTasks<KotlinWasmJsTargetDsl>()
        }
    }
}

