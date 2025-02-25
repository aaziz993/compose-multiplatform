package plugin.project.nat

import gradle.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project
import plugin.project.kotlin.model.language.KotlinNativeTarget

internal class NativePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.kotlin.targets?.none { target -> target is KotlinNativeTarget } == true) {
                return@with
            }
        }
    }
}
