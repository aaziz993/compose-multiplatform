package gradle.plugins.compose

import gradle.api.project.ProjectLayout
import gradle.api.project.compose
import gradle.api.project.kotlin
import gradle.api.project.projectScript
import gradle.api.project.resources
import gradle.api.project.sourceSetsToComposeResourcesDirs
import org.gradle.api.Plugin
import org.gradle.api.Project

public class ComposePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            adjustResources()
        }
    }

    private fun Project.adjustResources() = project.pluginManager.withPlugin("org.jetbrains.compose") {
        when (project.projectScript.layout) {
            is ProjectLayout.Flat -> kotlin.sourceSets.forEach { sourceSet ->
                compose.resources.customDirectory(
                    sourceSet.name,
                    project.provider { sourceSetsToComposeResourcesDirs[sourceSet]!! },
                )
            }

            else -> Unit
        }
    }
}
