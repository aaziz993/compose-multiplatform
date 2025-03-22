package gradle.plugins.karakum

import gradle.accessors.id
import gradle.accessors.libs
import gradle.accessors.plugin
import gradle.accessors.plugins
import gradle.accessors.settings
import gradle.api.tasks.DefaultTask
import gradle.api.tasks.Task
import gradle.api.tasks.applyTo
import gradle.api.tryAssign
import gradle.collection.SerializableAnyMap
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
    override val name: String = "karakumGenerate",
    val configFile: String? = null,
    val extensionDirectory: String? = null,
) : DefaultTask<KarakumGenerate>() {

    context(Project)
    override fun applyTo(recipient: KarakumGenerate) =
        pluginManager.withPlugin(settings.libs.plugins.plugin("karakum").id) {
            super.applyTo(recipient)

            recipient.configFile tryAssign configFile?.let(::file)

            recipient.extensionDirectory tryAssign extensionDirectory?.let(layout.projectDirectory::dir)

            recipient.doLast {
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

    context(Project)
    override fun applyTo() =
        applyTo(tasks.withType<KarakumGenerate>())
}
