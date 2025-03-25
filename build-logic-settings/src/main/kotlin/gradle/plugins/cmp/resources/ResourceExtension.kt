package gradle.plugins.cmp.resources

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
    fun applyTo() =
        project.pluginManager.withPlugin(project.settings.libs.plugins.plugin("compose.multiplatform").id) {
            project.compose.resources::publicResClass trySet publicResClass
            project.compose.resources::packageOfResClass trySet packageOfResClass
            project.compose.resources::generateResClass trySet generateResClass
            customResourceDirectories?.forEach { (sourceSetName, directory) ->
                project.compose.resources.customDirectory(sourceSetName, project.provider { project.layout.projectDirectory.dir(directory) })
            }

            // Adjust composeResources to match flatten directory structure
            when (project.projectProperties.layout) {
                is ProjectLayout.Flat -> project.kotlin.sourceSets.forEach { sourceSet ->
                    project.compose.resources.customDirectory(
                        sourceSet.name,
                        project.provider { project.sourceSetsToComposeResourcesDirs[sourceSet]!! },
                    )
                }

                else -> Unit
            }
        }
}
