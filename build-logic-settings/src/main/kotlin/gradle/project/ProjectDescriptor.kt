package gradle.project

import kotlinx.serialization.Serializable
import org.gradle.api.initialization.Settings

/**
 *
 * A `ProjectDescriptor` declares the configuration required to create and evaluate a [ ].
 *
 *
 *  A `ProjectDescriptor` is created when you add a project to the build from the settings script, using [ ][Settings.include]. You can access the descriptors using one of
 * the lookup methods on the [Settings] object.
 */
@Serializable
internal data class ProjectDescriptor(
    /**
     * Name of this project.
     *
     * @return The name of the project. Never returns null.
     */
    val name: String? = null,

    /**
     * The project directory of this project.
     */
    val projectDir: String? = null,
    /**
     * The name of the build file for this project. This name is interpreted relative to the project
     * directory.
     */
    val buildFileName: String? = null,
    /**
     * Sets the path of this project. The path can be used as a unique identifier for this project.
     *
     * @return The path. Never returns null.
     */
    val path: String
) {

    context(Settings)
    @Suppress("UnstableApiUsage")
    fun applyTo() {
        val project = project(path)
        name?.let(project::setName)
        projectDir?.let { projectDir ->
            project.projectDir = layout.settingsDirectory.file(projectDir).asFile
        }
        buildFileName?.let(project::setBuildFileName)
    }
}

