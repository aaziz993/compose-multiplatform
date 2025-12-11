package di.module

import config.applicationScript
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import klib.data.config.Config
import klib.data.config.EnabledConfig
import klib.data.location.locale.AggregateLocaleService
import klib.data.location.locale.LocaleService
import klib.data.location.locale.weblate.WeblateApiService
import klib.data.net.http.client.HTTP_CLIENT_JSON
import klib.data.net.http.client.createHttpClient
import klib.data.net.http.client.installPlugin
import klib.data.type.collections.takeUnlessEmpty
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
@ComponentScan
public class ServerModule {

    @Single
    public fun provideConfig(): Config = applicationScript

    @Single
    public fun provideJson(): Json = HTTP_CLIENT_JSON

    @Factory
    public fun provideHttpClient(config: Config, json: Json): HttpClient =
        with(config.httpClient) {
            createHttpClient {
                this@with.timeout?.takeIf(EnabledConfig::enabled)?.let { timeout ->
                    installPlugin(HttpTimeout) {
                        timeout.requestTimeoutMillis?.let { requestTimeoutMillis = it }
                        timeout.connectTimeoutMillis.let { connectTimeoutMillis = it }
                        timeout.socketTimeoutMillis.let { socketTimeoutMillis = it }
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
