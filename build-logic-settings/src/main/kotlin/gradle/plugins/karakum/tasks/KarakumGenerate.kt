package gradle.plugins.karakum.tasks

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.api.tasks.DefaultTask
import gradle.api.tasks.applyTo
import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
import gradle.doubleQuoted
import gradle.serialization.decodeMapFromString
import gradle.serialization.encodeAnyToString
import io.github.sgrishchenko.karakum.gradle.plugin.tasks.KarakumGenerate
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType

private val JS_TYPE_IMPORTS =
    mapOf(
        "Date" to "js.date.Date",
        "ArrayBuffer" to "js.buffer.ArrayBuffer",
        "Promise" to "js.promise.Promise",
        "Uint8Array" to "js.typedarrays.Uint8Array",
        "ReadableStream" to "web.streams.ReadableStream",
    )

@Serializable
internal data class KarakumGenerate(
    override val dependsOn: LinkedHashSet<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: Set<String>? = null,
    override val finalizedBy: LinkedHashSet<String>? = null,
    override val shouldRunAfter: Set<String>? = null,
    override val name: String? = null,
    val configFile: String? = null,
    val extensionDirectory: String? = null,
) : DefaultTask<KarakumGenerate>() {

    context(Project)
    override fun applyTo(receiver: KarakumGenerate) =
        pluginManager.withPlugin(settings.libs.plugins.plugin("karakum").id) {
            super.applyTo(receiver)

            receiver.configFile tryAssign configFile?.let(::file)

            receiver.extensionDirectory tryAssign extensionDirectory?.let(layout.projectDirectory::dir)

            receiver.doLast {
                this as KarakumGenerate

                val karakumConfigFile = configFile.asFile.get()

                if (!karakumConfigFile.exists()) {
                    return@doLast
                }

                val karakumConfig = Json.Default.decodeMapFromString(karakumConfigFile.readText())

                val karakumOutput = file(karakumConfig["output"]!!.toString())

                // Add internal modifier to all generated declarations
                karakumOutput
                    .walkTopDown()
                    .filter { file -> file.isFile && file.extension == "kt" }
                    .mapNotNull { file ->
                        val content = file.readText()

                        // Use a regex to find and replace missing visibility modifiers
                        val modifiedContent =
                            content.replace(
                                Regex("""^((?:sealed\s*)?external|typealias)(?=\s+)"""),
                                "internal $1",
                            )
                        file.writeText(modifiedContent)

                        JS_TYPE_IMPORTS
                            .filterKeys { key ->
                                content.contains("""([:=])$key(\?|<|$)""".toRegex())
                            }.values
                            .takeIf(Collection<*>::isNotEmpty)
                            ?.map(String::doubleQuoted)
                            ?.let { imports ->
                                file.toRelativeString(karakumOutput).replace("\\", "/") to imports
                            }

                    }.toMap()
                    .takeIf(Map<*, *>::isNotEmpty)
                    ?.let { imports ->
                        karakumConfigFile.writeText(
                            Json.Default.encodeAnyToString(
                                karakumConfig.toMutableMap().apply {
                                    this["importInjector"] = imports
                                },
                            ),
                        )
                    }
            }
        }

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<KarakumGenerate>())
}
