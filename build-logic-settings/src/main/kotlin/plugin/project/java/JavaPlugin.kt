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
import plugin.project.kotlin.kmp.model.jvm.android.KotlinAndroidTarget
import plugin.project.kotlin.kmp.model.jvm.KotlinJvmTarget
import plugin.project.kotlin.model.sourceSets
import plugin.project.model.ProjectLayout
import plugin.project.model.ProjectType
import plugin.project.model.dependencies

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

            if (!projectProperties.kotlin.enableKMP) {
                projectProperties.dependencies<KotlinJvmTarget>().let { dependencies ->
                    dependencies {
//                        dependencies.filterIsInstance<Dependency>().forEach { dependency ->
//                            when {
//                                dependency.configuration == "kspCommonMainMetadata" -> "ksp"
//                                dependency.configuration == "kspCommonTestMetadata" -> "kspTest"
//                                dependency.configuration.startsWith("ksp")
//                            }
//                        }
                    }
                }

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
        val isMultiplatform = projectProperties.kotlin.enableKMP

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
