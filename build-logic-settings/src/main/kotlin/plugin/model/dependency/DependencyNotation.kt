@file:Suppress("UnstableApiUsage")

package plugin.model.dependency

import gradle.libraries
import gradle.library
import gradle.libs
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.initialization.Settings
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

@Serializable(with = DependencyNotationSerializer::class)
internal data class DependencyNotation(
    val notation: String,
    val compile: Boolean = true,
    val runtime: Boolean = true,
    val exported: Boolean = false
) {

    context(Settings)
    internal fun toDependencyNotation(): Any = toDependencyNotation(layout.rootDirectory) { catalogName, libraryName ->
        if (catalogName == "libs") {
            return@toDependencyNotation libs.libraries.library(libraryName)
        }

        error("setting.gradle.kts has access only to default libs version catalog!")
    }

    context(Project)
    internal fun toDependencyNotation(): Any =
        toDependencyNotation(layout.projectDirectory) { catalogName, libraryName ->
            libs(catalogName).findLibrary(libraryName).get()
        }

    context(Project)
    internal fun addTo(kotlinDependencyHandler: KotlinDependencyHandler): Any =
        depFunction(compile, runtime, exported)(kotlinDependencyHandler, toDependencyNotation())

    private fun toDependencyNotation(directory: Directory, library: (catalogName: String, libraryName: String) -> Any): Any =
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
}

internal fun depFunction(
    compile: Boolean,
    runtime: Boolean,
    exported: Boolean): KotlinDependencyHandler.(Any) -> Unit = when {
    compile && runtime && !exported -> KotlinDependencyHandler::implementation
    !compile && runtime && !exported -> KotlinDependencyHandler::runtimeOnly
    compile && !runtime && !exported -> KotlinDependencyHandler::compileOnly
    compile && runtime && exported -> KotlinDependencyHandler::api
    compile && !runtime && exported -> error("Exporting a compile-only dependency is not supported")
    !compile && runtime && exported -> error("Cannot export a runtime-only dependency to the consumer's compile classpath")
    else -> KotlinDependencyHandler::implementation
}
