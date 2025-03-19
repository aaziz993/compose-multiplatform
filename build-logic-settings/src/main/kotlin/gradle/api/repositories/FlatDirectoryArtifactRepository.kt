package gradle.api.repositories

import gradle.api.applyTo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.FlatDirectoryArtifactRepository
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.withType

/**
 * A repository that looks into a number of directories for artifacts. The artifacts are expected to be located in the root of the specified directories.
 * The repository ignores any group/organization information specified in the dependency section of your build script. If you only use this kind of
 * resolver you can specify your dependencies like `:junit:4.8.1` instead of `junit:junit:4.8.1`.
 *
 *
 * To resolve a dependency, this resolver looks for one of the following files. It will return the first match it finds:
 *
 *
 *
 *  * [artifact]-[version].[ext]
 *  * [artifact]-[version]-[classifier].[ext]
 *  * [artifact].[ext]
 *  * [artifact]-[classifier].[ext]
 *
 *
 *
 * So, for example, to resolve `:junit:junit:4.8.1`, this repository will look for `junit-4.8.1.jar` and then `junit.jar`.
 */
@Serializable
@SerialName("flatDir")
internal data class FlatDirectoryArtifactRepository(
    override val name: String = "flatDir",
    override val content: RepositoryContentDescriptorImpl? = null,
    /**
     * Sets the directories where this repository will look for artifacts.
     *
     * @param dirs the directories.
     * @since 4.0
     */
    val dirs: Set<String>,
) : ArtifactRepository<FlatDirectoryArtifactRepository> {

    context(Settings)
    override fun applyTo(handler: RepositoryHandler) =
        applyTo(handler.withType<FlatDirectoryArtifactRepository>()) { _name, action ->
            handler.flatDir {
                name = _name
                action.execute(this)
            }
        }

    context(Project)
    override fun applyTo(handler: RepositoryHandler) =
        applyTo(handler.withType<FlatDirectoryArtifactRepository>()) { _name, action ->
            handler.flatDir {
                name = _name
                action.execute(this)
            }
        }

    override fun _applyTo(named: FlatDirectoryArtifactRepository) {
        super._applyTo(named)

        dirs.let(named::setDirs)
    }
}
