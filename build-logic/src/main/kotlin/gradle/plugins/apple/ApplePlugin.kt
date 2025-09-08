package gradle.plugins.apple

import gradle.api.project.ProjectLayout
import gradle.api.project.apple
import gradle.api.project.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project

public class ApplePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.withPlugin("org.jetbrains.gradle.apple.applePlugin") {
                adjustSourceSets()
            }
        }
    }

    private fun Project.adjustSourceSets() = when (val layout = projectProperties.layout) {
        is ProjectLayout.Flat -> apple.sourceSets.configureEach {
            apple.srcDir(
                if (sourceSetName.endsWith("AppMain"))
                    "src${layout.targetDelimiter}${sourceSetName.removeSuffix("AppMain")}"
                else
                    "test${layout.targetDelimiter}${sourceSetName.removeSuffix("AppTest")}",
            )
        }

        else -> Unit
    }
}
