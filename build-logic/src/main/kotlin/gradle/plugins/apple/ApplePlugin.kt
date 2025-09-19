package gradle.plugins.apple

import gradle.api.project.ProjectLayout
import gradle.api.project.apple
import gradle.api.project.projectScript
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.extraProperties

public class ApplePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.withPlugin("org.jetbrains.gradle.apple.applePlugin") {
                // ios Compose uses UiKit, so we need to explicitly enable it, since it is experimental.
                extraProperties["org.jetbrains.compose.experimental.uikit.enabled"] = "true"

                project.extraProperties.set("generateBuildableXcodeproj.skipKotlinFrameworkDependencies", "true")

                // Add ios App
                apple.iosApp {
                    buildSettings.DEVELOPMENT_TEAM(group.toString())
                    productInfo["UILaunchScreen"] = mapOf<String, Any>()
                }
                adjustSourceSets()
            }
        }
    }

    private fun Project.adjustSourceSets() = when (val layout = projectScript.layout) {
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
