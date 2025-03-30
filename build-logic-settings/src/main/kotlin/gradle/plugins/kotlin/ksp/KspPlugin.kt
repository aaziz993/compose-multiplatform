package gradle.plugins.kotlin.ksp

import gradle.accessors.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KspPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.ksp?.applyTo()
        }
    }
}
