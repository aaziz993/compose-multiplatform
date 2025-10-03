package di.module

import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import clib.presentation.event.navigator.DefaultNavigator
import clib.presentation.event.navigator.Navigator
import navigation.presentation.Destination

@Module
@ComponentScan("presentation")
public class CommonModule {

    @Single
    public fun provideJson(): Json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

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
    public fun provideNavigator(): Navigator<Destination> = DefaultNavigator(Destination.Main)
}
