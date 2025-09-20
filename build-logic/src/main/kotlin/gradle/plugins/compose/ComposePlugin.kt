@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.plugins.compose

import gradle.api.project.ProjectLayout
import gradle.api.project.compose
import gradle.api.project.desktop
import gradle.api.project.kotlin
import gradle.api.project.projectScript
import gradle.api.project.resources
import gradle.api.project.sourceSetsToComposeResourcesDirs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.assign
import org.jetbrains.compose.resources.configureComposeResourcesGeneration
import org.jetbrains.compose.resources.configureMultimoduleResources
import org.jetbrains.compose.resources.AssembleTargetResourcesTask
import org.jetbrains.compose.resources.onKotlinJvmApplied

public class ComposePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            project.pluginManager.withPlugin("org.jetbrains.compose") {
                adjustResources()
            }
        }
    }

    private fun Project.adjustResources() = project.pluginManager.withPlugin("org.jetbrains.compose") {
        var dir = "src/commonMain/composeResources"
        when (project.projectScript.layout) {
            is ProjectLayout.Flat -> {
                kotlin.sourceSets.forEach { sourceSet ->
                    compose.resources.customDirectory(
                        sourceSet.name,
                        project.provider { sourceSetsToComposeResourcesDirs[sourceSet]!! },
                    )
                }
                dir = "composeResources"
            }

            else -> Unit
        }

        // jpackage only supports .png on Linux, .ico on Windows, .icns on Mac, so a developer must do a conversion (probably from a png) to a 3 different formats.
        // Also it seems that ico and icns need to contain an icon in multiple resolutions, so the conversion becomes a bit inconvenient.
        compose.desktop.application.nativeDistributions {
            macOS {
                if (!iconFile.isPresent) iconFile = project.file("$dir/drawable/compose-multiplatform.icns")
            }
            linux {
                if (!iconFile.isPresent) iconFile = project.file("$dir/drawable/compose-multiplatform.png")
            }
            windows {
                if (!iconFile.isPresent) iconFile = project.file("$dir/drawable/compose-multiplatform.ico")
            }
        }
    }
}
