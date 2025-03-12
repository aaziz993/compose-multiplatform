package plugins.java

import gradle.accessors.java
import gradle.accessors.projectProperties
import gradle.api.all
import gradle.decapitalized
import gradle.file.replace
import gradle.plugins.kmp.android.KotlinAndroidTarget
import gradle.plugins.kmp.jvm.KotlinJvmTarget
import gradle.plugins.kotlin.sourceSets
import gradle.plugins.project.ProjectLayout
import gradle.plugins.project.ProjectType
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.SourceSet
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.kotlin.dsl.dependencies

internal class JavaPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            if (
                projectProperties.kotlin.targets.any { target -> target is KotlinAndroidTarget } ||
                projectProperties.kotlin.targets.none { target -> target is KotlinJvmTarget }
            ) {
                return@with
            }

            plugins.apply(JavaPlugin::class.java)

            projectProperties.java?.applyTo()

            // Apply java application plugin.
            if (projectProperties.type == ProjectType.APP && !projectProperties.compose.enabled) {
                plugins.apply(ApplicationPlugin::class.java)

                projectProperties.application?.applyTo()
            }

            if (!projectProperties.kotlin.enabledKMP) {
                plugins.apply("org.jetbrains.kotlin.jvm")

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

                adjustSourceSets()
            }
        }
    }

    private fun Project.adjustSourceSets() =
        when (projectProperties.layout) {
            ProjectLayout.FLAT -> {
                java.sourceSets.all { sourceSet ->
                    val (srcPrefixPart, resourcesPrefixPart) = if (SourceSet.isMain(sourceSet))
                        "src" to "resources"
                    else sourceSet.name to "${sourceSet.name}Resources"


                    sourceSet.java.replace("src/${sourceSet.name}/java", "$srcPrefixPart@jvm")
                    sourceSet.resources.replace("src/${sourceSet.name}/resources", "$resourcesPrefixPart@jvm")
                }
            }

            else -> Unit
        }
}
