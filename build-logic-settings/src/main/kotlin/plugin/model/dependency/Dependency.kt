@file:Suppress("UnstableApiUsage")

package plugin.model.dependency

import gradle.allLibs
import gradle.compose
import gradle.isUrl
import gradle.libraries
import gradle.library
import gradle.libraryAsDependency
import gradle.libs
import gradle.module
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.tasks.JvmConstants
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

@Serializable(with = DependencySerializer::class)
internal data class Dependency(
    val notation: String,
    val configuration: String = "implementation"
) {

    context(Settings)
    internal fun resolve(): Any = resolve(layout.rootDirectory) { catalogName, libraryName ->
        allLibs[catalogName]?.libraryAsDependency(libraryName)
            ?: error("Can't access Version catalog: $catalogName")
    }

    context(Settings)
    internal fun applyTo(kotlinDependencyHandler: KotlinDependencyHandler): Any = kotlinDependencyHandler.depFunction(resolve())

    context(Project)
    internal fun resolve(): Any = resolve(layout.projectDirectory) { catalogName, libraryName ->
        settings.allLibs[catalogName]?.libraryAsDependency(libraryName)
            ?: error("Can't access Version catalog: $catalogName")
    }

    context(Project)
    internal fun applyTo(kotlinDependencyHandler: KotlinDependencyHandler): Unit =
        kotlinDependencyHandler.depFunction(resolve())

    private fun resolve(directory: Directory, library: (catalogName: String, libraryName: String) -> String): Any =
        when {
            notation.startsWith("$") ->
                library(
                    notation
                        .removePrefix("$")
                        .substringBefore("."),
                    notation
                        .substringAfter("."),
                )

            notation.contains("[/\\\\]".toRegex()) && !notation.isUrl -> directory.files(notation)

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

internal fun String.asVersionCatalogUri(): String {
    val fileNamePart = substringAfter(":").replace(":", "-")
    return "${substringBeforeLast(":").replace("[.:]".toRegex(), "/")}/${
        substringAfterLast(":")
    }/$fileNamePart.toml"
}
