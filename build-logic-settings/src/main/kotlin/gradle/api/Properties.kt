package gradle.api

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import gradle.api.ci.CI
import gradle.api.initialization.ScriptHandler
import gradle.get
import java.io.File
import klib.data.type.asListOrNull
import klib.data.type.asMap
import klib.data.type.collection.deepMerge
import klib.data.type.eval
import klib.data.type.serialization.encoder.deserialize
import klib.data.type.serialization.yaml.decodeAnyFromString
import kotlin.io.path.Path
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.gradle.api.file.Directory
import org.gradle.api.plugins.ExtraPropertiesExtension

internal interface Properties : Map<String, Any?> {

    val buildscript: ScriptHandler?
    val plugins: Set<Any>?
    val cacheRedirector: Boolean

    companion object {

        const val LOCAL_PROPERTIES_EXT = "local.properties.ext"

        val json = Json {
            ignoreUnknownKeys = true
        }

        val yaml = Yaml(
            configuration = YamlConfiguration(
                strictMode = false,
            ),
        )

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
                val properties = yaml.decodeAnyFromString(propertiesFile.readText()).asMap

                val templatesProperties = (properties["templates"] as List<String>?)?.loadTemplates(directory).orEmpty()

                (templatesProperties deepMerge properties)
                    .eval(context) { keys -> get(*keys.toTypedArray()) }
            }
            else emptyMap()
        }.let(T::class.serializer()::deserialize)

        /** Templates also can contain templates.
         * Loads templates recursively.
         *
         */
        @Suppress("UNCHECKED_CAST")
        private fun List<String>.loadTemplates(directory: Directory): Map<String, Any?> =
            fold(emptyMap()) { acc, templatePath ->
                val template = yaml.decodeAnyFromString(directory.file(templatePath).asFile.readText()).asMap

                acc deepMerge template["templates"]?.asListOrNull<String>()?.map { subTemplatePath ->
                    Path(templatePath).resolve(subTemplatePath).normalize().toString()
                }?.loadTemplates(directory).orEmpty() deepMerge template
            }
    }
}
