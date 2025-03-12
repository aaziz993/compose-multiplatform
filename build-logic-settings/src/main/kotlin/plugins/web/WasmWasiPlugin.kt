package plugins.web

import gradle.plugins.kmp.web.KotlinWasmWasiTarget
import gradle.accessors.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmWasiTargetDsl

internal class WasmWasiPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.kotlin.targets.none { target -> target is KotlinWasmWasiTarget }) {
                return@with
            }

            configureJsTestTasks<KotlinWasmWasiTargetDsl>()
        }
    }
}

