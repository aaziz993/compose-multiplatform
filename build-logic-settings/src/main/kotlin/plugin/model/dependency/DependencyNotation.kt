@file:Suppress("UnstableApiUsage")

package plugin.model.dependency

import gradle.libs
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.file.Directory
import org.gradle.api.initialization.Settings

@Serializable(with = DependencyNonationSerializer::class)
internal data class DependencyNotation(
    val notation: String,
    val compile: Boolean = true,
    val runtime: Boolean = true,
    val exported: Boolean = false
) {

    context(Settings)
    internal fun toDependencyNotation(): Any = toDependencyNotation(layout.rootDirectory, extensions::libs)

    context(Project)
    internal fun toDependencyNotation(): Any = toDependencyNotation(layout.projectDirectory, extensions::libs)

    private fun toDependencyNotation(directory: Directory, catalog: (name: String) -> VersionCatalog): Any =
        when {
            notation.startsWith("$") ->
                notation
                    .removePrefix("$")
                    .substringBefore(".")
                    .let(catalog).findLibrary(
                        notation.substringAfter(".", "").replace(".", "-"),
                    ).get().also {
                        println("KSP COMPILER: ${it}")
                    }

            notation.contains("[/\\\\]".toRegex()) -> directory.files(notation)

            else -> notation
        }
}
