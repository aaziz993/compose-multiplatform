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
        get() = checkNotNull(this::class as? KClass<out NavRoute>) { "No nav route" }
    public open val name: String
        get() = toString()
    public open val metadata: Map<String, Any> = emptyMap()

    public open val navigationItem: (@Composable (name: String) -> NavigationItem)? = null
    public open val authResource: AuthResource? = null

    public fun isAuth(auth: Auth): Boolean =
        authResource?.validate(auth.provider, auth.user) != false

    context(scope: EntryProviderScope<NavRoute>)
    internal abstract fun entry(
        router: Router,
        navigatorFactory: @Composable (Routes) -> Navigator,
    )

    public open fun isNavigationItem(auth: Auth): Boolean = navigationItem != null && isAuth(auth)

    @Composable
    public open fun item(
        router: Router,
        auth: Auth = Auth(),
        pushDuplicate: Boolean = false,
    ): NavigationSuiteScope.() -> Unit {
        if (!isNavigationItem(auth)) return {}
        check(this is NavRoute) { "Not a nav route '$this'" }

        val selected = this == router.backStack.lastOrNull()
        val item = navigationItem!!(name)
        val selectedItem = item.item(selected)

        return {
            item(
                selected,
                { router.actions(NavigationAction.Push(this@BaseRoute, pushDuplicate)) },
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
        router: Router,
        auth: Auth = Auth(),
        pushDuplicate: Boolean = false,
    ) {
        if (!isNavigationItem(auth)) return
        check(this is NavRoute) { "Not a route '$this'" }

        val selected = this == router.backStack.lastOrNull()
        val item = navigationItem!!(name)
        val selectedItem = item.item(selected)

        scope.NavigationBarItem(
            selected,
            { router.actions(NavigationAction.Push(this, pushDuplicate)) },
            selectedItem.icon,
            selectedItem.modifier,
            item.enabled,
            selectedItem.text,
            item.alwaysShowLabel,
        )
    }

    override fun toString(): String = navRoute.serializer().descriptor.serialName
}

public abstract class Route<T : NavRoute> : BaseRoute() {

    @Composable
    protected abstract fun Content(
        router: Router,
        route: T,
    )

    @Suppress("UNCHECKED_CAST")
    context(scope: EntryProviderScope<NavRoute>)
    final override fun entry(
        router: Router,
        navigatorFactory: @Composable (Routes) -> Navigator,
    ): Unit = scope.addEntryProvider(
        navRoute,
        { navRoute -> navRoute.route.toString() },
        metadata,
    ) { navRoute ->
        Content(router, navRoute as T)
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
    override val metadata: Map<String, Any>
        get() = startRoute.route.metadata

    override val navigationItem: (@Composable (name: String) -> NavigationItem)?
        get() = startRoute.route.navigationItem
    override val authResource: AuthResource?
        get() = startRoute.route.authResource

    @Composable
    protected open fun NavDisplay(
        router: Router,
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
        navigatorFactory: @Composable (Routes) -> Navigator,
    ) = scope.addEntryProvider(
        navRoute,
        { navRoute -> navRoute.route.toString() },
        metadata,
    ) {
        NavHost(router, navigatorFactory)
    }

    @Composable
    public fun NavHost(
        router: Router = rememberRouter(),
        navigatorFactory: @Composable (Routes) -> Navigator = { routes -> rememberNav3Navigator(routes) },
    ) {
        check(startRoute.route in routes) { "Start route '${startRoute.route}' isn't in '$routes'" }

        val navigator = navigatorFactory(this)

        Nav3Host(
            router,
            navigator,
        ) {
            NavDisplay(
                router,
                navigator.backStack,
                { navigator.actions(NavigationAction.Pop) },
                entryProvider {
                    routes.forEach { route ->
                        route.entry(
                            router,
                            navigatorFactory,
                        )
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
        pushDuplicate: Boolean = false,
    ): NavigationSuiteScope.() -> Unit {
        val items = routes.map { route -> route.item(router, auth, pushDuplicate) }
        return { items.forEach { item -> item() } }
    }

    @Composable
    context(scope: RowScope)
    public fun NavigationBarItems(
        router: Router,
        label: @Composable BaseRoute.() -> String = { toString() },
        auth: Auth = Auth(),
        pushDuplicate: Boolean = false,
    ): Unit = routes.forEach { route -> route.NavigationBarItem(router, auth, pushDuplicate) }

    final override fun iterator(): Iterator<BaseRoute> = sequence {
        routes.forEach { route ->
            yield(route)
            yieldAll(route)
        }
    }.iterator()
}

