package clib.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.snapshots.Snapshot
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.serialization.NavBackStackSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import klib.data.type.serialization.subclass
import kotlin.reflect.KClass
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer

/**
 * Atomically replaces the contents of a SnapshotStateList.
 *
 * Uses Compose's snapshot system to ensure the update is atomic and
 * properly triggers recomposition.
 *
 * @param value The new contents for the list.
 */
public fun NavBackStack<NavRoute>.swap(
    value: List<NavRoute>,
) {
    Snapshot.withMutableSnapshot {
        clear()
        addAll(value)
    }
}

@Composable
internal fun rememberNavBackStack(
    routes: Routes,
    startRoute: NavRoute = routes.startRoute,
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
    NavBackStack(startRoute)
}
