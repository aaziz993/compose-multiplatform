package plugin.project.web

import gradle.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmWasiTargetDsl
import plugin.project.kotlin.model.language.KotlinWasmWasiTarget

internal class WasmWasiPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.kotlin.targets?.none { target -> target is KotlinWasmWasiTarget } == true) {
                return@with
            }

            configureJsTestTasks<KotlinWasmWasiTargetDsl>()
        }
    }
}

