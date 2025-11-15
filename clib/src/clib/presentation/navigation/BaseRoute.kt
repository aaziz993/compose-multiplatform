package clib.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import clib.presentation.navigation.model.NavigationItem
import klib.data.type.auth.AuthResource
import klib.data.type.auth.model.Auth
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
@Immutable
public sealed class BaseRoute : Iterable<BaseRoute> {

    public open val navigationRoute: KClass<out NavRoute>
        get() = requireNotNull(this::class as? KClass<out NavRoute>) { "No rout in '$this'" }

    public open val metadata: Map<String, Any> = emptyMap()

    public open val navigationItem: NavigationItem? = null

    protected open val authResource: AuthResource? = null

    public fun isAuth(auth: Auth): Boolean =
        authResource?.validate(auth.provider, auth.user) != false

    public open fun isNavigationItem(auth: Auth): Boolean = navigationItem != null && isAuth(auth)

    @Composable
    public abstract fun Content(backStack: List<NavRoute>, onBack: () -> Unit)

    public fun navigationItem(
        currentRoute: NavRoute,
        onNavigate: (NavRoute) -> Unit,
        auth: Auth = Auth(),
        content: (
            selected: Boolean,
            onClick: () -> Unit,
            icon: @Composable () -> Unit,
            modifier: Modifier,
            enabled: Boolean,
            label: @Composable () -> Unit,
            alwaysShowLabel: Boolean,
            badge: @Composable () -> Unit,
        ) -> Unit
    ) {
        if (!isNavigationItem(auth)) return

        requireNotNull(this as? NavRoute) { "Not a route '$this'" }

        val selected = this == currentRoute
        val selectedItem = navigationItem!!.item(selected)

        content(
            selected,
            { onNavigate(this) },
            selectedItem.icon,
            selectedItem.modifier,
            navigationItem!!.enabled,
            selectedItem.text,
            navigationItem!!.alwaysShowLabel,
            selectedItem.badge,
        )
    }
}

public abstract class Route<T : NavRoute> : BaseRoute() {

    @Composable
    protected abstract fun Content(route: T)

    @Suppress("UNCHECKED_CAST")
    @Composable
    final override fun Content(
        backStack: List<NavRoute>,
        onBack: () -> Unit,
    ): Unit = Content(backStack.last() as T)

    final override fun iterator(): Iterator<BaseRoute> = emptyList<BaseRoute>().iterator()
}

public abstract class Routes : BaseRoute(), NavRoute {

    public open val isRoot: Boolean = false

    public abstract val routes: List<BaseRoute>

    public open val startRoute: NavRoute
        get() = requireNotNull(routes.first() as? NavRoute) { "No start route in '$this'" }

    @Composable
    protected open fun Content(
        backStack: List<NavRoute>,
        onBack: () -> Unit,
        entryProvider: (NavRoute) -> NavEntry<NavRoute>
    ): Unit = NavDisplay(
        backStack = backStack,
        onBack = onBack,
        entryProvider = entryProvider,
    )

    @Composable
    final override fun Content(backStack: List<NavRoute>, onBack: () -> Unit) {
        val ownBackStack by remember(backStack) {
            derivedStateOf {
                backStack.filter { route -> route.route == this || route.route in routes }.let { stack ->
                    if (stack.last().route == this) stack + startRoute else stack
                }
            }
        }

        BackInterceptionProvider(!isRoot) {
            Content(ownBackStack, onBack) { navRoute ->
                NavEntry(navRoute, navRoute.name, navRoute.route.metadata) {
                    navRoute.route.Content(backStack, onBack)
                }
            }
        }
    }

    final override fun isNavigationItem(auth: Auth): Boolean =
        super.isNavigationItem(auth) && routes.any { route -> route.isNavigationItem(auth) }

    final override fun iterator(): Iterator<BaseRoute> = sequence {
        routes.forEach { route ->
            yield(route)
            yieldAll(route)
        }
    }.iterator()
}


