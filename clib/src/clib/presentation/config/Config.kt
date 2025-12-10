package clib.presentation.config

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import clib.presentation.noLocalProvidedFor
import com.charleskorn.kaml.Yaml
import klib.data.config.LogConfig
import klib.data.config.ServerConfig
import klib.data.config.auth.AuthConfig
import klib.data.config.http.client.HttpClientConfig
import klib.data.config.locale.LocalizationConfig
import klib.data.coroutines.runBlocking
import klib.data.type.collections.deepGetOrNull
import klib.data.type.collections.list.asList
import klib.data.type.collections.map.asStringNullableMap
import klib.data.type.primitives.string.ifNotEmpty
import klib.data.type.serialization.IMPORTS_KEY
import klib.data.type.serialization.coders.tree.deserialize
import klib.data.type.serialization.decodeFile
import klib.data.type.serialization.json.decodeAnyFromString
import klib.data.type.serialization.properties.Properties
import klib.data.type.serialization.yaml.decodeAnyFromString
import klib.data.validator.Validator
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Suppress("ComposeCompositionLocalUsage")
public val LocalConfig: ProvidableCompositionLocal<Config> =
    staticCompositionLocalOf { noLocalProvidedFor("LocalConfig") }

@Serializable
public data class Config(
    override val log: LogConfig = LogConfig(),
    override val localization: LocalizationConfig = LocalizationConfig(),
    override val validator: Map<String, Map<String, Validator>> = emptyMap(),
    override val httpClient: HttpClientConfig = HttpClientConfig(),
    override val auth: AuthConfig = AuthConfig(),
    override val ui: UIConfig = UIConfig(),
    override val server: ServerConfig = ServerConfig()
) : klib.data.config.Config {

    public companion object {

        public operator fun invoke(readText: suspend (file: String) -> String): Config {
            val bootstrap = loadBootstrap(readText = readText)
            val environment = bootstrap["environment"]?.toString().orEmpty()
            val applicationFile = "files/application${environment.ifNotEmpty { "-$it" }}.yaml"

            return decodeFile<Map<String, Any?>>(
                applicationFile,
                { file, decodedFile ->
                    decodedFile.deepGetOrNull(IMPORTS_KEY).second?.asList<String>()?.map { import ->
                        "${file.substringBeforeLast("/")}/$import"
                    }
                },
                decoder = { file ->
                    runBlocking {
                        val text = readText(file)
                        val extension = file.substringAfterLast(".", "")
                        when (extension) {
                            "yaml" -> Yaml.default.decodeAnyFromString(text)
                            "json" -> Json.decodeAnyFromString(text)
                            "properties" -> Properties.decodeAnyFromString(text)

                            else -> throw IllegalArgumentException("Unknown file extension '$extension'")
                        }!!.asStringNullableMap
                    }
                },
            ).deserialize<Config>()
        }

        private fun loadBootstrap(
            path: String = "bootstrap.yaml",
            readText: suspend (file: String) -> String,
        ): Map<String, Any?> = runBlocking {
            Yaml.default.decodeAnyFromString(readText("files/$path"))
                ?.asStringNullableMap ?: emptyMap()
        }
    }
}
