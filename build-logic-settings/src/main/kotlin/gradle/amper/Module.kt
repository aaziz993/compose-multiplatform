package gradle.amper

import gradle.amper.model.Properties
import gradle.decodeFromAny
import gradle.deepMerge
import kotlinx.serialization.json.Json
import org.jetbrains.amper.frontend.AmperModule
import org.jetbrains.amper.frontend.schema.commonSettings
import org.jetbrains.amper.gradle.moduleDir
import org.yaml.snakeyaml.Yaml
import kotlin.io.path.readText

private val json = Json { ignoreUnknownKeys = true }

internal val AmperModule.isComposeEnabled
    get()= origin.commonSettings.compose.enabled

@Suppress("UNCHECKED_CAST")
internal val AmperModule.additionalProperties: Properties
    get() {
        val yaml = Yaml()
        val map: Map<String, *> = yaml.load(moduleDir.resolve("module.yaml").readText())

        val merged = (map["apply"] as List<String>).fold(emptyMap<String, Any?>()) { props, path ->
            props.deepMerge(yaml.load(moduleDir.resolve(path).readText()))
        }.deepMerge(map)

        return json.decodeFromAny(merged)
    }