package plugin.project.kotlin.ksp

import gradle.id
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import plugin.model.dependency.Dependency

internal class KspPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.ksp.takeIf { it.enabled && projectProperties.kotlin.targets?.isNotEmpty()==true }?.let { ksp ->
                plugins.apply(settings.libs.plugins.plugin("ksp").id)

                ksp.applyTo()
            }
        }
    }
}
