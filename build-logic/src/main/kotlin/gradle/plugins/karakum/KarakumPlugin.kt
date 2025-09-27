package gradle.plugins.karakum

import gradle.api.configureEach
import gradle.api.project.kotlin
import java.io.File
import klib.data.type.collections.map.asMap
import klib.data.type.primitives.string.uppercaseFirstChar
import klib.data.type.serialization.json.decodeAnyFromString
import kotlinx.serialization.json.Json
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl

private val JS_TYPE_IMPORTS =
    mapOf(
        "Date" to "js.date.Date",
        "ArrayBuffer" to "js.buffer.ArrayBuffer",
        "Promise" to "js.promise.Promise",
        "Uint8Array" to "js.typedarrays.Uint8Array",
        "ReadableStream" to "web.streams.ReadableStream",
    )

public class KarakumPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            pluginManager.withPlugin("io.github.sgrishchenko.karakum") {
                adjustSourceSets()
                adjustKarakumGenerateTask()
            }
        }
    }

    private fun Project.adjustSourceSets(): Unit =
        project.pluginManager.withPlugin("io.github.sgrishchenko.karakum") {
            project.file("karakum.config.json")
                .takeIf(File::exists)
                ?.let(File::readText)
                ?.let(Json.Default::decodeAnyFromString)
                ?.asMap
                ?.let { karakumConfig ->
                    val jsSourceSetNames = project.kotlin.targets
                        .filter { this::class == KotlinJsTargetDsl::class }
                        .flatMap { target ->
                            target.compilations.map { compilation -> "${target.name}${compilation.name.uppercaseFirstChar()}" }
                        }

                    project.kotlin.sourceSets
                        .matching { sourceSet -> sourceSet.name in jsSourceSetNames }
                        .configureEach { sourceSet ->
                            sourceSet.kotlin.srcDir(project.file(karakumConfig["output"]!!))
                        }
                }
        }

    private fun Project.adjustKarakumGenerateTask() =
        tasks.named("generateKarakumExternals") {
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
                                content.contains("""([:=])$k(\?|<|$)""".toRegex())
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
