package plugins.web

import gradle.accessors.projectProperties
import gradle.plugins.kmp.web.KotlinWasmWasiTargetDslImpl
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmWasiTargetDsl

internal class WasmWasiPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.kotlin.targets.none { target -> target is KotlinWasmWasiTargetDslImpl }) {
                return@with
            }

            configureJsTestTasks<KotlinWasmWasiTargetDsl>()
        }
    }
}

