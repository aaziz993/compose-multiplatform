package gradle.plugins.web.js.karakum

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.api.tryAssign
import gradle.serialization.serializer.AnySerializer
import gradle.tasks.Task
import io.github.sgrishchenko.karakum.gradle.plugin.tasks.KarakumGenerate
import kotlinx.serialization.Serializable
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
    override val dependsOn: List<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: Map<String, @Serializable(with = AnySerializer::class) Any>? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: List<String>? = null,
    override val finalizedBy: List<String>? = null,
    override val shouldRunAfter: List<String>? = null,
    override val name: String = "karakumGenerate",
    val configFile: String? = null,
    val extensionDirectory: String? = null,
) : Task {

    context(Project)
    override fun applyTo() =
        pluginManager.withPlugin(settings.libs.plugins.plugin("karakum").id) {
            tasks.withType<KarakumGenerate> {
                configFile tryAssign this@KarakumGenerate.configFile?.let(::file)

                extensionDirectory tryAssign this@KarakumGenerate.extensionDirectory?.let(layout.projectDirectory::dir)

                doLast {
                    val imports = mutableListOf<String>()

                    val karakumConfigFile = file("karakum.config.json")

                    val generatedDir =
                        files(""""output": "(.*?)",""".toRegex().find(karakumConfigFile.readText())!!.groupValues[1])

                    // Add internal modifier to all generated declarations
                    generatedDir.asFileTree.forEach { file ->
                        if (file.extension == "kt") {
                            val content = file.readText()

                            JS_TYPE_IMPORTS.entries
                                .filter { (k, _) ->
                                    content.contains("""(:|=)$k(\?|<|$)""".toRegex())
                                }.map { """"${it.value}"""" }
                                .let {
                                    if (it.isNotEmpty()) {
                                        imports.add(
                                            """
                            "${file.path.replaceFirst("${generatedDir.asPath}\\", "").replace("\\", "/")}" : [
                            ${it.joinToString(",\n")}
                            ]""",
                                        )
                                    }
                                }

                            // Use a regex to find and replace missing visibility modifiers
                            val modifiedContent =
                                content.replace(
                                    Regex("""(?<=^)((sealed\s*)?external|typealias)(?=\s+)"""),
                                    "internal $1",
                                )
                            file.writeText(modifiedContent)
                        }
                    }

                    if (imports.isNotEmpty()) {
                        karakumConfigFile.writeText(
                            karakumConfigFile.readText().replace(
                                """"importInjector"[\s\S]*?:[\s\S]*?\{[\s\S]*?\}""".toRegex(),
                                """
                        "importInjector":{
                           ${imports.joinToString(",\n")}
                        }
                        """.trimIndent(),
                            ),
                        )
                    }
                }
            }
        }
}
