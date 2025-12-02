package di.module

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
import klib.data.net.http.client.createHttpClient
import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
@ComponentScan
public class ServerModule {

    @Single
    public fun provideHttpClient(config: Config): HttpClient =
        with(config.httpClient) {
            createHttpClient {
                this@with.timeout?.takeIf(EnabledConfig::enabled)?.let { timeout ->
                    install(HttpTimeout) {
                        timeout.requestTimeoutMillis?.let { requestTimeoutMillis = it }
                        timeout.connectTimeoutMillis.let { connectTimeoutMillis = it }
                        timeout.socketTimeoutMillis.let { socketTimeoutMillis = it }
                    }
                }

                this@with.cache?.takeIf(EnabledConfig::enabled)?.let { cache ->
                    install(HttpCache) {
                        cache.isShared?.let { isShared = it }
                    }
                }

                install(ContentNegotiation) {
                    json(
                        Json {
                            isLenient = true
                            ignoreUnknownKeys = true
                            explicitNulls = false
                        },
                    )
                }

                log?.takeIf(EnabledConfig::enabled)?.let {
                    install(Logging) {
                        logger = Logger.DEFAULT
                        it.level?.let { level = LogLevel.valueOf(it.uppercase()) }
                    }
                }
            }
        }
}
