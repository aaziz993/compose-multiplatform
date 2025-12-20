package clib.presentation.navigation

import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import clib.presentation.auth.LocalAuthState
import clib.presentation.components.model.item.SelectableItem
import io.ktor.http.Url
import klib.data.auth.model.Auth
import klib.data.auth.model.AuthResource
import klib.data.net.toRoute
import klib.data.net.url
import kotlin.reflect.KClass
import kotlinx.serialization.serializer

@Stable
public sealed class BaseRoute : Iterable<BaseRoute> {

    @Suppress("UNCHECKED_CAST")
    public open val navRoute: KClass<out NavRoute>
        get() = checkNotNull(this::class as? KClass<out NavRoute>) { "No route" }

    private var _urls: List<Url>? = null
    public var urls: List<Url>
        get() = _urls ?: listOf(navRoute.serializer().url()).also { _urls = it }
        set(value) {
            _urls = value
        }

    public var metadata: Map<String, Any> = emptyMap()

    public val name: String
        get() = navRoute.serializer().descriptor.serialName
    public var enabled: Boolean = true
    public var alwaysShowLabel: Boolean = true
    public open val selectableItem: (@Composable (name: String) -> SelectableItem)? = null

    /**
     * Indicates the route is part of authentication/authorizations flow.
     */
    public var isAuth: Boolean = false

    public open fun isAuth(auth: Auth): Boolean = if (auth.user == null) true else !isAuth

    context(scope: EntryProviderScope<NavRoute>)
    internal abstract fun entry(
        routerFactory: @Composable (Routes) -> Router,
        navigatorFactory: @Composable (Routes) -> Navigator,
        sharedTransitionScope: SharedTransitionScope,
    )

    @Composable
    public open fun isNavigationItem(auth: Auth): Boolean = selectableItem != null && isAuth(auth)

    @Composable
    public open fun item(
        enabled: Boolean = this.enabled,
        alwaysShowLabel: Boolean = this.alwaysShowLabel,
        auth: Auth = LocalAuthState.current.value,
        router: Router = currentRouter(),
        onClick: Router.(NavRoute) -> Unit = Router::push,
    ): NavigationSuiteScope.() -> Unit {
        if (!isNavigationItem(auth)) return {}
        check(this is NavRoute) { "Not a nav route '$this'" }

        val selected = this == router.backStack.lastOrNull()
        val item = selectableItem!!(name)
        val selectedItem = item.item(selected)

        return {
            item(
                selected,
                { router.onClick(this@BaseRoute) },
                selectedItem.icon,
                selectedItem.modifier,
                enabled,
                selectedItem.text,
                alwaysShowLabel,
                selectedItem.badge,
            )
        }
    }

    @Suppress("ComposeParameterOrder")
    @Composable
    context(scope: RowScope)
    public open fun NavigationBarItem(
        enabled: Boolean = this.enabled,
        alwaysShowLabel: Boolean = this.alwaysShowLabel,
        auth: Auth = LocalAuthState.current.value,
        router: Router = currentRouter(),
        onClick: Router.(NavRoute) -> Unit = Router::push,
    ) {
        if (!isNavigationItem(auth)) return
        check(this is NavRoute) { "Not a route '$this'" }

        val selected = this == router.backStack.lastOrNull()
        val item = selectableItem!!(name)
        val selectedItem = item.item(selected)

        scope.NavigationBarItem(
            selected,
            { router.onClick(this) },
            selectedItem.icon,
            selectedItem.modifier,
            enabled,
            selectedItem.text,
            alwaysShowLabel,
        )
    }

    public abstract fun resolve(
        transform: (BaseRoute) -> NavRoute?,
    ): List<NavRoute>?

    public fun resolve(navRoute: NavRoute): List<NavRoute>? = resolve { route ->
        if (route == navRoute.route) route as NavRoute else null
    }

    public fun resolve(url: Url): List<NavRoute>? = resolve { route ->
        route.urls.firstNotNullOfOrNull {
            url.toRoute(route.navRoute.serializer(), it)
        }
    }
}

public abstract class Route<T : NavRoute> : BaseRoute() {

    /**
     * Sets the route need authentication/authorization.
     */
    public var authResource: AuthResource? = null

    final override fun isAuth(auth: Auth): Boolean =
        super.isAuth(auth) && authResource?.validate(auth.provider, auth.user) != false

    @Composable
    protected abstract fun Content(
        route: T,
        sharedTransitionScope: SharedTransitionScope,
    )

