@file:Suppress("UnstableApiUsage")

package plugin.model.dependency

import gradle.allLibs
import gradle.isUrl
import gradle.libraryAsDependency
import gradle.libs
import gradle.resolve
import gradle.serialization.getPolymorphicSerializer
import gradle.settings
import java.io.File
import kotlin.String
import kotlin.text.endsWith
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.FileCollection
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.tasks.JvmConstants
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import org.tomlj.TomlParseResult
import plugin.project.kotlin.cocoapods.model.CocoapodsExtension
import plugin.project.kotlin.cocoapods.model.CocoapodsExtension.CocoapodsDependency.PodLocation
import plugin.project.kotlin.model.language.KotlinTarget

@Serializable(with = ProjectDependencySerializer::class)
internal sealed class ProjectDependency {

    abstract val notation: String
}

internal object ProjectDependencyPolymorphicSerializer : JsonContentPolymorphicSerializer<ProjectDependency>(ProjectDependency::class) {

    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<ProjectDependency> {
        val type = element.jsonObject["type"]!!.jsonPrimitive.content
        return ProjectDependency::class.getPolymorphicSerializer(type)!!
    }
}

internal object ProjectDependencySerializer :
    JsonTransformingSerializer<ProjectDependency>(ProjectDependencyPolymorphicSerializer) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        if (element is JsonObject) {
            val key = element.keys.single()
            val value = element.values.single()

            when {
                key.endsWith("npm", true) || key == "pod" -> {
                    val npmConfiguration = if (key.endsWith("npm")) mapOf(
                        "npmConfiguration" to JsonPrimitive(key),
                    )
                    else emptyMap()

                    if (value is JsonObject) {
                        return JsonObject(
                            buildMap {
                                put("type", JsonPrimitive(key))
                                putAll(npmConfiguration)
                                putAll(value.jsonObject)
                            },
                        )
                    }

                    return JsonObject(
                        mapOf(
                            "type" to JsonPrimitive(key),
                            "notation" to value,
                        ) + npmConfiguration,
                    )
                }

                else -> return JsonObject(
                    mapOf(
                        "type" to JsonPrimitive("dep"),
                        "notation" to JsonPrimitive(key),
                        "configuration" to value,
                    ),
                )
            }
        }

        return JsonObject(
            mapOf(
                "type" to JsonPrimitive("dep"),
                "notation" to element,
            ),
        )
    }
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
                fromNotation(libs.resolve(notation))
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
@SerialName("dep")
internal data class Dependency(
    override val notation: String,
    override val configuration: String = "implementation"
) : StandardDependency()

@Serializable
@SerialName("npm")
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
@SerialName("pod")
internal data class PodDependency(
    override val notation: String,
    val moduleName: String? = null,
    val headers: String? = null,
    val source: PodLocation? = null,
    val extraOpts: List<String>? = null,
    val packageName: String? = null,
    val linkOnly: Boolean? = null,
    val interopBindingDependencies: List<String>? = null,
    val podspecDirectory: String? = null,
) : ProjectDependency() {

    context(Project)
    fun toCocoapodsDependency() {
        val cocoapodsNotation = (if (notation.startsWith("$"))
            settings.allLibs.resolve(notation)
        else notation).removePrefix("cocoapods:")

        CocoapodsExtension.CocoapodsDependency(
            cocoapodsNotation.substringBefore(":"),
            moduleName,
            headers,
            cocoapodsNotation.substringAfter(":", "").ifEmpty { null },
            source,
            extraOpts,
            packageName,
            linkOnly,
            interopBindingDependencies,
            podspecDirectory,
        )
    }
}

internal fun String.toVersionCatalogUrlPath(): String {
    val fileNamePart = substringAfter(":").replace(":", "-")
    return "${substringBeforeLast(":").replace("[.:]".toRegex(), "/")}/${
        substringAfterLast(":")
    }/$fileNamePart.toml"
}






