package plugin.project.kotlin.ksp

import gradle.libs
import gradle.projectProperties
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate

internal class KspPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            settings.projectProperties.plugins.ksp.let { ksp ->
                if (!ksp.enabled || settings.projectProperties.kotlin.targets.isEmpty()) {
                    return@with
                }

                plugins.apply(project.libs.plugins.ksp.get().pluginId)

                configureKspExtension()

                if (name == "android-app") {
                    println("CONFIGS: $settings.projectProperties.kotlin.targets}")
                }

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
