package gradle.plugins.ksp

import gradle.api.kotlin.mpp.commonMainSourceSet
import gradle.api.kotlin.mpp.kotlin
import gradle.api.ksp.ksp
import gradle.api.project.ProjectLayout
import gradle.api.project.projectScript
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

public class KspPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.withPlugin("com.google.devtools.ksp") {
                configureArgs()
                configureTasks()
            }
        }
    }

    private fun Project.configureArgs() = ksp {
        arg("projectDir", projectDir.path)
        pluginManager.withPlugin("org.jetbrains.kotlin.multiplatform") {
            arg(
                "commonMainKotlinSrc",
                when (projectScript.layout) {
                    is ProjectLayout.Flat -> "/src/"
                    else -> "/src/${KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME}/kotlin/"
                },
            )
        }
    }

    // Trigger Common Metadata Generation from Native tasks.
    private fun Project.configureTasks() =
        tasks.matching { it.name.startsWith("ksp") && it.name != "kspCommonMainKotlinMetadata" }.configureEach {
            dependsOn("kspCommonMainKotlinMetadata")
        }
}
