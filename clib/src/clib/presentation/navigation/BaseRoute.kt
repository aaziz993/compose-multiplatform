package clib.presentation.navigation

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import clib.presentation.navigation.model.NavigationItem
import klib.data.type.auth.AuthResource
import klib.data.type.auth.model.Auth
import kotlin.reflect.KClass
import kotlinx.serialization.serializer

@Stable
@Immutable
public sealed class BaseRoute : Iterable<BaseRoute> {

    @Suppress("UNCHECKED_CAST")
    public open val navRoute: KClass<out NavRoute>
        get() = checkNotNull(this::class as? KClass<out NavRoute>) { "No nav route" }

    public open val metadata: Map<String, Any> = slideTransition()

    public open val name: String
        get() = navRoute.serializer().descriptor.serialName
    public open val navigationItem: (@Composable (name: String) -> NavigationItem)? = null
    public open val authResource: AuthResource? = null

    public fun isAuth(auth: Auth): Boolean =
        authResource?.validate(auth.provider, auth.user) != false

    context(scope: EntryProviderScope<NavRoute>)
    internal abstract fun entry(
        routerFactory: @Composable (Routes) -> Router,
        navigatorFactory: @Composable (Routes) -> Navigator,
    )

    public open fun isNavigationItem(auth: Auth): Boolean = navigationItem != null && isAuth(auth)

    @Composable
    public open fun item(
        auth: Auth = Auth(),
        onPush: Router.(NavRoute) -> Unit = Router::push,
    ): NavigationSuiteScope.() -> Unit {
        if (!isNavigationItem(auth)) return {}
        check(this is NavRoute) { "Not a nav route '$this'" }

        val router = currentRouter()
        val selected = this == router.backStack.lastOrNull()
        val item = navigationItem!!(name)
        val selectedItem = item.item(selected)

        return {
            item(
                selected,
                { router.onPush(this@BaseRoute) },
                selectedItem.icon,
                selectedItem.modifier,
                item.enabled,
                selectedItem.text,
                item.alwaysShowLabel,
                selectedItem.badge,
            )
        }
    }

    @Suppress("ComposeParameterOrder")
    @Composable
    context(scope: RowScope)
    public open fun NavigationBarItem(
        auth: Auth = Auth(),
        onPush: Router.(NavRoute) -> Unit = Router::push,
    ) {
        if (!isNavigationItem(auth)) return
        check(this is NavRoute) { "Not a route '$this'" }

        val router = currentRouter()
        val selected = this == router.backStack.lastOrNull()
        val item = navigationItem!!(name)
        val selectedItem = item.item(selected)

        scope.NavigationBarItem(
            selected,
            { router.onPush(this) },
            selectedItem.icon,
            selectedItem.modifier,
            item.enabled,
            selectedItem.text,
            item.alwaysShowLabel,
        )
    }
}

public abstract class Route<T : NavRoute> : BaseRoute() {

    @Composable
    protected abstract fun Content(route: T)

    @Suppress("UNCHECKED_CAST")
    context(scope: EntryProviderScope<NavRoute>)
    final override fun entry(
        routerFactory: @Composable (Routes) -> Router,
        navigatorFactory: @Composable (Routes) -> Navigator,
    ): Unit = scope.addEntryProvider(
        navRoute,
        { navRoute -> navRoute.route.name },
        metadata,
    ) { navRoute ->
        Content(navRoute as T)
    }

    final override fun iterator(): Iterator<BaseRoute> = emptyList<BaseRoute>().iterator()
}

public abstract class Routes() : BaseRoute(), NavRoute {

    public abstract val routes: List<BaseRoute>

    public val startRoute: NavRoute by lazy {
        checkNotNull(routes.firstOrNull() as? NavRoute) { "No start route" }
    }

    override val name: String
        get() = startRoute.route.name
    override val navigationItem: (@Composable (name: String) -> NavigationItem)?
        get() = startRoute.route.navigationItem
    override val authResource: AuthResource?
        get() = startRoute.route.authResource

    @Composable
    protected open fun NavDisplay(
        backStack: List<NavRoute>,
        onBack: () -> Unit,
        entryProvider: (NavRoute) -> NavEntry<NavRoute>,
    ): Unit = NavDisplay(
        backStack = backStack,
        modifier = Modifier.fillMaxSize(),
        onBack = onBack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider,
    )

    context(scope: EntryProviderScope<NavRoute>)
    final override fun entry(
        routerFactory: @Composable (Routes) -> Router,
        navigatorFactory: @Composable (Routes) -> Navigator,
    ) = scope.addEntryProvider(
        navRoute,
        { navRoute -> navRoute.route.name },
        metadata,
    ) {
        Nav3Host(routerFactory, navigatorFactory)
    }

    @Composable
    public open fun Nav3Host(
        routerFactory: @Composable (Routes) -> Router = { routes -> rememberRouter(routes) },
        navigatorFactory: @Composable (Routes) -> Navigator = { routes -> rememberNav3Navigator(routes) },
    ) {
        check(startRoute.route in routes) { "Start route '${startRoute.route}' isn't in '$routes'" }

        Nav3Host(
            routerFactory(this),
            navigatorFactory(this),
        ) { backStack, onBack, _ ->
            NavDisplay(
                backStack,
                onBack,
                entryProvider {
                    routes.forEach { route ->
                        route.entry(routerFactory, navigatorFactory)
                    }
                },
            )
        }
    }

    public fun isNavigationItems(auth: Auth): Boolean = routes.any { route -> route.isNavigationItem(auth) }

    @Composable
    public fun items(
        router: Router,
        auth: Auth = Auth(),
        onPush: Router.(NavRoute) -> Unit = Router::push,
    ): NavigationSuiteScope.() -> Unit {
        val items = routes.map { route -> route.item(auth, onPush) }
        return { items.forEach { item -> item() } }
    }

    @Composable
    context(scope: RowScope)
    public fun NavigationBarItems(
        router: Router,
        auth: Auth = Auth(),
        onPush: Router.(NavRoute) -> Unit = Router::push,
    ): Unit = routes.forEach { route -> route.NavigationBarItem(auth, onPush) }

    final override fun iterator(): Iterator<BaseRoute> = sequence {
        routes.forEach { route ->
            yield(route)
            yieldAll(route)
        }
    }.iterator()
}

