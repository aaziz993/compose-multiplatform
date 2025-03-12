package gradle.repositories

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.FlatDirectoryArtifactRepository
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
    override val content: RepositoryContentDescriptor? = null,
    /**
     * Sets the directories where this repository will look for artifacts.
     *
     * @param dirs the directories.
     * @since 4.0
     */
    val dirs: Set<String>,
) : ArtifactRepository {

    override fun applyTo(repository: org.gradle.api.artifacts.repositories.ArtifactRepository) {
        super.applyTo(repository)

        repository as FlatDirectoryArtifactRepository

        dirs.let(repository::setDirs)
    }

    override fun applyTo(handler: RepositoryHandler) =
        super.applyTo(handler.withType<FlatDirectoryArtifactRepository>()) {
            handler.flatDir(it)
        }
}
