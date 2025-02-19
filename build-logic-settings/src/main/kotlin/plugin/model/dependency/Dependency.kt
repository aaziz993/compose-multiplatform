@file:Suppress("UnstableApiUsage")

package plugin.model.dependency

import gradle.asLibs
import gradle.libraries
import gradle.library
import gradle.libs
import gradle.module
import java.io.File
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.initialization.Settings

@Serializable(with = DependencySerializer::class)
internal data class Dependency(
    val notation: String,
    val compile: Boolean = true,
    val runtime: Boolean = false,
    val exported: Boolean = false
) {

    context(Settings)
    internal fun toDependencyNotation(): Any = toDependencyNotation(layout.rootDirectory)

    context(Project)
    internal fun toDependencyNotation(): Any = toDependencyNotation(layout.projectDirectory)

    private fun toDependencyNotation(directory: Directory): Any =
        when {
            notation.startsWith("$") ->
                notation
                    .removePrefix("$")
                    .substringBefore(".")
                    .let(::File).let { file ->
                        if (file.isAbsolute) file else file.relativeTo(directory.asFile)
                    }.asLibs().libraries.library(
                        notation.substringAfter(".", "").replace(".", "-"),
                    ).module

            notation.contains("[/\\\\]".toRegex()) -> directory.files(notation)

            else -> notation
        }
}