    @Suppress("UNCHECKED_CAST")
    context(scope: EntryProviderScope<NavRoute>)
    final override fun entry(
        routerFactory: @Composable (Routes) -> Router,
        navigatorFactory: @Composable (Routes) -> Navigator,
        sharedTransitionScope: SharedTransitionScope,
    ): Unit = scope.addEntryProvider(
        clazz = navRoute,
        metadata = metadata,
    ) { navRoute ->
        Content(navRoute as T, sharedTransitionScope)
    }

    final override fun iterator(): Iterator<BaseRoute> = emptyList<BaseRoute>().iterator()

    final override fun resolve(
        transform: (BaseRoute) -> NavRoute?,
    ): List<NavRoute>? = transform(this)?.let(::listOf)

    override fun toString(): String = name
}

public abstract class Routes() : BaseRoute(), NavRoute {

    public abstract val routes: List<BaseRoute>

    final override fun isAuth(auth: Auth): Boolean =
        super.isAuth(auth) && routes.any { route -> route.isAuth(auth) }

    @Composable
    protected open fun NavDisplay(
        backStack: List<NavRoute>,
        onBack: () -> Unit,
        entryProvider: (NavRoute) -> NavEntry<NavRoute>,
    ): Unit = NavDisplay(
        backStack = backStack,
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
        sharedTransitionScope: SharedTransitionScope,
    ) = scope.addEntryProvider(
        clazz = navRoute,
        metadata = metadata,
    ) {
        Nav3Host(routerFactory, navigatorFactory)
    }

    @Composable
    public fun Nav3Host(
        routerFactory: @Composable (Routes) -> Router = { remember { Router(it) } },
        navigatorFactory: @Composable (Routes) -> Navigator = { rememberNav3Navigator(it) },
        onDeepLink: Router.(Url) -> Unit = Router::push,
    ): Unit = Nav3Host(
        routerFactory(this),
        navigatorFactory(this),
        onDeepLink,
    ) { _, backStack, onBack ->
        SharedTransitionLayout {
            NavDisplay(
                backStack,
                onBack,
                entryProvider {
                    routes.forEach { route ->
                        route.entry(
                            routerFactory,
                            navigatorFactory,
                            this@SharedTransitionLayout,
                        )
                    }
                },
            )
        }
    }

    @Composable
    public fun isNavigationItems(auth: Auth): Boolean = routes.any { route -> route.isNavigationItem(auth) }

    @Composable
    public fun items(
        enabled: (BaseRoute) -> Boolean = BaseRoute::enabled,
        alwaysShowLabel: (BaseRoute) -> Boolean = BaseRoute::alwaysShowLabel,
        auth: Auth = LocalAuthState.current.value,
        router: Router = currentRouter(),
        onClick: Router.(NavRoute) -> Unit = Router::push,
    ): NavigationSuiteScope.() -> Unit {
        val items = routes.map { route ->
            route.item(
                this@Routes.enabled && enabled(route),
                this@Routes.alwaysShowLabel && alwaysShowLabel(route),
                auth,
                router,
                onClick,
            )
        }
        return { items.forEach { item -> item() } }
    }

    @Composable
    context(scope: RowScope)
    public fun NavigationBarItems(
        enabled: (BaseRoute) -> Boolean = BaseRoute::enabled,
        alwaysShowLabel: (BaseRoute) -> Boolean = BaseRoute::alwaysShowLabel,
        auth: Auth = LocalAuthState.current.value,
        router: Router = currentRouter(),
        onClick: Router.(NavRoute) -> Unit = Router::push,
    ): Unit = routes.forEach { route ->
        route.NavigationBarItem(
            this.enabled && enabled(route),
            this.alwaysShowLabel && alwaysShowLabel(route),
            auth,
            router,
            onClick,
        )
    }

    public operator fun contains(route: BaseRoute): Boolean = route in routes

    final override fun iterator(): Iterator<BaseRoute> = sequence {
        routes.forEach { route ->
            yield(route)
            yieldAll(route)
        }
    }.iterator()

    final override fun resolve(
        transform: (BaseRoute) -> NavRoute?,
    ): List<NavRoute>? {
        for (route in routes) {
            if (route !is NavRoute) continue
            val childPath = route.resolve(transform)
            if (childPath != null) return listOf(this) + childPath
        }
        return null
    }

    override fun toString(): String = "$name${routes.map(BaseRoute::name)}"
}
