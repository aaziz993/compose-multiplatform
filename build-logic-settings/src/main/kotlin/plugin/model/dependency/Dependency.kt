@file:Suppress("UnstableApiUsage")

package plugin.model.dependency

import gradle.libraries
import gradle.library
import gradle.libs
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.tasks.JvmConstants
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

@Serializable(with = DependencySerializer::class)
internal data class Dependency(
    val notation: String,
    val configuration: String = "implementation"
) {

    context(Settings)
    internal fun resolve(): Any = resolve(layout.rootDirectory) { catalogName, libraryName ->
        if (catalogName == "libs") {
            return@resolve libs.libraries.library(libraryName)
        }

        error("setting.gradle.kts has access only to default libs version catalog!")
    }

    context(Settings)
    internal fun applyTo(kotlinDependencyHandler: KotlinDependencyHandler): Any = kotlinDependencyHandler.depFunction(resolve())

    context(Project)
    internal fun resolve(): Any = resolve(layout.projectDirectory) { catalogName, libraryName ->
        libs(catalogName).findLibrary(libraryName).get()
    }

    context(Project)
    internal fun applyTo(kotlinDependencyHandler: KotlinDependencyHandler): Any =
        kotlinDependencyHandler.depFunction(resolve())

    private fun resolve(directory: Directory, library: (catalogName: String, libraryName: String) -> Any): Any =
        when {
            notation.startsWith("$") ->
                library(
                    notation
                        .removePrefix("$")
                        .substringBefore("."),
                    notation
                        .substringAfter(".", "")
                        .replace(".", "-"),
                )

            notation.contains("[/\\\\]".toRegex()) -> directory.files(notation)

            else -> notation
        }

    private val depFunction: KotlinDependencyHandler.(Any) -> Unit
        get() = when (configuration) {
            JvmConstants.IMPLEMENTATION_CONFIGURATION_NAME -> KotlinDependencyHandler::implementation
            JvmConstants.RUNTIME_ONLY_CONFIGURATION_NAME -> KotlinDependencyHandler::runtimeOnly
            JvmConstants.COMPILE_ONLY_CONFIGURATION_NAME -> KotlinDependencyHandler::compileOnly
            JvmConstants.API_CONFIGURATION_NAME -> KotlinDependencyHandler::api
            else -> error("Unsupported dependency configuration: $configuration")
        }
}
