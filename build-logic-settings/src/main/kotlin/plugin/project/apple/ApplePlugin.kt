package plugin.project.apple

import gradle.all
import gradle.apple
import gradle.id
import gradle.java
import gradle.libs
import gradle.model.kotlin.kmp.nat.apple.KotlinAppleTarget
import gradle.model.project.ProjectLayout
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.replace
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.jetbrains.kotlin.gradle.plugin.extraProperties

internal class ApplePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (projectProperties.kotlin.targets?.none { target -> target is KotlinAppleTarget } != false) {
                return@with
            }

            extraProperties.set("generateBuildableXcodeproj.skipKotlinFrameworkDependencies", "true")

            plugins.apply(settings.libs.plugins.plugin("apple").id)

            projectProperties.apple.applyTo()

            adjustSourceSets()
        }
    }

    private fun Project.adjustSourceSets() = when (projectProperties.layout) {
        ProjectLayout.FLAT -> apple.sourceSets.all {
            println("""
                APPLE SOURCE SET: $name
                SRCS: ${apple.srcDirs.map{ it.absolutePath }}
            """.trimIndent())
            apple.srcDir(
                    if (name.endsWith("AppMain"))
                        "src@${name.removeSuffix("AppMain")}"
                    else
                        "test@${name.removeSuffix("AppTest")}",
            )
        }

        else -> Unit
    }
}
