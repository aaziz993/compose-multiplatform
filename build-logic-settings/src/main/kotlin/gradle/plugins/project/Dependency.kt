@file:Suppress("UnstableApiUsage")

package gradle.plugins.project

import gradle.accessors.allLibs
import gradle.accessors.resolveLibraryRef
import gradle.accessors.settings
import gradle.isUrl
import gradle.serialization.serializer.BaseKeyTransformingSerializer
import java.io.File
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.file.Directory
import org.gradle.api.file.FileCollection
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.tasks.JvmConstants
import org.gradle.kotlin.dsl.kotlin
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.tomlj.TomlParseResult

private val SUB_CONFIGURATIONS = listOf("kotlin", "npm", "devNpm", "optionalNpm", "peerNpm")

@Serializable
internal data class Dependency(
    val notation: String,
    val configuration: String = "implementation",
    val subConfiguration: String? = null
) {

    context(Settings)
    fun resolve(): Any = resolve(
        settings.allLibs,
        settings.layout.settingsDirectory,
    )

    context(Settings)
    fun applyTo(receiver: DependencyHandler) {
        receiver.add(configuration, subConfiguration(receiver, resolve()))
    }

    context(Project)
    fun resolve(): Any = resolve(
        project.settings.allLibs,
        project.layout.projectDirectory,
    ) { notation ->
        if (notation.startsWith(":")) project.project(notation) else notation
    }

    context(Project)
    fun applyTo(receiver: DependencyHandler) {
        val config = when {
            project.configurations.findByName(configuration) != null -> configuration

            configuration == "kspCommonMainMetadata" && project.configurations.findByName("ksp") != null ->
                "ksp"

            else -> {
                project.logger.warn("Configuration doesn't exists: $configuration")
                return
            }
        }

        receiver.add(config, subConfiguration(receiver, resolve()))
    }

    private fun subConfiguration(handler: DependencyHandler, notation: Any) =
        when (subConfiguration) {
            null -> notation
            "kotlin" -> handler.kotlin(notation.toString())
            else -> error("Unsupported dependency additional configuration: $subConfiguration")
        }

    context(Project)
    fun applyTo(receiver: KotlinDependencyHandler): Unit =
        receiver.kotlinConfigurationFunction(kotlinSubConfiguration(receiver, resolve()))

    private val kotlinConfigurationFunction: KotlinDependencyHandler.(Any) -> Unit
        get() = when (configuration) {
            JvmConstants.API_CONFIGURATION_NAME -> KotlinDependencyHandler::api
            JvmConstants.IMPLEMENTATION_CONFIGURATION_NAME -> KotlinDependencyHandler::implementation
            JvmConstants.COMPILE_ONLY_CONFIGURATION_NAME -> KotlinDependencyHandler::compileOnly
            JvmConstants.RUNTIME_ONLY_CONFIGURATION_NAME -> KotlinDependencyHandler::runtimeOnly
            else -> error("Unsupported dependency configuration: $configuration")
        }

    private fun kotlinSubConfiguration(handler: KotlinDependencyHandler, notation: Any) =
        when {
            subConfiguration == null -> notation
            subConfiguration == "kotlin" -> handler.kotlin(notation.toString())

            subConfiguration.endsWith("npm") -> {
                if (notation is FileCollection) {
                    npm(handler, notation.singleFile)
                }
                else {

                    val npmNotation = notation.toString().removePrefix("npm:")

                    npm(
                        handler,
                        npmNotation.substringBefore(":"),
                        npmNotation.substringAfter(":", ""),
                    )
                }
            }

            else -> error("Unsupported dependency additional configuration: $subConfiguration")
        }

    private fun npm(handler: KotlinDependencyHandler, file: File) = when (subConfiguration) {
        "npm" -> handler.npm(file)
        "devNpm" -> handler.devNpm(file)
        "optionalNpm" -> handler.optionalNpm(file)
        else -> error("Unsupported dependency npm configuration: $subConfiguration")
    }

    private fun npm(handler: KotlinDependencyHandler, name: String, version: String) = when (subConfiguration) {
        "npm" -> handler.npm(name, version)
        "devNpm" -> handler.devNpm(name, version)
        "optionalNpm" -> handler.optionalNpm(name, version)
        "peerNpm" -> handler.peerNpm(name, version)
        else -> error("Unsupported dependency npm configuration: $subConfiguration")
    }

    private fun resolve(
        libs: Map<String, TomlParseResult>,
        directory: Directory,
        fromNotation: (String) -> Any = { it }
    ): Any =
        when {
            notation.startsWith("$") -> {
                fromNotation(libs.resolveLibraryRef(notation))
            }

            notation.contains("[/\\\\]".toRegex()) && !notation.isUrl -> directory.file(notation)

            else -> fromNotation(notation)
        }
}

internal object DependencyKeyTransformingSerializer : BaseKeyTransformingSerializer<Dependency>(
    Dependency.serializer(),
) {

    override fun transformKey(key: String, value: JsonElement?): JsonObject = JsonObject(
        mapOf(
            when {
                value == null -> "notation"
                key in SUB_CONFIGURATIONS -> "subConfiguration"
                else -> "configuration"
            } to JsonPrimitive(key),
        ),
    )

    override fun transformValue(key: String, value: JsonElement): JsonObject = JsonObject(
        mapOf(
            "notation" to value,
        ),
    )
}






