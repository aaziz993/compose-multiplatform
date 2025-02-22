package plugin.project.web

import gradle.kotlin
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
            if (projectProperties.kotlin.wasmJs == null) {
                return@with
            }

            projectProperties.kotlin.wasmJs!!.forEach { target ->
                target.applyTo()
            }

            configureJsTestTasks<KotlinWasmJsTargetDsl>()
        }
    }
}

