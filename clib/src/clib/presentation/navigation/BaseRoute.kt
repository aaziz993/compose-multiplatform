package clib.presentation.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
        get() = requireNotNull(this::class as? KClass<out NavRoute>) { "No nav route" }
    public open val metadata: Map<String, Any> = emptyMap()

    public open val navigationItem: NavigationItem? = null
    public open val authResource: AuthResource? = null

    public fun isAuth(auth: Auth): Boolean =
        authResource?.validate(auth.provider, auth.user) != false

    context(scope: EntryProviderScope<NavRoute>)
    internal abstract fun entry(
        router: Router,
        navigator: @Composable (Routes) -> Navigator,
        hasBack: Boolean,
    )

    public open fun isNavigationItem(auth: Auth): Boolean = navigationItem != null && isAuth(auth)

    @Composable
    public open fun item(
        label: @Composable BaseRoute.() -> String = { toString() },
        auth: Auth = Auth(),
        pushDuplicate: Boolean = false,
    ): NavigationSuiteScope.() -> Unit {
        if (!isNavigationItem(auth)) return {}
        check(this is NavRoute) { "Not a nav route '$this'" }

        val router = LocalParentNavigator.current!!
        val selected = this == router.backStack.lastOrNull()
        val item = navigationItem!!.item(label(), selected)

        return {
            item(
                selected,
                { router.actions(NavigationAction.Push(this@BaseRoute, pushDuplicate)) },
                item.icon,
                item.modifier,
                navigationItem!!.enabled,
                item.text,
                navigationItem!!.alwaysShowLabel,
                item.badge,
            )
        }
    }

    @Suppress("ComposeParameterOrder")
    @Composable
    context(scope: RowScope)
    public open fun NavigationBarItem(
        label: @Composable BaseRoute.() -> String = { toString() },
        auth: Auth = Auth(),
        pushDuplicate: Boolean = false,
    ) {
        if (!isNavigationItem(auth)) return
        check(this is NavRoute) { "Not a route '$this'" }

        val router = LocalParentNavigator.current!!
        val selected = this == router.backStack.lastOrNull()
        val item = navigationItem!!.item(label(), selected)

        scope.NavigationBarItem(
            selected,
            { router.actions(NavigationAction.Push(this, pushDuplicate)) },
            item.icon,
            item.modifier,
            navigationItem!!.enabled,
            item.text,
            navigationItem!!.alwaysShowLabel,
        )
    }

    override fun toString(): String = navRoute.serializer().descriptor.serialName
}

public abstract class Route<T : NavRoute> : BaseRoute() {

    @Composable
    protected abstract fun Content(
        router: Router,
        hasBack: Boolean,
        route: T,
    )

    @Suppress("UNCHECKED_CAST")
    context(scope: EntryProviderScope<NavRoute>)
    final override fun entry(
        router: Router,
        navigator: @Composable (Routes) -> Navigator,
        hasBack: Boolean,
    ): Unit = scope.addEntryProvider(
        navRoute,
        { navRoute -> navRoute.route.toString() },
        metadata,
    ) { navRoute ->
        Content(router, hasBack, navRoute as T)
    }

    final override fun iterator(): Iterator<BaseRoute> = emptyList<BaseRoute>().iterator()
}

public abstract class Routes() : BaseRoute(), NavRoute {

    public abstract val routes: List<BaseRoute>

    public val startRoute: NavRoute by lazy {
        requireNotNull(routes.firstOrNull() as? NavRoute) { "No start route" }
    }

    override val metadata: Map<String, Any>
        get() = startRoute.route.metadata

    override val navigationItem: NavigationItem?
        get() = startRoute.route.navigationItem

    override val authResource: AuthResource?
        get() = startRoute.route.authResource

    @Composable
    protected open fun Content(
        router: Router,
        hasBack: Boolean,
        content: @Composable () -> Unit
    ): Unit = content()

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
        transitionSpec = {
            slideInHorizontally(initialOffsetX = { it }) togetherWith
                slideOutHorizontally(targetOffsetX = { -it })
        },
        popTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                slideOutHorizontally(targetOffsetX = { it })
        },
        predictivePopTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                slideOutHorizontally(targetOffsetX = { it })
        },
        entryProvider = entryProvider,
    )

    context(scope: EntryProviderScope<NavRoute>)
    final override fun entry(
        router: Router,
        navigator: @Composable (Routes) -> Navigator,
        hasBack: Boolean,
    ) = scope.addEntryProvider(
        navRoute,
        { navRoute -> navRoute.route.toString() },
        metadata,
    ) {
        NavHost(router, navigator)
    }

    @Composable
    public fun NavHost(
        router: Router = rememberRouter(),
        navigator: @Composable (Routes) -> Navigator = { routes -> rememberNav3Navigator(routes) },
    ) {
        require(this.startRoute.route in routes) { "Start route '${this.startRoute.route}' isn't in '$routes'" }

        val _navigator = navigator(this)

        Nav3Host(
            router,
            _navigator,
        ) { hasBack ->
            Content(
                router,
                hasBack,
            ) {
                NavDisplay(
                    _navigator.backStack,
                    { _navigator.actions(NavigationAction.Pop) },
                    entryProvider {
                        routes.forEach { route ->
                            route.entry(
                                router,
                                navigator,
                                hasBack,
                            )
                        }
                    },
                )
            }
        }
    }

    public fun isNavigationItems(auth: Auth): Boolean = routes.any { route -> route.isNavigationItem(auth) }

    @Composable
    public fun items(
        label: @Composable BaseRoute.() -> String = { toString() },
        auth: Auth = Auth(),
        pushDuplicate: Boolean = false,
    ): NavigationSuiteScope.() -> Unit {
        val items = routes.map { route -> route.item(label, auth, pushDuplicate) }
        return { items.forEach { item -> item() } }
    }

    @Composable
    context(scope: RowScope)
    public fun NavigationBarItems(
        label: @Composable BaseRoute.() -> String = { toString() },
        auth: Auth = Auth(),
        pushDuplicate: Boolean = false,
    ): Unit = routes.forEach { route -> route.NavigationBarItem(label, auth, pushDuplicate) }

    final override fun iterator(): Iterator<BaseRoute> = sequence {
        routes.forEach { route ->
            yield(route)
            yieldAll(route)
        }
    }.iterator()

    override fun toString(): String = startRoute.route.toString()
}

