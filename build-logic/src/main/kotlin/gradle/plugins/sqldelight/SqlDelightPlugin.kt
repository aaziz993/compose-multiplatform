package gradle.plugins.sqldelight

import gradle.api.kotlin.mpp.kotlin
import gradle.api.project.ProjectLayout
import gradle.api.project.projectScript
import gradle.api.sqldelight.sqldelight
import org.gradle.api.Plugin
import org.gradle.api.Project

public class SqlDelightPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.withPlugin("app.cash.sqldelight") {
                adjustSourceSets()
            }
        }
    }

    private fun Project.adjustSourceSets() = when (projectScript.layout) {
        is ProjectLayout.Flat -> sqldelight.databases.forEach { database ->
            kotlin.sourceSets.forEach { sourceSet ->
                database.srcDirs(sourceSet.resources.srcDirs.first().resolve("sqldelight"))
            }
        }

        else -> Unit
    }
}
