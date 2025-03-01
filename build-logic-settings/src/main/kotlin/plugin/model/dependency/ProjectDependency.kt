@file:Suppress("UnstableApiUsage")

package plugin.model.dependency

import gradle.allLibs
import gradle.isUrl
import gradle.resolve
import gradle.settings
import java.io.File
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
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

@Serializable
internal sealed class ProjectDependency {

    abstract val notation: String
}

internal object ProjectDependencyTransformingSerializer :
    JsonTransformingSerializer<ProjectDependency>(ProjectDependency.serializer()) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        if (element is JsonObject) {
            val key = element.keys.single()
            val value = element.values.single()

            when {
                key.endsWith("npm", true) -> {
                    if (value is JsonObject) {
                        return JsonObject(
                            buildMap {
                                put("type", JsonPrimitive("dependency"))
                                put("subConfiguration", JsonPrimitive(key))
                                putAll(value.jsonObject)
                            },
                        )
                    }

                    return JsonObject(
                        mapOf(
                            "type" to JsonPrimitive("dependency"),
                            "notation" to value,
                            "subConfiguration" to JsonPrimitive(key),
                        ),
                    )
                }

                key == "pod" -> {
                    if (value is JsonObject) {
                        return JsonObject(
                            buildMap {
                                put("type", JsonPrimitive(key))
                                putAll(value.jsonObject)
                            },
                        )
                    }

                    return JsonObject(
                        mapOf(
                            "type" to JsonPrimitive(key),
                            "notation" to value,
                        ),
                    )
                }

                else -> return JsonObject(
                    mapOf(
                        "type" to JsonPrimitive("dependency"),
                        "notation" to JsonPrimitive(key),
                        "configuration" to value,
                    ),
                )
            }
        }

        return JsonObject(
            mapOf(
                "type" to JsonPrimitive("dependency"),
                "notation" to element,
            ),
        )
    }

//    override fun transformSerialize(element: JsonElement): JsonElement =
//        JsonObject(
//            mapOf(
//                when (element.jsonObject["type"].jsonPrimitive.content) {
//                    "dependency" -> {
//                        val subConfigurationMap = element.jsonObject["subConfiguration"]?.let { subConfiguration ->
//                            subConfiguration to JsonPrimitive("")
//                        }
//
//                        element.jsonObject["configuration"]?.let { configuration ->
//                            element.jsonObject["notation"]!!.jsonPrimitive.content to configuration
//                        }
//
//
//
//                        "pod" to JsonPrimitive("")
//                    }
//
//                    "pod" -> {
//                        "pod" to JsonPrimitive("")
//                    }
//                },
//            ),
//        )
}

@Serializable
@SerialName("dependency")
internal data class Dependency(
    override val notation: String,
    val configuration: String = "implementation",
    val subConfiguration: String? = null
) : ProjectDependency() {

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

    context(Project)
    fun applyTo(handler: KotlinDependencyHandler): Unit =
        handler.configurationFunction(subConfiguration(handler, resolve()))

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

    private val configurationFunction: KotlinDependencyHandler.(Any) -> Unit
        get() = when (configuration) {
            JvmConstants.API_CONFIGURATION_NAME -> KotlinDependencyHandler::api
            JvmConstants.IMPLEMENTATION_CONFIGURATION_NAME -> KotlinDependencyHandler::implementation
            JvmConstants.COMPILE_ONLY_CONFIGURATION_NAME -> KotlinDependencyHandler::compileOnly
            JvmConstants.RUNTIME_ONLY_CONFIGURATION_NAME -> KotlinDependencyHandler::runtimeOnly
            "kotlin" -> {
                { kotlin(it.toString()) }
            }

            else -> error("Unsupported dependency configuration: $configuration")
        }

    private fun subConfiguration(handler: KotlinDependencyHandler, notation: Any) =
        when {
            subConfiguration == null -> notation

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
    fun toCocoapodsDependency(): CocoapodsExtension.CocoapodsDependency {
        val cocoapodsNotation = (if (notation.startsWith("$"))
            settings.allLibs.resolve(notation)
        else notation).removePrefix("cocoapods:")

        return CocoapodsExtension.CocoapodsDependency(
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






