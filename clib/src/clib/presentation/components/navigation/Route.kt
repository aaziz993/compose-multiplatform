package clib.presentation.components.navigation

import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import klib.data.type.auth.model.Auth
import kotlinx.serialization.serializer

public interface Route : NavKey {

    public val name: String
        get() = this::class.serializer().descriptor.serialName

    @Suppress("UNCHECKED_CAST")
    public val navRoute: NavRoute<Route>
        get() = requireNotNull(this as? NavRoute<Route>) {
            "NavRoute not found for '$this'"
        }
}

public fun List<Route>.hasNavigationItems(auth: Auth = Auth()): Boolean =
    any { route -> route.navRoute.isNavigateItem(auth) }

context(navigationSuiteScope: NavigationSuiteScope)
public fun List<Route>.items(
    auth: Auth = Auth(),
    currentRoute: Route,
    transform: @Composable (name: String) -> String = { it },
    navigateTo: (Route) -> Unit,
): Unit = forEach { route ->
    route.navRoute.item(
        auth,
        currentRoute,
        transform,
        navigateTo,
    )
}


