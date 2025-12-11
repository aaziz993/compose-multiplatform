package di.module

import clib.presentation.auth.AuthState
import clib.presentation.config.Config
import compose_app.generated.resources.Res
import dev.jordond.connectivity.Connectivity
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import klib.data.cache.Cache
import klib.data.cache.SettingsCache
import klib.data.config.EnabledConfig
import klib.data.location.locale.AggregateLocaleService
import klib.data.location.locale.LocaleService
import klib.data.location.locale.weblate.WeblateApiService
import klib.data.net.createConnectivity
import klib.data.net.http.client.HTTP_CLIENT_JSON
import klib.data.net.http.client.createHttpClient
import klib.data.net.http.client.installPlugin
import klib.data.type.collections.takeUnlessEmpty
import klib.data.type.serialization.json.decodeAnyFromString
import klib.data.type.serialization.json.encodeAnyToString
import kotlinx.coroutines.MainScope
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
@ComponentScan("ui", "presentation")
public class CommonModule {

    @Single
    public fun provideConfig(): Config = Config { file ->
        Res.readBytes(file).decodeToString()
    }

    @Single
    public fun provideCache(json: Json): Cache<String, Any> =
        SettingsCache(
            valueKClass = Any::class,
            valueEncoder = { value -> json.encodeAnyToString(value) },
            valueDecoder = { value -> json.decodeAnyFromString(value)!! },
        )

    @Single
    public fun provideConnectivity(): Connectivity = createConnectivity(MainScope())

    @Single
    public fun provideAuthState(): AuthState = AuthState()

    @Single
    public fun provideJson(): Json = HTTP_CLIENT_JSON

    @Factory
    public fun provideHttpClient(config: Config, json: Json): HttpClient = with(config.httpClient) {
        createHttpClient {
            this@with.timeout?.takeIf(EnabledConfig::enabled)?.let { timeout ->
                installPlugin(HttpTimeout) {
                    timeout.requestTimeoutMillis?.let { requestTimeoutMillis }
                    timeout.connectTimeoutMillis.let { connectTimeoutMillis }
                    timeout.socketTimeoutMillis.let { socketTimeoutMillis }
                }
            }

            this@with.cache?.takeIf(EnabledConfig::enabled)?.let { cache ->
                installPlugin(HttpCache) {
                    cache.isShared?.let { isShared = it }
                }
            }

            installPlugin(ContentNegotiation) {
                json(json)
            }

            log?.takeIf(EnabledConfig::enabled)?.let {
                installPlugin(Logging) {
                    logger = Logger.DEFAULT
                    it.level?.let { level = LogLevel.valueOf(it.uppercase()) }
                }
            }
        }
    }

    @Single
    public fun provideWeblate(config: Config, httpClient: HttpClient): LocaleService =
        config.localization.weblates.filter(EnabledConfig::enabled).map { weblate ->
            WeblateApiService(
                weblate.baseUrl,
                weblate.apiKey,
                weblate.projectName,
                httpClient,
            )
        }.takeUnlessEmpty()?.let(::AggregateLocaleService) ?: LocaleService()
}
