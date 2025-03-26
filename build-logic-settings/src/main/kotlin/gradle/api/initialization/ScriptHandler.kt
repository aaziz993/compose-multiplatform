package gradle.api.initialization

import gradle.api.artifacts.dsl.RepositoryHandler
import gradle.api.repositories.ArtifactRepository
import gradle.api.repositories.ArtifactRepositoryTransformingSerializer
import gradle.project.Dependency
import gradle.project.DependencyTransformingSerializer

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.repositories

/**
 *
 * A `ScriptHandler` allows you to manage the compilation and execution of a build script. You can declare the
 * classpath used to compile and execute a build script. This classpath is also used to load the plugins which the build
 * script uses.
 *
 *
 * You can obtain a `ScriptHandler` instance using [Project.getBuildscript] or [ ][org.gradle.api.Script.getBuildscript].
 *
 *
 * To declare the script classpath, you use the [org.gradle.api.artifacts.dsl.DependencyHandler] provided by
 * [.getDependencies] to attach dependencies to the {@value #CLASSPATH_CONFIGURATION} configuration. These
 * dependencies are resolved just prior to script compilation, and assembled into the classpath for the script.
 *
 *
 * For most external dependencies you will also need to declare one or more repositories where the dependencies can
 * be found, using the [org.gradle.api.artifacts.dsl.RepositoryHandler] provided by [ ][.getRepositories].
 */
@Serializable
internal data class ScriptHandler(
    /**
     * Returns a handler to create repositories which are used for retrieving dependencies for the script classpath.
     *
     * @return the repository handler. Never returns null.
     */
    val repositories: RepositoryHandler? = null,
    /**
     * Returns the dependencies of the script. The returned dependency handler instance can be used for adding new
     * dependencies. For accessing already declared dependencies, the configurations can be used.
     *
     * @return the dependency handler. Never returns null.
     * @see .getConfigurations
     */
    val dependencies: Set<@Serializable(with = DependencyKeyTransformingSerializer::class) Dependency>? = null,
) {

    context(Settings)
    fun applyTo() {
        repositories?.let { repositories ->
            settings.buildscript.repositories {
                repositories.forEach { repository ->
                    repository.applyTo(this)
                }
            }
        }

        dependencies?.let { dependencies ->
            dependencies.forEach { dependency ->
                dependency.applyTo(settings.buildscript.dependencies)
            }
        }
    }

    context(Project)
    fun applyTo() {
        repositories?.let { repositories ->
            project.buildscript.repositories {
                repositories.forEach { repository ->
                    repository.applyTo(this)
                }
            }
        }

        dependencies?.let { dependencies ->
            dependencies.forEach { dependency ->
                dependency.applyTo(project.buildscript.dependencies)
            }
        }
    }
}
