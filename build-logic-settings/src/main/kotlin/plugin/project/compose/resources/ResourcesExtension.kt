@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package plugin.project.compose.resources

import gradle.all
import gradle.compose
import gradle.id
import gradle.kotlin
import gradle.libs
import gradle.plugin
import gradle.plugins
import gradle.projectProperties
import gradle.resources
import gradle.settings
import gradle.trySet
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import plugin.project.model.Layout

internal fun Project.configureResourcesExtension() =
    pluginManager.withPlugin(settings.libs.plugins.plugin("compose.multiplatform").id) {
        compose {
            projectProperties.compose.resources.let { resources ->
                resources {
                    ::publicResClass trySet resources.publicResClass
                    ::packageOfResClass trySet resources.packageOfResClass
                    ::generateResClass trySet resources.generateResClass
                    resources.customResourceDirectories?.forEach { (sourceSetName, directory) ->
                        customDirectory(sourceSetName, provider { layout.projectDirectory.dir(directory) })
                    }

                    when (projectProperties.layout) {
                        // Adjust composeResources to match flatten directory structure
                        Layout.FLAT -> kotlin.sourceSets.all { sourceSet ->
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

                        else -> Unit
                    }
                }
            }
        }
    }
