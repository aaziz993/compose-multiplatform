package plugin.project.java

import app.cash.sqldelight.core.capitalize
import gradle.java
import gradle.kotlin
import gradle.projectProperties
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.SourceSet
import plugin.project.kotlin.model.language.KotlinAndroidTarget
import plugin.project.kotlin.model.language.KotlinJvmTarget
import plugin.project.model.ProjectLayout
import plugin.project.model.ProjectType

internal class JavaPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (
                projectProperties.kotlin.targets.orEmpty().any { target -> target is KotlinAndroidTarget } ||
                projectProperties.kotlin.targets.orEmpty().none { target -> target is KotlinJvmTarget }
            ) {
                return@with
            }

            plugins.apply(JavaPlugin::class.java)

            adjustSourceSets()

            projectProperties.jvm?.applyTo()

            if (projectProperties.type == ProjectType.APP && !projectProperties.compose.enabled) {
                plugins.apply(ApplicationPlugin::class.java)
                projectProperties.application?.applyTo()
            }
        }
    }

    private fun Project.adjustSourceSets() {
        when (projectProperties.layout) {
            ProjectLayout.FLAT -> java.sourceSets.all {
                val (compilationPrefixPart, resourcesPrefixPart) = if (SourceSet.isMain(this)) {
                    "src" to "resources"
                }
                else {
                    "test" to "testResources"
                }

                java.setSrcDirs(listOf("$compilationPrefixPart@jvm"))
                resources.setSrcDirs(listOf("$resourcesPrefixPart@jvm"))
            }

            else -> java.sourceSets.all {
                val compilationName = name.capitalize()
                java.setSrcDirs(listOf("src/jvm$compilationName/java"))
                resources.setSrcDirs(listOf("src/jvm$compilationName/resources"))
            }
        }
    }
}
