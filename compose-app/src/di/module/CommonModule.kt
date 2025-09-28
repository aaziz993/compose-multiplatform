package di.module

import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import presentation.event.navigator.DefaultNavigator
import presentation.event.navigator.Navigator
import screen.navigation.presentation.Destination

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
