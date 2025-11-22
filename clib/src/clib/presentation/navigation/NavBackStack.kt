package clib.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.serialization.NavBackStackSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import klib.data.type.auth.model.Auth
import klib.data.type.serialization.subclass
import kotlin.reflect.KClass
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer

@Composable
internal fun rememberNavBackStack(
    routes: Routes,
    auth: Auth = Auth(),
    authRedirectRoute: NavRoute? = null,
): NavBackStack<NavRoute> = rememberSerializable(
    serializer = NavBackStackSerializer(PolymorphicSerializer(NavRoute::class)),
    configuration = SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(baseClass = NavRoute::class) {
                routes.routes.forEach { route ->
                    subclass(route.navRoute as KClass<NavRoute>, route.navRoute.serializer())
                }
            }
        }
    },
) {
    NavBackStack(
        if (auth.user == null || authRedirectRoute == null) routes.startRoute else authRedirectRoute,
    )
}
