@file:Suppress("UnstableApiUsage")

package gradle.plugins.project

import gradle.allLibs
import gradle.isUrl
import gradle.resolveLibrary
import gradle.serialization.serializer.BaseKeyTransformingSerializer
import gradle.serialization.serializer.KeyTransformingSerializer
import gradle.settings
import java.io.File
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.FileCollection
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.tasks.JvmConstants
import org.gradle.kotlin.dsl.DependencyHandlerScope
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
        allLibs,
        layout.settingsDirectory,
    )

    context(Project)
    fun resolve(): Any = resolve(
        settings.allLibs,
        layout.projectDirectory,
    ) { notation ->
        if (notation.startsWith(":")) project(notation) else notation
    }

    context(Project)
    fun applyTo(handler: DependencyHandlerScope) {
        val config = when {
            configurations.findByName(configuration) != null -> configuration

            configuration == "kspCommonMainMetadata" && configurations.findByName("ksp") != null ->
                "ksp"

            else -> {
                logger.warn("Configuration doesn't exists: $configuration")
                return
            }
        }

        handler.add(config, subConfiguration(handler, resolve()))
    }

    private fun subConfiguration(handler: DependencyHandlerScope, notation: Any) =
        when (subConfiguration) {
            null -> notation
            "kotlin" -> handler.kotlin(notation.toString())
            else -> error("Unsupported dependency additional configuration: $subConfiguration")
        }

    context(Project)
    fun applyTo(handler: KotlinDependencyHandler): Unit =
        handler.kotlinConfigurationFunction(kotlinSubConfiguration(handler, resolve()))

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
                        npmNotation.substringAfter(":"),
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
                fromNotation(libs.resolveLibrary(notation))
            }

            notation.contains("[/\\\\]".toRegex()) && !notation.isUrl -> directory.file(notation)

            else -> fromNotation(notation)
        }
}

internal object DependencyTransformingSerializer : BaseKeyTransformingSerializer<Dependency>(
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






