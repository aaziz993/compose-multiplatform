package plugin.project.web

import gradle.kotlin
import gradle.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmJsTargetDsl

internal class WasmPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.kotlin.wasmJs?.forEach { targetName, target ->
                targetName.takeIf(String::isNotEmpty)?.also { targetName ->
                    kotlin.wasmJs(targetName) {
                        target.applyTo(this)
                    }
                } ?: kotlin.wasmJs {
                    target.applyTo(this)
                }
            } ?: return

            configureJsTestTasks<KotlinWasmJsTargetDsl>()
        }
    }
}

