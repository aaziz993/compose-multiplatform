package gradle.plugins.kotlin.ksp

import gradle.accessors.id
import gradle.accessors.catalog.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.settings
import gradle.plugins.kotlin.ksp.model.KspSettings
import org.gradle.api.Plugin
import org.gradle.api.Project

internal class KspPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.ksp.takeIf(KspSettings::enabled)?.let { ksp ->
                plugins.apply(project.settings.libs.plugin("ksp").id)

                ksp.applyTo()
            }
        }
    }
}
