package plugin.project.java

import gradle.id
import gradle.all
import gradle.java
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import gradle.replace
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.SourceSet
import org.gradle.internal.extensions.stdlib.capitalized
import plugin.project.kotlin.model.KotlinAndroidTarget
import plugin.project.kotlin.model.KotlinJvmTarget
import plugin.project.model.ProjectLayout
import plugin.project.model.ProjectType

internal class JavaPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (
                projectProperties.kotlin.targets?.any { target ->
                    target is KotlinAndroidTarget
                } == true
                ||
                projectProperties.kotlin.targets?.none { target ->
                    target is KotlinJvmTarget
                } != false
            ) {
                return@with
            }


            plugins.apply(JavaPlugin::class.java)

            projectProperties.jvm?.applyTo()

            // Apply java application plugin.
            if (projectProperties.type == ProjectType.APP && !projectProperties.compose.enabled) {
                plugins.apply(ApplicationPlugin::class.java)

                projectProperties.application?.applyTo()
            }

            adjustSourceSets()
        }
    }

    private fun Project.adjustSourceSets() {
        val isMultiplatform =
            pluginManager.hasPlugin(settings.libs.plugins.plugin("kotlin.multiplatform").id)

        when (projectProperties.layout) {
            ProjectLayout.FLAT -> {
                val targetName = if (isMultiplatform) "jvm" else "java"

                java.sourceSets.all { sourceSet ->
                    val (compilationPrefixPart, resourcesPrefixPart) = if (SourceSet.isMain(sourceSet))
                        "src" to "resources"
                    else sourceSet.name to "${sourceSet.name}Resources"


                    sourceSet.java.replace("src/${sourceSet.name}/java", "$compilationPrefixPart@$targetName")
                    sourceSet.resources.replace("src/${sourceSet.name}/resources", "$resourcesPrefixPart@$targetName")
                }
            }

            else -> if (isMultiplatform) {
                java.sourceSets.all { sourceSet ->
                    val compilationName = sourceSet.name.capitalized()
                    sourceSet.java.replace("src/${sourceSet.name}/java", "src/jvm$compilationName/java")
                    sourceSet.resources.replace("src/${sourceSet.name}/resources", "src/jvm$compilationName/resources")
                }
            }
        }
    }
}
