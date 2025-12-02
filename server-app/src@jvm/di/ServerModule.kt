package di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module
@ComponentScan
public class ServerModule {
//
//    @OptIn(ExperimentalSerializationApi::class)
//    @Single
//    public fun provideHttpClient(config: ServerConfigImpl): HttpClient =
//        with(config.httpClient) {
//            createHttpClient {
//                this@with.timeout?.takeIf(EnabledConfig::enabled)?.let {
//                    install(HttpTimeout) {
//                        it.requestTimeoutMillis?.let { requestTimeoutMillis }
//                        it.connectTimeoutMillis.let { connectTimeoutMillis }
//                        it.socketTimeoutMillis.let { socketTimeoutMillis }
//                    }
//                }
//
//                this@with.cache?.takeIf(EnabledConfig::enabled)?.let {
//                    install(HttpCache) {
//                        it.isShared?.let { isShared = it }
//                    }
//                }
//
//                install(ContentNegotiation) {
//                    json(
//                        Json {
//                            isLenient = true
//                            ignoreUnknownKeys = true
//                            explicitNulls = false
//                        },
//                    )
//                }
//
//                log?.takeIf(EnabledConfig::enabled)?.let {
//                    install(Logging) {
//                        logger = Logger.DEFAULT
//                        it.level?.let { level = LogLevel.valueOf(it.uppercase()) }
//                    }
//                }
//            }
//        }
}
