@file:Suppress("UnstableApiUsage")

package plugin.model.dependency

import gradle.allLibs
import gradle.isUrl
import gradle.libraryAsDependency
import gradle.settings
import java.io.File
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.FileCollection
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.tasks.JvmConstants
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.tomlj.TomlParseResult

@Serializable(with = ProjectDependencySerializer::class)
internal sealed class ProjectDependency {

    abstract val notation: String
}

@Serializable
internal sealed class StandardDependency : ProjectDependency() {

    abstract val configuration: String

    context(Settings)
    fun resolve(): Any = resolve(
        allLibs,
        layout.settingsDirectory,
    )

    context(Project)
    fun resolve(): Any = resolve(
        settings.allLibs,
        layout.projectDirectory,
    ) { notation ->
        if (notation.startsWith(":")) {
            project(notation)
        }
        else {
            notation
        }
    }

    context(Project)
    fun applyTo(handler: DependencyHandlerScope) {
        handler.add(configuration, resolve())
    }

    open fun additionalConfiguration(handler: KotlinDependencyHandler, notation: Any) = notation

    context(Project)
    open fun applyTo(handler: KotlinDependencyHandler): Unit =
        handler.depFunction(additionalConfiguration(handler, resolve()))

    private fun resolve(
        libs: Map<String, TomlParseResult>,
        directory: Directory,
        fromNotation: (String) -> Any = { it }
    ): Any =
        when {
            notation.startsWith("$") -> {
                val catalogName = notation
                    .removePrefix("$")
                    .substringBefore(".")
                val libraryName = notation
                    .substringAfter(".")

                fromNotation(
                    libs[catalogName]?.libraryAsDependency(libraryName)
                        ?: error("Not found version catalog: $catalogName"),
                )
            }

            notation.contains("[/\\\\]".toRegex()) && !notation.isUrl -> directory.file(notation)

            else -> fromNotation(notation)
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

@Serializable
internal data class Dependency(
    override val notation: String,
    override val configuration: String = "implementation"
) : StandardDependency()

@Serializable
internal data class NpmDependency(
    override val notation: String,
    override val configuration: String = "implementation",
    val npmConfiguration: String,
) : StandardDependency() {

    override fun additionalConfiguration(handler: KotlinDependencyHandler, notation: Any): Any {
        if (notation is FileCollection) {
            return npm(handler, notation.singleFile)
        }

        val npmNotation = notation.toString().removePrefix("npm:")

        return npm(
            handler,
            npmNotation.substringBefore(":"),
            npmNotation.substringAfter(":"),
        )
    }

    private fun npm(handler: KotlinDependencyHandler, file: File) = when (configuration) {
        "npm" -> handler.npm(file)
        "devNpm" -> handler.devNpm(file)
        "optionalNpm" -> handler.optionalNpm(file)
        else -> error("Unsupported npm dependency configuration: $configuration")
    }

    private fun npm(handler: KotlinDependencyHandler, name: String, version: String) = when (configuration) {
        "npm" -> handler.npm(name, version)
        "devNpm" -> handler.devNpm(name, version)
        "optionalNpm" -> handler.optionalNpm(name, version)
        "peerNpm" -> handler.peerNpm(name, version)
        else -> error("Unsupported npm dependency configuration: $configuration")
    }
}

@Serializable
internal data class PodDependency(override val notation: String) : ProjectDependency() {

    fun toCocoapodsDependency() {
//        CocoapodsExtension.CocoapodsDependency()
    }
}

internal fun String.toVersionCatalogUrlPath(): String {
    val fileNamePart = substringAfter(":").replace(":", "-")
    return "${substringBeforeLast(":").replace("[.:]".toRegex(), "/")}/${
        substringAfterLast(":")
    }/$fileNamePart.toml"
}


