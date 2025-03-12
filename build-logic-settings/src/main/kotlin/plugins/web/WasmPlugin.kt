package plugins.web

import gradle.accessors.projectProperties
import gradle.plugins.kmp.web.KotlinWasmJsTarget
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmJsTargetDsl

internal class WasmPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.kotlin.targets.none { target -> target is KotlinWasmJsTarget }) {
                return@with
            }

            configureJsTestTasks<KotlinWasmJsTargetDsl>()
        }
    }
}

