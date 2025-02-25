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
import plugin.project.model.ProjectLayout
import plugin.project.model.ProjectType

internal class JavaPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            projectProperties.kotlin.jvm?.forEach { targetName, target ->
                if (targetName.isNotEmpty()) {
                    kotlin.jvm(targetName) {
                        target.applyTo(this)
                    }
                }
                else kotlin.jvm {
                    target.applyTo(this)
                }
            } ?: return

            if (projectProperties.kotlin.android != null) {
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
