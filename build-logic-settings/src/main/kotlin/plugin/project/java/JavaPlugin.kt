package plugin.project.java

import gradle.all
import gradle.decapitalized
import gradle.java
import gradle.projectProperties
import gradle.replace
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.SourceSet
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.kotlin.dsl.dependencies
import gradle.model.kmp.jvm.KotlinJvmTarget
import gradle.model.kmp.jvm.android.KotlinAndroidTarget
import gradle.model.kotlin.sourceSets
import gradle.model.project.ProjectLayout
import gradle.model.project.ProjectType

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

            if (!projectProperties.kotlin.enabledKMP) {
                projectProperties.kotlin.sourceSets<KotlinJvmTarget>()?.forEach { sourceSet ->
                    val compilationPrefix =
                        if (sourceSet.name.endsWith(SourceSet.TEST_SOURCE_SET_NAME, true)) "test" else ""

                    sourceSet.dependencies?.let { dependencies ->
                        dependencies {
                            dependencies.forEach { dependency ->
                                add(
                                    "$compilationPrefix${dependency.configuration.capitalized()}"
                                        .decapitalized(),
                                    dependency.resolve(),
                                )
                            }
                        }
                    }
                }
            }

            adjustSourceSets()
        }
    }

    private fun Project.adjustSourceSets() {
        val isMultiplatform = projectProperties.kotlin.enabledKMP

        when (projectProperties.layout) {
            ProjectLayout.FLAT -> {
                val targetPart = "@${if (isMultiplatform) "jvm" else "java"}"

                java.sourceSets.all { sourceSet ->
                    val (srcPrefixPart, resourcesPrefixPart) = if (SourceSet.isMain(sourceSet))
                        "src" to "resources"
                    else sourceSet.name to "${sourceSet.name}Resources"


                    sourceSet.java.replace("src/${sourceSet.name}/java", "$srcPrefixPart$targetPart")
                    sourceSet.resources.replace("src/${sourceSet.name}/resources", "$resourcesPrefixPart$targetPart")
                }
            }

            else -> if (isMultiplatform) {
                java.sourceSets.all { sourceSet ->
                    val newSourceSetName = "jvm${sourceSet.name.capitalized()}"
                    sourceSet.java.replace("src/${sourceSet.name}/java", "src/$newSourceSetName/java")
                    sourceSet.resources.replace("src/${sourceSet.name}/resources", "src/$newSourceSetName/resources")
                }
            }
        }
    }
}
