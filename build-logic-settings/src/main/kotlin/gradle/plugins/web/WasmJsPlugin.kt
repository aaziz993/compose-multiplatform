package gradle.plugins.web

import gradle.accessors.projectProperties
import gradle.plugins.kmp.web.KotlinWasmJsTargetDsl
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class WasmJsPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.kotlin.targets.none { target -> target is KotlinWasmJsTargetDsl }) {
                return@with
            }
        }
    }
}

