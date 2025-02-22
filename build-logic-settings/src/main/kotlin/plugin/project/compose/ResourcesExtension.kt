@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.compose

import gradle.all
import gradle.kotlin
import gradle.projectProperties
import gradle.settings
import gradle.trySet
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.compose.resources.ResourcesExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

internal fun Project.configureResourcesExtension() =
    plugins.withType<ComposePlugin> {
        extensions.configure<ComposeExtension> {
           projectProperties.compose.resources.let { resources ->
                extensions.configure<ResourcesExtension> {
                    ::publicResClass trySet resources.publicResClass
                    ::packageOfResClass trySet resources.packageOfResClass
                    ::generateResClass trySet resources.generateResClass
                    resources.customResourceDirectories?.forEach { (sourceSetName, directory) ->
                        customDirectory(sourceSetName, provider { layout.projectDirectory.dir(directory) })
                    }

                    kotlin.sourceSets.all { sourceSet ->
                        // Adjust composeResources to match flatten directory structure
                        customDirectory(
                            sourceSet.name,
                            provider {
                                layout.projectDirectory.dir(
                                    when (sourceSet.name) {
                                        KotlinSourceSet.COMMON_MAIN_SOURCE_SET_NAME -> "composeResources"
                                        KotlinSourceSet.COMMON_TEST_SOURCE_SET_NAME -> "composeTestResources"
                                        else -> "composeResources@${sourceSet.name}"
                                    },
                                )
                            },
                        )
                    }
                }
            }
        }
    }
