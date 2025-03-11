package plugin.project.java

import gradle.all
import gradle.decapitalized
import gradle.java
import gradle.model.kotlin.kmp.android.KotlinAndroidTarget
import gradle.model.kotlin.kmp.jvm.KotlinJvmTarget
import gradle.model.kotlin.sourceSets
import gradle.model.project.ProjectLayout
import gradle.model.project.ProjectType
import gradle.projectProperties
import gradle.replace
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ApplicationPlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.SourceSet
import org.gradle.internal.extensions.stdlib.capitalized
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.targets.jvm.tasks.KotlinJvmTest

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

            projectProperties.jvm?.applyTo()

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
