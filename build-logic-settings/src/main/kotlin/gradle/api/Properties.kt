package gradle.api

import gradle.api.catalog.PluginNotationContentPolymorphicSerializer
import gradle.api.ci.CI
import gradle.api.initialization.ScriptHandler
import java.io.File
import klib.data.type.collection.deepMerge
import klib.data.type.serialization.json.decodeFromAny
import klib.data.type.serialization.json.serializer.JsonUnknownPreservingSerializer
import klib.data.type.serialization.json.serializer.JsonOptionalAnySerializer
import kotlin.io.path.Path
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.gradle.api.file.Directory
import org.gradle.api.plugins.ExtraPropertiesExtension
import org.yaml.snakeyaml.Yaml

internal abstract class Properties : HashMap<String, Any?>() {

    abstract val buildscript: ScriptHandler?
    abstract val plugins: Set<@Serializable(with = PluginNotationContentPolymorphicSerializer::class) Any>?
    abstract val cacheRedirector: Boolean

    companion object {

        const val LOCAL_PROPERTIES_EXT = "local.properties.ext"

        val json = Json {
            ignoreUnknownKeys = true
        }

        val yaml = Yaml()

        fun loadLocalProperties(file: File) =
            java.util.Properties().apply {
                file.takeIf(File::exists)
                    ?.reader()
                    ?.let(::load)
            }

        fun ExtraPropertiesExtension.exportExtraProperties() =
            properties.putAll(
                mapOf(
                    "ci" to buildMap {
                        put("present", CI.present)
                        CI.name?.let { name -> put("name", name) }
                    },
                ),
            )

        @Suppress("UNCHECKED_CAST")
        inline fun <reified T : Any> load(
            path: String,
            directory: Directory,
            context: Any,
        ): T = directory.file(path).asFile.let { propertiesFile ->
            if (propertiesFile.exists()) {
                val properties = yaml.load<MutableMap<String, *>>(propertiesFile.readText())

                val templatesProperties = (properties["templates"] as List<String>?)?.loadTemplates(directory).orEmpty()


                (templatesProperties deepMerge properties)
//                        .resolve(context) { keys -> get(*keys.toTypedArray()) },
            }
            else emptyMap()
        }.let(json::decodeFromAny)

        /** Resolves deep templates.
         * Templates also can contain templates.
         */
        @Suppress("UNCHECKED_CAST")
        private fun List<String>.loadTemplates(directory: Directory): Map<String, Any?> =
            fold(emptyMap()) { acc, templatePath ->
                val template = yaml.load<MutableMap<String, *>>(directory.file(templatePath).asFile.readText())

                acc deepMerge (template["templates"] as List<String>?)?.map { subTemplatePath ->
                    Path(templatePath).resolve(subTemplatePath).normalize().toString()
                }?.loadTemplates(directory).orEmpty() deepMerge template
            }
    }
}

internal abstract class PropertiesUnknownPreservingSerializer<T : Properties>(tSerializer: KSerializer<T>) :
    JsonUnknownPreservingSerializer<T, Any?>(
            tSerializer,
            JsonOptionalAnySerializer,
    )
