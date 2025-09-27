package di.module

import ai.tech.core.presentation.event.navigator.DefaultNavigator
import ai.tech.core.presentation.event.navigator.Navigator
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cache.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import navigation.presentation.Destination
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("screen")
public class CommonModule {

    @Single
    public fun provideJson(): Json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    @Single
    public fun provideNavigator(): Navigator<Destination> = DefaultNavigator(Destination.Main)
}
