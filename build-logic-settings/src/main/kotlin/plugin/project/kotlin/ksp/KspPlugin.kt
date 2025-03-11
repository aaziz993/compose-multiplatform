package plugin.project.kotlin.ksp

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KspPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.ksp.takeIf { it.enabled && projectProperties.kotlin.targets.isNotEmpty() }?.let { ksp ->
                plugins.apply(settings.libs.plugins.plugin("ksp").id)

                ksp.applyTo()
            }
        }
    }
}
