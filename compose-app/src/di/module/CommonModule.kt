package di.module

import clib.presentation.auth.AuthState
import dev.jordond.connectivity.Connectivity
import klib.data.cache.Cache
import klib.data.cache.SettingsCache
import klib.data.net.createConnectivity
import klib.data.type.serialization.json.decodeAnyFromString
import klib.data.type.serialization.json.encodeAnyToString
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
@ComponentScan("ui", "presentation")
public class CommonModule {

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
