package plugin.project.java

import gradle.id
import gradle.java
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.settings
import gradle.tryReplace
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.SourceSet
import org.gradle.internal.extensions.stdlib.capitalized
import org.jetbrains.amper.gradle.kmpp.KMPPBindingPluginPart
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

            adjustSourceSets()

            // Apply java application plugin.
            if (projectProperties.type == ProjectType.APP && !projectProperties.compose.enabled) {
                plugins.apply(ApplicationPlugin::class.java)

                projectProperties.application?.applyTo()
            }
        }
    }

    private fun Project.adjustSourceSets() {
        val isMultiplatform =
            pluginManager.hasPlugin(settings.libs.plugins.plugin("kotlin.multiplatform").id)

        when (projectProperties.layout) {
            ProjectLayout.FLAT -> java.sourceSets.all {
                val (compilationPrefixPart, resourcesPrefixPart) = if (SourceSet.isMain(this)) {
                    "src" to "resources"
                }
                else {
                    SourceSet.TEST_SOURCE_SET_NAME to "${SourceSet.TEST_SOURCE_SET_NAME}Resources"
                }

                java.tryReplace("src/$name/java", "$compilationPrefixPart@jvm")
                resources.tryReplace("src/$name/resources", "$resourcesPrefixPart@jvm")
            }

            else -> if (isMultiplatform) {
                java.sourceSets.all {
                    val compilationName = name.capitalized()
                    java.tryReplace("src/$name/java", "src/jvm$compilationName/java")
                    resources.tryReplace("src/$name/resources", "src/jvm$compilationName/resources")
                }
            }
        }

        println("$name JAVA SOURCE SETS: ${java.sourceSets.map { it.java.sourceDirectories.map { it.path } }}")
    }
}
