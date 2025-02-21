package plugin.project.kotlin.ksp

import gradle.libs
import gradle.moduleProperties
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate

internal class KspPluginPart : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            moduleProperties.settings.kotlin.ksp.let { ksp ->
                if (!ksp.enabled || moduleProperties.targets.isEmpty()) {
                    return@with
                }

                plugins.apply(project.libs.plugins.ksp.get().pluginId)

                configureKspExtension()

                if(name=="android-app"){
                    println("CONFIGS: ${moduleProperties.targets}")
                }

                val kspCommonMainMetadata by configurations
                dependencies {
                    ksp.processors?.forEach { processor ->
                        kspCommonMainMetadata(processor.toDependencyNotation())
                    }
                }
            }
        }
    }
}
