package plugins.kotlin.ksp

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import plugins.kotlin.ksp.model.KspSettings

internal class KspPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.ksp.takeIf(KspSettings::enabled)?.let { ksp ->
                plugins.apply(project.settings.libs.plugins.plugin("ksp").id)

                ksp.applyTo()
            }
        }
    }
}
