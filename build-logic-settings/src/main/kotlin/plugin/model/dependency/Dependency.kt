@file:Suppress("UnstableApiUsage")

package plugin.model.dependency

import gradle.allLibs
import gradle.isUrl
import gradle.libraryAsDependency
import gradle.settings
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.tasks.JvmConstants
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

@Serializable(with = DependencySerializer::class)
internal data class Dependency(
    val notation: String,
    val configuration: String = "implementation"
) {

    context(Settings)
    internal fun resolve(): Any = resolve({
        layout.settingsDirectory.file(it)
    }) { catalogName, libraryName ->
        allLibs[catalogName]?.libraryAsDependency(libraryName)
            ?: error("Not found version catalog: $catalogName")
    }

    context(Settings)
    internal fun applyTo(kotlinDependencyHandler: KotlinDependencyHandler): Any = kotlinDependencyHandler.depFunction(resolve())

    context(Project)
    internal fun resolve(): Any = resolve({
        project(it)
    }) { catalogName, libraryName ->
        settings.allLibs[catalogName]?.libraryAsDependency(libraryName)
            ?: error("Not found version catalog: $catalogName")
    }

    context(Project)
    internal fun applyTo(kotlinDependencyHandler: KotlinDependencyHandler): Unit =
        kotlinDependencyHandler.depFunction(resolve())

    private fun resolve(fromFile: (path: String) -> Any, fromLibs: (catalogName: String, libraryName: String) -> String): Any =
        when {
            notation.startsWith("$") ->
                fromLibs(
                    notation
                        .removePrefix("$")
                        .substringBefore("."),
                    notation
                        .substringAfter("."),
                )

            notation.contains("[/\\\\]".toRegex()) && !notation.isUrl -> fromFile(notation)

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
