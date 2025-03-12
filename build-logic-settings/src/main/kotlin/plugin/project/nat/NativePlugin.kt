package plugin.project.nat

import gradle.plugins.kmp.nat.KotlinNativeTarget
import gradle.accessors.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class NativePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.kotlin.targets.none { target -> target is KotlinNativeTarget } != false) {
                return@with
            }
        }
    }
}
