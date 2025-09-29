package di.module

import kotlinx.serialization.json.Json
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import clib.ui.presentation.event.navigator.DefaultNavigator
import clib.ui.presentation.event.navigator.Navigator
import ui.navigation.presentation.Destination

@Module
@ComponentScan("ui")
public class CommonModule {

    @Single
    public fun provideJson(): Json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    @Single
    public fun provideNavigator(): Navigator<Destination> = DefaultNavigator(Destination.Main)
}
