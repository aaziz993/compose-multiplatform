package plugins.web

import gradle.accessors.projectProperties
import gradle.plugins.kmp.web.KotlinWasmJsTargetDsl
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmJsTargetDsl

internal class WasmPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.kotlin.targets.none { target -> target is KotlinWasmJsTargetDsl }) {
                return@with
            }

            configureJsTestTasks<org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmJsTargetDsl>()
        }
    }
}

