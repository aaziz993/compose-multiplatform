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

internal class KspPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.plugins.ksp.let { ksp ->
                if (!ksp.enabled || projectProperties.kotlin.hasTargets) {
                    return@with
                }

                plugins.apply(settings.libs.plugins.plugin("ksp").id)

                configureKspExtension()

                val kspCommonMainMetadata by configurations
                dependencies {
                    ksp.processors?.forEach { processor ->
                        kspCommonMainMetadata(processor.resolve())
                    }
                }
            }
        }
    }
}
