package gradle.plugins.cmp.resources.model

import gradle.accessors.compose
import gradle.accessors.id
import gradle.accessors.kotlin
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.projectProperties
import gradle.accessors.resources
import gradle.accessors.settings
import gradle.accessors.sourceSetsToComposeResourcesDirs
import gradle.api.trySet
import gradle.project.ProjectLayout
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.compose.resources.ResourcesExtension

@Serializable
internal data class ResourcesExtension(
    /**
     * Whether the generated resources accessors class should be public or not.
     *
     * Default is false.
     */
    val publicResClass: Boolean? = null,
    /**
     * The unique identifier of the resources in the current project.
     * Uses as package for the generated Res class and for isolation resources in a final artefact.
     *
     * If it is empty then `{group name}.{module name}.generated.resources` will be used.
     *
     */
    val packageOfResClass: String? = null,
    /**
     * The mode of resource class generation.
     *
     * - `auto`: The Res class will be generated if the current project has an explicit "implementation" or "api" dependency on the resource's library.
     * - `always`: Unconditionally generate the Res class. This may be useful when the resources library is available transitively.
     * - `never`: Never generate the Res class.
     */
    val generateResClass: ResourcesExtension.ResourceClassGeneration? = null,
    /**
     * Associates a custom resource directory with a specific source set.
     *
     * @param sourceSetName the name of the source set to associate the custom resource directory with
     * @param directoryProvider the provider that provides the custom directory
     */
    val customResourceDirectories: Map<String, String>? = null
) {

    context(Project)
    fun applyTo() = pluginManager.withPlugin(settings.libs.plugins.plugin("compose.multiplatform").id) {
        compose.resources {
            ::publicResClass trySet this@ResourcesExtension.publicResClass
            ::packageOfResClass trySet this@ResourcesExtension.packageOfResClass
            ::generateResClass trySet this@ResourcesExtension.generateResClass
            this@ResourcesExtension.customResourceDirectories?.forEach { (sourceSetName, directory) ->
                customDirectory(sourceSetName, provider { layout.projectDirectory.dir(directory) })
            }

            // Adjust composeResources to match flatten directory structure
            when (projectProperties.layout) {
                ProjectLayout.FLAT -> kotlin.sourceSets.forEach { sourceSet ->
                    customDirectory(
                        sourceSet.name,
                        provider { sourceSetsToComposeResourcesDirs[sourceSet]!! },
                    )
                }

                else -> Unit
            }
        }
    }
}
