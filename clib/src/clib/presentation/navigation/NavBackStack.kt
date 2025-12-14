package clib.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.serialization.NavBackStackSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import klib.data.type.serialization.subclass
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer

@Composable
internal fun rememberNavBackStack(
    routes: Routes,
    startRoute: NavRoute? = null,
): NavBackStack<NavRoute> = rememberSerializable(
    serializer = NavBackStackSerializer(PolymorphicSerializer(NavRoute::class)),
    configuration = SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavRoute::class) {
                routes.routes.forEach { route ->
                    subclass(route.navRoute, route.navRoute.serializer())
                }
            }
        }
    },
) {
    NavBackStack(startRoute ?: routes.startRoute)
}
