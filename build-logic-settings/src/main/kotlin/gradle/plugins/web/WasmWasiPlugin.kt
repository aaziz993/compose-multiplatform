package gradle.plugins.web

import gradle.accessors.projectProperties
import gradle.plugins.kmp.web.KotlinWasmWasiTargetDsl
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class WasmWasiPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.kotlin.targets.none { target -> target is KotlinWasmWasiTargetDsl }) {
                return@with
            }
        }
    }
}

