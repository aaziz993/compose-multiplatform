package di.module

import clib.data.config.ComposeConfig
import clib.presentation.auth.AuthState
import com.charleskorn.kaml.Yaml
import compose_app.generated.resources.Res
import dev.jordond.connectivity.Connectivity
import klib.data.cache.Cache
import klib.data.cache.SettingsCache
import klib.data.coroutines.runBlocking
import klib.data.net.createConnectivity
import klib.data.type.collections.deepGet
import klib.data.type.collections.deepGetOrNull
import klib.data.type.collections.deepMap
import klib.data.type.collections.deepPlus
import klib.data.type.collections.deepSubstitute
import klib.data.type.collections.getOrPut
import klib.data.type.collections.list.asList
import klib.data.type.collections.map.asMapOrNull
import klib.data.type.collections.map.asStringNullableMap
import klib.data.type.collections.set
import klib.data.type.collections.toNewMutableCollection
import klib.data.type.serialization.IMPORTS_KEY
import klib.data.type.serialization.coders.tree.deserialize
import klib.data.type.serialization.decodeFile
import klib.data.type.serialization.json.decodeAnyFromString
import klib.data.type.serialization.json.encodeAnyToString
import klib.data.type.serialization.properties.Properties
import klib.data.type.serialization.yaml.decodeAnyFromString
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.orEmpty
import kotlin.collections.plus
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
@ComponentScan("ui", "presentation")
public class CommonModule {

//    @Single
//    public fun provideConfig(config: ComposeConfig): ComposeConfig = runBlocking {
//        decodeFile<Map<String, Any?>>(
//            "files/application.yaml",
//            { file, decodedFile ->
//                decodedFile.deepGetOrNull(IMPORTS_KEY).second?.asList<String>()?.map { import ->
//                    "files/$import"
//                }
//            },
//            decoder = { file ->
//                val text = Res.readBytes(Res.getUri(file)).decodeToString()
//                val extension = file.substringAfterLast(".", "")
//                when (extension) {
//                    "yaml" -> Yaml.default.decodeAnyFromString(text)
//                    "json" -> Json.decodeAnyFromString(text)
//                    "properties" -> Properties.decodeAnyFromString(text)
//
//                    else -> throw IllegalArgumentException("Unsupported file extension $extension")
//                }!!.asStringNullableMap
//
//            },
//        ).deserialize<ComposeConfig>()
//    }

    @Single
    public fun provideAuthState(): AuthState = AuthState()

    @Single
    public fun provideJson(): Json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    @Single
    public fun provideCache(json: Json): Cache<String, Any> =
        SettingsCache(
            valueKClass = Any::class,
            valueEncoder = { value -> json.encodeAnyToString(value) },
            valueDecoder = { value -> json.decodeAnyFromString(value)!! },
        )

//    @Single
//    public fun provideHttpClient(config: ClientConfigImpl, json: Json): HttpClient = with(config.httpClient) {
//        createHttpClient {
//            this@with.timeout?.takeIf(EnabledConfig::enabled)?.let {
//                install(HttpTimeout) {
//                    it.requestTimeoutMillis?.let { requestTimeoutMillis }
//                    it.connectTimeoutMillis.let { connectTimeoutMillis }
//                    it.socketTimeoutMillis.let { socketTimeoutMillis }
//                }
//            }
//
//            this@with.cache?.takeIf(EnabledConfig::enabled)?.let {
//                install(HttpCache) {
//                    it.isShared?.let { isShared = it }
//                }
//            }
//
//            install(ContentNegotiation) {
//                json(
//                    Json {
//                        isLenient = true
//                        ignoreUnknownKeys = true
//                        explicitNulls = false
//                    },
//                )
//            }
//
//            log?.takeIf(EnabledConfig::enabled)?.let {
//                install(Logging) {
//                    logger = Logger.DEFAULT
//                    it.level?.let { level = LogLevel.valueOf(it.uppercase()) }
//                }
//            }
//        }
//    }

    @Single
    public fun provideConnectivity(): Connectivity = createConnectivity()
}
