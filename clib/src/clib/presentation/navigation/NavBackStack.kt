package clib.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.serialization.NavBackStackSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import klib.data.type.serialization.subclass
import kotlin.reflect.KClass
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer

@Composable
public fun Routes.rememberNavBackStack(
    vararg elements: NavRoute,
): NavBackStack<NavRoute> {
    return rememberSerializable(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(baseClass = NavRoute::class) {
                    forEach { route ->
                        subclass(route.navigationRoute as KClass<NavRoute>, route.navigationRoute.serializer())
                    }
                }
            }
        },
        serializer = NavBackStackSerializer(PolymorphicSerializer(NavRoute::class)),
    ) {
        NavBackStack(*elements)
    }
}
