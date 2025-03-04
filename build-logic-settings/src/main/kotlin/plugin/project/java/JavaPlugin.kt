package plugin.project.java

import gradle.all
import gradle.id
import gradle.java
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.replace
import gradle.settings
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.SourceSet
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.kotlin.dsl.dependencies
import plugin.model.dependency.Dependency
import plugin.project.kotlin.kmp.model.android.KotlinAndroidTarget
import plugin.project.kotlin.kmp.model.jvm.KotlinJvmTarget
import plugin.project.kotlin.model.sourceSets
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

            if (!projectProperties.kotlin.needKmp) {
                projectProperties.kotlin.sourceSets<KotlinJvmTarget>()?.forEach { sourceSet ->
                    dependencies {

                    }
                }
            }

            adjustSourceSets()
        }
    }

    private fun Project.adjustSourceSets() {
        val isMultiplatform = projectProperties.kotlin.needKmp

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
