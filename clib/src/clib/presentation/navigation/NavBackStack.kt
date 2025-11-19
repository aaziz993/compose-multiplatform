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
public fun rememberNavBackStack(
    vararg elements: NavRoute,
): NavBackStack<NavRoute> {
    return rememberSerializable(
        serializer = NavBackStackSerializer(PolymorphicSerializer(NavRoute::class)),
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(baseClass = NavRoute::class) {
                    elements.forEach { route ->
                        subclass(route::class as KClass<NavRoute>, route::class.serializer())
                    }
                }
            }
        },
    ) {
        NavBackStack(*elements)
    }
}
